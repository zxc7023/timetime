<?php
$con=mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$num=$_POST['num'];
$del_send=$_POST['del_send']; 


if($del_send == 'Y'){ // 받은사람이 쪽지를 삭제했을 때
  $result = mysqli_query($con, "DELETE FROM notes WHERE num='$num'");
  echo '삭제되었습니다.';
}
else{ // 쪽지를 삭제하지 않았을 때
  $result = mysqli_query($con, "UPDATE notes SET del_recv='Y' WHERE num='$num'");
  echo 'y로 바꿨습니다.';
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