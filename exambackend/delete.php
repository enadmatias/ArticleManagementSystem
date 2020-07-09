<?php
include 'connect.php';
header('Content-Type: application/json');
if ($_SERVER['REQUEST_METHOD']=='POST') {
    $id = $_POST['id'];

$sql = "DELETE  FROM article WHERE article_id = $id";
    $response = mysqli_query($conn, $sql);
    $result = array();
    if($response){
        $result['message'] = "success";
        echo json_encode($result);

         mysqli_close($conn);

    }
    else{
        $result['message'] = "failed";
        echo json_encode($result);

         mysqli_close($conn);
    }
}


?>