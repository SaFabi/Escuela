<?php
require_once(dirname(__FILE__)."/../../core/utils.php");
$clave = $_REQUEST['clave'];
$nombre = $_REQUEST['nombre'];

$db = connectDB();

//Armamos la consulta
$consulta = "INSERT INTO materia(clave,nombre)VALUES('".$clave."','".$nombre."')";

$resultado = $db->query($consulta);

if ($resultado) {
echo "Se inserto correctamente";
}

 ?>
