<?php
session_start();
require_once 'config.php';

if (isset($_POST['login'])) {
    $correo = trim($_POST['correo']);
    $pass = $_POST['contrasena'];

    $stmt = $mysqli->prepare("SELECT id, nombre, contrasena FROM usuarios WHERE correo = ? LIMIT 1");
    $stmt->bind_param('s', $correo);
    $stmt->execute();
    $stmt->store_result();
    if ($stmt->num_rows === 1) {
        $stmt->bind_result($id, $nombre, $hash);
        $stmt->fetch();
        if (password_verify($pass, $hash)) {
            $_SESSION['usuario_id'] = $id;
            $_SESSION['usuario_nombre'] = $nombre;
            header('Location: dashboard.php');
            exit();
        } else {
            $error = 'Contraseña incorrecta.';
        }
    } else {
        $error = 'Usuario no encontrado.';
    }
    $stmt->close();
}

// si venimos directo mostramos el html
if (!empty($error)) {
    echo '<div style="font-family:Arial;padding:20px;color:#b00020;">'.htmlspecialchars($error).'<br><a href="index.html">Volver</a></div>';
}
?>