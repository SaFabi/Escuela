<?php
require_once(dirname(__FILE__)."/../../core/utils.php");
require_once(dirname(__FILE__)."/../../library/nusoap/lib/nusoap.php");
$db = connectDB();
$nombre = $_REQUEST['nombre'];
$pass = $_REQUEST['pass'];
$nick = $_REQUEST['nick'];

//Encriptamos el pass
$cliente = new nusoap_client("http://atc.mx/android/libraryPHP/encriptado/seguridad.php?wsdl",true);
$parametros = array('password'=>$pass);
$passEncriptado = $cliente->call('encriptar',$parametros);
//Armamos la consulta
$consulta = "INSERT INTO usuario(nombre,nick,pass,categoria_id)VALUES('".$nombre."','".$nick."','".$passEncriptado."',1)";

$resultado = $db->query($consulta);

if ($resultado) {
echo "Se inserto correctamente";
}

 ?>
