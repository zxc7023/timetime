<?php
include_once 'config.php';
$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con)){
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

function han($s){ return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

function send_notification ($tokens, $message)
  {
    $url = 'https://fcm.googleapis.com/fcm/send';
    $fields = array(
       'registration_ids' => $tokens,
       'data' => $message
      );

    $headers = array(
      'Authorization:key =' . GOOGLE_API_KEY,
      'Content-Type: application/json'
      );

     $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);  
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);           
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;
  }

$id_recv = $_POST['id_recv'];
$id_send = $_POST['id_send'];
$contents = $_POST['msg_contents'];
$date_send = $_POST['msg_writeDate'];

$result = mysqli_query($con,
   "INSERT INTO notes (num, id_recv, id_send, contents,date_send, read_recv, del_recv, del_send)
   VALUES ('','$id_recv','$id_send','$contents','$date_send','읽지않음','N', 'N')");

  if($result){
    $res=mysqli_query($con,"SELECT token from userjoin where id='$id_recv'");
    $token = array();

    if(mysqli_num_rows($res)>0){
      while($row=mysqli_fetch_assoc($res)){
        $tokens[]=$row['token'];
      }
    }
    $message=array("message"=>$contents,"id_recv"=>$id_recv,"token"=>$token);

    $message_status = send_notification($tokens, $message);
    //echo $message_status;

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