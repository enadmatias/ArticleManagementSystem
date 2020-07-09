<?php 
include 'connect.php';
header('Content-Type: application/json');


    $sql = "SELECT * FROM article";
    $response = mysqli_query($conn, $sql);
    $result = array();
    $result['articlesData'] = array();
    while($row = mysqli_fetch_assoc($response)){
        $index['articleId'] = $row['article_id'];
        $index['articleTitle'] = $row['article_title'];
        $index['articleContent'] = $row['article_content'];
        $index['articleDate'] = $row['article_date'];
        $index['userId'] = $row['user_id'];
        $index['thumbnail'] = $row['article_thumbnail'];
        array_push($result['articlesData'], $index);
       
    }
    echo json_encode($result);



?>