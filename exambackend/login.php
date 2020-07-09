<?php 
include 'connect.php';
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD']=='POST') {
     $name = $_POST['uname'];
     $pwd = md5($_POST['upass']);
  
     $sql = "SELECT * FROM user WHERE user_username = '$name' && user_password = '$pwd' ";
     $response = mysqli_query($conn, $sql);
     $result = array();
    
  
  
      if($row = mysqli_fetch_assoc($response)){
          $result['userId'] = $row['id'];
          $result['message'] = "success";
          echo json_encode($result);        
      }
      else{
          $result['message'] = "failed";
          echo json_encode($result);
      }
  
      mysqli_close($conn);
  }
?>