<?php 
require_once("config.php");
$userId = $_REQUEST["userid"];
$firstname = $_REQUEST["firstname"];
$lastname = $_REQUEST["lastname"];
$type = $_REQUEST["type"];
$active = $_REQUEST["active"];


if(!empty($userId)) {

	$updateUser = "UPDATE users set firstname='".$firstname."', lastname='".$lastname."', type='".$type."', active='".$active."', created_at='".date("Y-m-d H:i:s")."' WHERE id=".$userId;
	$rs = mysqli_query($conn, $updateUser);
	$resp = array("success" => "1", "msg" => "User Information Updated Successfully.");
	echo json_encode( $resp );

} else {
	$resp = array("error" => "1" ,"errorMsg" => "Invalid Inputs", "msg" => "Invalid Inputs");
	echo json_encode( $resp );

}

exit;
?>