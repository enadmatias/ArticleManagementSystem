<?php 
include 'connect.php';
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD']=='POST') {
   $Fullname = $_POST['Fname'];
   $EmailAdd = $_POST['EmailAdd'];
   $Username = $_POST['Username'];
   $Password = md5($_POST['Password']);

   $sql = "INSERT INTO user (user_name, user_email, user_username, user_password) values ('$Fullname', '$EmailAdd', '$Username', '$Password')";
   $response = mysqli_query($conn, $sql);
   $result = array();


   if($response){
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