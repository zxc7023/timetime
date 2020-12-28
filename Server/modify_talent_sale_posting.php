<?php

$con = mysqli_connect("127.0.0.1","root","zmzm1004","donate");

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){  
   echo "Failed to connect to MySQL: " . mysql_connect_error();
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

$id = urldecode($_POST['id']);
$title = urldecode($_POST['title']);
$dayResult = urldecode($_POST['dayResult']);
$writeDate = urldecode($_POST['writeDate']);
$type = urldecode($_POST['type']);
$startHour = urldecode($_POST['startHour']);
$endHour = urldecode($_POST['endHour']);
$category = urldecode($_POST['category']);
$talent = urldecode($_POST['talent']);
$contents = urldecode($_POST['contents']);
$boardNumber = urldecode($_POST['boardNumber']);

$file_path = "uploads/";
//.basename 파일명+확장자명 반환
//$_FILES['uploaded_file']['name'] 클라이언트에서 올린 원래 이름.
$file_path = $file_path . basename( $_FILES['uploaded_file']['name']); //file_path는 uploads/파일명.확장자 상태로 됨
$str=strcmp($file_path,"uploads/");
if($str)
{
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) 
    { 
      $result = mysqli_query($con, "UPDATE talent_sale_posting SET title='$title', dayResult='$dayResult', gender=(select gender from userjoin where userjoin.id='$id'), writeDate='$writeDate', type='$type', major=(select major from userjoin where userjoin.id='$id'), subMajor=(select subMajor from userjoin WHERE userjoin.id='$id'), startHour='$startHour', endHour='$endHour', category='$category', talent='$talent', contents='$contents',filePath='$file_path' WHERE boardNumber='$boardNumber'");
    echo "success";
  }
  else
  {
    echo "moveFail";
  }
}
else
{
    $result = mysqli_query($con, "UPDATE talent_sale_posting SET title='$title' , dayResult='$dayResult' , gender=(select gender from userjoin where userjoin.id='$id') , writeDate='$writeDate' , type='$type' , major=(select major from userjoin where userjoin.id='$id') , subMajor=(select subMajor from userjoin WHERE userjoin.id='$id') , startHour='$startHour' ,endHour='$endHour' , category='$category' , talent='$talent' , contents='$contents' WHERE boardNumber='$boardNumber'");
        echo "success";

}

    
    
?>