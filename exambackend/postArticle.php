<?php 
include 'connect.php';
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD']=='POST') {
    $articleTitle = $_POST['articleTitle'];
    $articleContent = $_POST['articleContent'];
    $thumbnailName = $_POST['thumbnailName'];
    $userId = $_POST['userId'];
    $thumbnailPath = $_POST['Thumbnail'];

    $path = "uploads/$thumbnailName.png";
    $actualpath = "http://10.0.2.2:8080/exambackend/$path";
     $dateToday = date("Y-m-d");

    $sql = "INSERT INTO article (article_title, article_content, article_date, user_id, article_thumbnail) values ('$articleTitle', '$articleContent', '$dateToday','$userId','$actualpath')";
    $response = mysqli_query($conn, $sql);
    $result = array();
    if($response){
        file_put_contents($path,base64_decode($thumbnailPath));
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