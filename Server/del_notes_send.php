<?php
$con=mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$num=$_POST['num'];
$del_recv=$_POST['del_recv'];

if($del_recv == 'Y'){ // 받은사람이 쪽지를 삭제했을 때
  $result = mysqli_query($con, "DELETE FROM notes WHERE num='$num'");
}
else{ // 쪽지를 삭제하지 않았을 때
  $result = mysqli_query($con, "UPDATE notes SET del_send='Y' WHERE num='$num'");
}

  if($result){  
     echo 'success';
    $result=mysqli_query($con,"SELECT TABLE_ROWS FROM information_schema.tables WHERE table_name='notes'");
     $total_rows = mysqli_num_rows($result);
     mysqli_query($con,"ALTER TABLE notes AUTO_INCREMENT=1");
     mysqli_query($con,"SET @COUNT = 0");
     mysqli_query($con,"UPDATE notes SET notes.num = @COUNT:=@COUNT+1");
     mysqli_query($con,"ALTER TABLE notes AUTO_INCREMENT='$total_rows'");
  }  
  else{  
    echo 'failure';  
  }  
  
  
mysqli_close($con);  
?>