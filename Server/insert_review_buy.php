<?php  
$con=mysqli_connect("127.0.0.1","root","zmzm1004","donate");  
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con)){  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}

 $id = urldecode($_POST['id']); 
 $writeDate = urldecode($_POST['writeDate']); 
 $score = urldecode($_POST['score']); 
 $contents = urldecode($_POST['contents']);
 $num = urldecode($_POST['boardNumber']);

$result = mysqli_query($con,"INSERT INTO review_buy (id,writeDate,score,contents,num) VALUES ('$id', '$writeDate', '$score', '$contents', '$num')");

  if($result){
    echo 'success';
  }
  else{
    echo 'failure';
  }
  

  
mysqli_close($con);
?>