<?php

$con = mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){  
   echo "Failed to connect to MySQL: " . mysql_connect_error();
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

$id = $_POST['id'];

$res = mysqli_query($con, "SELECT * FROM userjoin WHERE id='$id'");

$result = array();

while($row = mysqli_fetch_array($res)){
   array_push($result,  
   array('id'=>$row[0],'password'=>$row[1],'name'=>$row[2],'phone'=>$row[3],'birth'=>$row[4],'gender'=>$row[5],'major'=>$row[6],
    'subMajor'=>$row[7]
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