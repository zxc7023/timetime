<?php

$con = mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){  
   echo "Failed to connect to MySQL: " . mysql_connect_error();
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

$gender = $_POST['gender'];
$type = $_POST['type'];
$dayResult = $_POST['dayResult'];
$startHour = $_POST['startHour'];
$startHour2 = $_POST['startHour'];
$endHour = $_POST['endHour'];
$talent1 = $_POST['talent1'];
$talent2 = $_POST['talent2'];
$talent3 = $_POST['talent3'];

if ($gender=='무관'){ // 성별이 무관일 때 
   $gender='gender IS NOT NULL';
}
else{
   $gender="gender = '$gender'";
}

if ($dayResult=='평일'){ // 평일일 때 
   $dayResult="dayResult = '62'";
}
else if($dayResult=='주말'){ // 주말일 때
   $dayResult="dayResult = '65'";
}
else if($dayResult=='무관'){
   $dayResult="dayResult IS NOT NULL";
}
else{
   $dayResult="dayResult = '$dayResult'";
}

if($startHour=='무관'&&$endHour=='무관'){
   $startHour="startHour IS NOT NULL";
   $endHour="endHour IS NOT NULL";
}
else{
   $startHour="startHour >= '$startHour'";
   $endHour="endHour >= '$startHour2' AND endHour <='$endHour'";
}

if ($talent1=='무관'){ // 재능이 무관일 때
   $talent1='talent IS NOT NULL';
   $talent2='talent IS NOT NULL';
   $talent3='talent IS NOT NULL';
}
else{
   $talent1="talent = '$talent1'";
   $talent2="talent = '$talent2'";
   $talent3="talent = '$talent3'";
}

$res = mysqli_query($con, "SELECT * FROM talent_buy_posting WHERE $gender AND type='$type' AND $dayResult AND $startHour AND $endHour AND ($talent1 OR $talent2 OR $talent3)");

$result = array();

while($row = mysqli_fetch_array($res)){
   array_push($result,  
   array('boardNumber'=>$row[0],'id'=>$row[1],'title'=>$row[2], 'dayResult'=>$row[3], 'gender'=>$row[4], 'writeDate'=>$row[5],'type'=>$row[6],'startHour'=>$row[7],'endHour'=>$row[8],'category'=>$row[9],'talent'=>$row[10],'contents'=>$row[11],'filePath'=>$row[12]
   ));
}

if($result){  
   //echo json_encode(array("result"=>$result));
   $encode = json_encode(array("result"=>$result));
   print(to_han($encode)); 
   mysqli_close($con);
}  
else{  
   echo '해당하는 검색결과가 없습니다.';
}  


?>