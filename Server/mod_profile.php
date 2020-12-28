<?php

$con = mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){  
   echo "Failed to connect to MySQL: " . mysql_connect_error();
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

$id = $_POST['id'];
$password = $_POST['password'];
$name = $_POST['name'];
$phone = $_POST['phone'];
$birth = $_POST['birth'];
$gender = $_POST['gender'];
$major = $_POST['major'];
$subMajor = $_POST['subMajor'];

$result = mysqli_query($con, 
   "UPDATE userjoin
   SET password='$password' , name='$name' , phone='$phone' , birth='$birth' , gender='$gender' , major='$major' , subMajor='$subMajor'
   WHERE id='$id'");

  if($result){  
       echo 'success';  
  }  
  else{  
    echo 'failure';  
  } 

?>