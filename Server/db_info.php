<?php

	// 데이터베이스
	$db_host="127.0.0.1"; // 서버 주소
	$db_user="root"; // 아이디
	$db_passwd="zmzm1004"; // 비밀번호
	$db_name="donate"; // DB명

	// 사용하는 db를 세팅
	$con = mysqli_connect($db_host,$db_user,$db_passwd);
	
	//캐릭터셋 지정
	mysqli_set_charset($con,"utf8");  

?>