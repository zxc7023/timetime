<?php

$con = mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){  
   echo "Failed to connect to MySQL: " . mysql_connect_error();  
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

$id = "tt";

$res = mysqli_query($con, "SELECT * FROM talent_buy_posting WHERE id='$id'");

$result = array();

while($row = mysqli_fetch_array($res)){
   array_push($result,  
   array('boardNumber'=>$row[0],'id'=>$row[1],'title'=>$row[2], 'dayResult'=>$row[3], 'gender'=>$row[4], 'writeDate'=>$row[5],'type'=>$row[6], 'startHour'=>$row[7],'endHour'=>$row[8],'talent'=>$row[10],'contents'=>$row[11],'filePath'=>$row[12]
   ));
}

if($result){
   //echo json_encode(array("result"=>$result));
   $encode = json_encode(array("result"=>$result));
   print(to_han($encode)); 
   mysqli_close($con);
}  
else{  
   echo '작성한 게시글이 없습니다.';
}  

?>