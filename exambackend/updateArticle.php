<?php 
include 'connect.php';
header('Content-Type: application/json');
if ($_SERVER['REQUEST_METHOD']=='POST') {
 $id = $_POST['id'];
 $title = $_POST['title'];
 $content = $_POST['content'];

 $sql = "UPDATE article SET article_title = '$title', article_content = '$content' WHERE article_id= $id";
 $response = mysqli_query($conn, $sql);
 $result = array();
 if($response){
    $result['message'] = 'success';
    echo json_encode($result);
    
 }
 else{
    $result['message'] = 'failed';
    echo json_encode($result);
 }
 mysqli_close($conn);
}
?>