<?php  
$con=mysqli_connect("127.0.0.1","root","zmzm1004","donate");  
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
} 
$id = $_POST['id'];  
$password = $_POST['password'];  
$name = $_POST['name']; 
$phone = $_POST['phone']; 
$birth = $_POST['birth']; 
$gender = $_POST['gender'];
$major = $_POST['major'];
$subMajor = $_POST['subMajor'];
$token= $_POST["token"];



$result = mysqli_query($con,"INSERT into userjoin (id,password,name,phone,birth,gender,major,subMajor,token)
    values ('$id','$password','$name','$phone','$birth','$gender','$major','$subMajor','$token');");  

  if($result){  
    echo 'success';  
  }  
  else{  
    echo 'failure';  
  }  
  
  
mysqli_close($con);  
?>
