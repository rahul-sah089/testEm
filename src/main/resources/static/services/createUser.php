<?php 
require_once("config.php");

$firstname	= $_REQUEST["firstname"];
$lastname	= $_REQUEST["lastname"];
$type		= $_REQUEST["type"];
$active		= $_REQUEST["active"];

if($firstname == ""  || $lastname== "") {
	$resp = array("error" => "1" ,"errorMsg" => "Invalid Inputs","msg" => "Invalid Inputs");
	echo json_encode( $resp );
	exit;
}else{
	$insert = "INSERT INTO users VALUES ('', '".$firstname."', '".$lastname."','".$type."', '".$active."', '".date("Y-m-d H:i:s")."')";
	mysqli_query($conn, $insert);
	$resp = array("success" => "1", "msg" => "User Information Added Successfully.");
	echo json_encode( $resp );
	exit;
}
?>