<?php  
$con=mysqli_connect("127.0.0.1","root","zmzm1004","donate");  
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
$id = $_POST['id'];  
$password = $_POST['password'];
$token =$_POST['token'];



$result = mysqli_query($con,"SELECT * FROM userjoin WHERE id='$id' AND password ='$password';");  

$check = mysqli_fetch_array($result);
		
if(isset($check)){
	echo "success";
	$result2 = mysqli_query($con, "UPDATE userjoin SET token='$token' WHERE id='$id'");
}
else{
	echo "Invalid Username or Password";
}
  
mysqli_close($con);  
?>
