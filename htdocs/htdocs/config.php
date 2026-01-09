<?php
// Ajusta estas constantes según tu hosting (InfinityFree da los datos)
define('DB_HOST','sql100.infinityfree.com');
define('DB_USER','if0_40396765');
define('DB_PASS','hBFcf3xi0q');
define('DB_NAME','if0_40396765_zoo_db');

$mysqli = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
if ($mysqli->connect_errno) {
    die("Fallo conexión MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error);
}
$mysqli->set_charset('utf8mb4');
?>