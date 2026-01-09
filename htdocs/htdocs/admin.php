<?php
require_once 'config.php';
session_start();
if (!isset($_SESSION['usuario_id'])) {
    echo '<p>No autorizado. <a href="index.html">Iniciar sesión</a></p>'; exit();
}

$allowed_tables = ['Habitat','Cuidador','Alimento','Animal','Animal_Permanente','Animal_Temporal','Animal_Mamifero','Animal_Ave','Animal_Reptil','Animal_Anfibio','Supervisa','RegistroAlimentacion','Cria'];
$table = $_GET['table'] ?? 'Habitat';
if (!in_array($table, $allowed_tables)) { echo 'Tabla no permitida.'; exit(); }

// Handle delete via GET param 'delete' with primary key(s)
if (isset($_GET['delete'])) {
    // assume single primary key for common tables like Habitat, Cuidador, etc.
    $pk = intval($_GET['delete']);
    // determine pk field name
    $pkField = '';
    $res = $mysqli->query("DESCRIBE `{$table}`");
    while ($r = $res->fetch_assoc()) {
        if ($r['Key'] === 'PRI') { $pkField = $r['Field']; break; }
    }
    if ($pkField) {
        $stmt = $mysqli->prepare("DELETE FROM `{$table}` WHERE `{$pkField}` = ? LIMIT 1");
        $stmt->bind_param('i', $pk);
        $stmt->execute();
        $stmt->close();
        header('Location: admin.php?table='.$table);
        exit();
    } else {
        echo 'No se encontró PK para borrar.'; exit();
    }
}

// Handle insert via POST (form submission from generated form)
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'insert') {
    $table = $_POST['table'];
    // get columns
    $colsRes = $mysqli->query("DESCRIBE `{$table}`");
    $cols = [];
    $place = [];
    $types = '';
    $vals = [];
    while ($c = $colsRes->fetch_assoc()) {
        if (strpos($c['Extra'],'auto_increment') !== false) continue;
        $cols[] = $c['Field'];
        $place[] = '?';
        $val = $_POST['col'][$c['Field']] ?? null;
        // null handling
        if ($val === '') $val = null;
        if (preg_match('/int|tinyint|smallint|mediumint|bigint/i',$c['Type'])) { $types .= 'i'; $vals[] = ($val === null? null : (int)$val); }
        elseif (preg_match('/decimal|float|double/i',$c['Type'])) { $types .= 'd'; $vals[] = ($val === null? null : (float)$val); }
        else { $types .= 's'; $vals[] = ($val === null? null : (string)$val); }
    }
    if (count($cols) > 0) {
        $sql = "INSERT INTO `{$table}` (".implode(',',$cols).") VALUES (".implode(',',$place).")";
        $stmt = $mysqli->prepare($sql);
        // bind params dynamically
        $bind_names = [];
        $bind_names[] = $types;
        for ($i=0; $i<count($vals); $i++) $bind_names[] = & $vals[$i];
        if ($types !== '') {
            call_user_func_array([$stmt, 'bind_param'], $bind_names);
        }
        $ok = $stmt->execute();
        if (!$ok) echo '<p>Error al insertar: '.htmlspecialchars($stmt->error).'</p>';
        else header('Location: admin.php?table='.$table);
        exit();
    } else {
        echo 'No hay columnas para insertar.'; exit();
    }
}

// If ?form=insert show an insert form
if (isset($_GET['form']) && $_GET['form'] === 'insert') {
    $desc = $mysqli->query("DESCRIBE `{$table}`");
    echo '<h3>Insertar en '.htmlspecialchars($table).'</h3>';
    echo '<form method="post">';
    echo '<input type="hidden" name="action" value="insert">';
    echo '<input type="hidden" name="table" value="'.htmlspecialchars($table).'">';
    while ($c = $desc->fetch_assoc()) {
        if (strpos($c['Extra'],'auto_increment') !== false) continue;
        $name = $c['Field'];
        $type = $c['Type'];
        echo '<label class="small">'.htmlspecialchars($name).'</label>';
        if (preg_match('/^enum\((.*)\)$/i',$type,$m)) {
            $opts = str_getcsv($m[1],',',"'"); echo '<select name="col['.htmlspecialchars($name).']">'; echo '<option value="">--</option>';
            foreach ($opts as $op) echo '<option value="'.htmlspecialchars($op).'">'.htmlspecialchars($op).'</option>';
            echo '</select>';
        } elseif (stripos($type,'date') !== false) {
            echo '<input type="date" name="col['.htmlspecialchars($name).']">';
        } elseif (preg_match('/int|tinyint|smallint|mediumint|bigint/i',$type)) {
            echo '<input type="number" name="col['.htmlspecialchars($name).']">';
        } elseif (preg_match('/text|blob|longtext/i',$type)) {
            echo '<textarea name="col['.htmlspecialchars($name).']" rows="3"></textarea>';
        } else {
            echo '<input type="text" name="col['.htmlspecialchars($name).']">';
        }
    }
    echo '<div style="margin-top:8px;"><button type="submit">Insertar</button></div>';
    echo '</form>';
    exit();
}

// otherwise show table view
$res = $mysqli->query("SELECT * FROM `{$table}` LIMIT 500");
echo '<h3>Tabla: '.htmlspecialchars($table).'</h3>';
echo '<div class="table-wrap"><table><thead><tr>';
$cols = [];
if ($res) {
    $first = $res->fetch_assoc();
    if ($first) {
        $cols = array_keys($first);
        foreach ($cols as $c) echo '<th>'.htmlspecialchars($c).'</th>';
        echo '<th>Acciones</th></tr></thead><tbody>';
        // first row
        echo '<tr>';
        foreach ($first as $val) echo '<td>'.htmlspecialchars((string)$val).'</td>';
        // determine pk field for delete link
        $pkField = '';
        $desc = $mysqli->query("DESCRIBE `{$table}`");
        while ($d = $desc->fetch_assoc()) { if ($d['Key'] === 'PRI') { $pkField = $d['Field']; break; } }
        $pkVal = $first[$pkField] ?? '';
        echo '<td class="actions">';
        if ($pkField) echo '<a class="del" href="admin.php?table='.urlencode($table).'&delete='.urlencode($pkVal).'">Borrar</a>';
        echo '</td></tr>';
        // rest rows
        while ($row = $res->fetch_assoc()) {
            echo '<tr>';
            foreach ($cols as $c) echo '<td>'.htmlspecialchars((string)($row[$c] ?? '')).'</td>';
            $pkVal = $row[$pkField] ?? '';
            echo '<td class="actions">';
            if ($pkField) echo '<a class="del" href="admin.php?table='.urlencode($table).'&delete='.urlencode($pkVal).'">Borrar</a>';
            echo '</td></tr>';
        }
        echo '</tbody></table></div>';
    } else {
        echo '<p>No hay registros en la tabla.</p>';
    }
} else {
    echo '<p>Error al consultar: '.htmlspecialchars($mysqli->error).'</p>';
}
?>