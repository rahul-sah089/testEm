<?php 
error_reporting(0);
$conn = mysqli_connect('localhost','root',''); 
if (!$conn) { 
	die('Could not connect to MySQL: ' . mysqli_error()); 
} 
mysqli_select_db( $conn, 'angularjs') or die('Could not select database.');
//mysqli_close($conn); 
?> 