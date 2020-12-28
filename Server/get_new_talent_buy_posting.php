<?php

$con = mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){
   echo "Failed to connect to MySQL: " . mysql_connect_error();
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

$res = mysqli_query($con, "SELECT talent_buy_posting.boardNumber,talent_buy_posting.id,talent_buy_posting.title,talent_buy_posting.dayResult,talent_buy_posting.gender,talent_buy_posting.writeDate,talent_buy_posting.type,talent_buy_posting.startHour,talent_buy_posting.endHour,talent_buy_posting.category,talent_buy_posting.talent,talent_buy_posting.contents,filePath,(SELECT ifnull(avg(review_buy.score),'0')),(SELECT count(*) FROM review_buy WHERE review_buy.num=talent_buy_posting.boardNumber) FROM talent_buy_posting LEFT JOIN review_buy ON talent_buy_posting.boardNumber = review_buy.num GROUP BY talent_buy_posting.boardNumber ORDER BY talent_buy_posting.writeDate DESC LIMIT 5");

$result = array();

while($row = mysqli_fetch_array($res)){
   array_push($result,
   array('boardNumber'=>$row[0],'id'=>$row[1],'title'=>$row[2], 'dayResult'=>$row[3], 'gender'=>$row[4], 'writeDate'=>$row[5],'type'=>$row[6],'startHour'=>$row[7],'endHour'=>$row[8],'category'=>$row[9],'talent'=>$row[10],'contents'=>$row[11],'filePath'=>$row[12],'score'=>$row[13],'review'=>$row[14]
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