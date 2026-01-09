<?php
require_once 'config.php';
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['registrar'])) {
    $nombre = trim($_POST['nombre']);
    $correo = trim($_POST['correo']);
    $pass = $_POST['contrasena'];
    if ($nombre === '' || $correo === '' || $pass === '') {
        die('Completa todos los campos. <a href="registro.html">Volver</a>');
    }
    // verificar si existe
    $stmt = $mysqli->prepare("SELECT id FROM usuarios WHERE correo = ?");
    $stmt->bind_param('s', $correo);
    $stmt->execute();
    $stmt->store_result();
    if ($stmt->num_rows > 0) {
        die('El correo ya está registrado. <a href="index.html">Iniciar sesión</a>');
    }
    $stmt->close();
    $hash = password_hash($pass, PASSWORD_DEFAULT);
    $ins = $mysqli->prepare("INSERT INTO usuarios (nombre, correo, contrasena) VALUES (?, ?, ?)"); 
    $ins->bind_param('sss', $nombre, $correo, $hash);
    if ($ins->execute()) {
        header('Location: index.html');
        exit();
    } else {
        die('Error al registrar: ' . $mysqli->error);
    }
}
?>