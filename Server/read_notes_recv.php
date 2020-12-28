<?php
$con=mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

$id_recv=$_POST['id_recv'];

$res = mysqli_query($con, "SELECT * FROM notes WHERE id_recv='$id_recv' AND del_recv='N'");

$result = array();

while($row = mysqli_fetch_array($res)){
   array_push($result,  
   array('num'=>$row[0],'id_recv'=>$row[1],'id_send'=>$row[2],'contents'=>$row[3],'date_send'=>$row[4],'read_recv'=>$row[5],'del_recv'=>$row[6],'del_send'=>$row[7]
   ));
}

if($result){
   //echo json_encode(array("result"=>$result));
   $encode = json_encode(array("messageSendResult"=>$result));
   print(to_han($encode)); 
   mysqli_close($con);
 }
else{  
   echo '받은 쪽지가 없습니다.';
}  

?>