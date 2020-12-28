<?php

$con = mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){
   echo "Failed to connect to MySQL: " . mysql_connect_error();
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

$res = mysqli_query($con, "SELECT talent_sale_posting.boardNumber,talent_sale_posting.id,talent_sale_posting.title,talent_sale_posting.dayResult,talent_sale_posting.gender,talent_sale_posting.writeDate,talent_sale_posting.type,talent_sale_posting.major,talent_sale_posting.subMajor,talent_sale_posting.startHour,talent_sale_posting.endHour,talent_sale_posting.category,talent_sale_posting.talent,talent_sale_posting.contents,filePath,(SELECT ifnull(avg(review.score),'0')),(SELECT count(*) FROM review WHERE review.num=talent_sale_posting.boardNumber) FROM talent_sale_posting LEFT JOIN review ON talent_sale_posting.boardNumber = review.num GROUP BY talent_sale_posting.boardNumber ORDER BY talent_sale_posting.writeDate DESC LIMIT 5");

$result = array();

while($row = mysqli_fetch_array($res)){
   array_push($result,
   array('boardNumber'=>$row[0],'id'=>$row[1],'title'=>$row[2], 'dayResult'=>$row[3], 'gender'=>$row[4], 'writeDate'=>$row[5],'type'=>$row[6],'major'=>$row[7],'subMajor'=>$row[8],'startHour'=>$row[9],'endHour'=>$row[10],'category'=>$row[11],'talent'=>$row[12],'contents'=>$row[13],'filePath'=>$row[14],'score'=>$row[15],'review'=>$row[16]
   ));
}

if($result){
   //echo json_encode(array("result"=>$result));
   $encode = json_encode(array("result"=>$result));
   print(to_han($encode));
   mysqli_close($con);
}
else{
   echo '게시글이 없습니다.';
}

?>