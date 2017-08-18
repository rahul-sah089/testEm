<?php 
require_once("config.php");

$select = "SELECT * FROM users order by created_at";
$rs = mysqli_query($conn, $select);
$rowcount=mysqli_num_rows($rs);
while($list = mysqli_fetch_assoc($rs)){
	$data[] = array('id'=> $list['id'],'firstname'=> $list['firstname'], 'lastname'=> $list['lastname'], 'type'=> $list['type'], 'active'=> $list['active'] , 'created_at'=> $list['created_at']);
}
//$resp = array("data" => array("success" => "1" , "rowCount"=> $rowcount, "data" => $data) );
$resp = array("success" => "1" , "rowCount"=> $rowcount, "userList" => $data);
echo json_encode( $resp );



exit;
?>