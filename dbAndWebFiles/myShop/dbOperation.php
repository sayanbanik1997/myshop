<?php
    $conn = new mysqli("localhost", "root", "", "myShop1.0");

    if(isset($_POST['qry'])){

        //$rslt = mysqli_query($conn, $_POST['qry']);
        $rslt = $conn -> query($_POST['qry']);
        $response = array();
        if(gettype($rslt)=="boolean"){
           // if( $rslt){
                
                //$conn -> query($_POST['qry']);
                
                // Print auto-generated id
                //echo "New record has id: " . $mysqli -> insert_id;
                //$lastId = null;

                //$lastRowRslt = mysqli_fetch_assoc(mysqli_query($conn, "select * from ". $_POST['tblName'] ." where id=". $lastId)); //
                //$lastRowObj = json_decode(json_encode($lastRowRslt));

                $lastId=-1;
                $jsonObj = json_decode("{}");
                if( $conn -> insert_id==null){
                   $jsonObj -> updOrDelBool = $rslt;
                }else{
                    $lastId =  ($conn -> insert_id);
                }

                //$conn -> close();

                $jsonObj -> lastId = $lastId;
                
                echo  json_encode($jsonObj);
            //}
        }else{
            while($row = mysqli_fetch_assoc($rslt)){
                array_push($response, $row);
            }
            echo json_encode($response);
        }
    }else{
        echo "no query";
    }
    
?>