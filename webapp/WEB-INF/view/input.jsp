<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>백종원 유튜브 목록</title>
<style>
.wrap {
	width: 1000px;
	margin: auto;
}

h1, .title {
	text-align: center;
}

.content {
	margin: 20px;
}

.ingredient {
	background: #fff9f5;
}
</style>
</head>
<body>
	<h1>백종원 유튜브 목록</h1>
	<div class="wrap">
		<div class="contents"></div>
		<!--//contents-->
	</div>
	<!--//wrap-->
	
	<script src="js/jquery.js"></script>
	<script>
    //유저 키
    const user_key = "AIzaSyD-swwnkV_MtoxfIocbo9mYEDwlBlemUZo";
    //재생목록 ID
    const user_palylistId="UUyn-K7rZLXjGl7VXGweIlcA";
    //최대 결과값 (50이 최대)
    const max_results=50;
    //레시피 정보가 들어갈 영역 선언
    const $contents=$(".contents");
    //다음 페이지로 들어가기 위한 token
    let token="";

    //유튜브 목록 불러오기
    function getYoutube(token) {
        $.ajax({
            //맵핑 url 설정
            url:"https://www.googleapis.com/youtube/v3/playlistItems",
            //형식
            dataType:"json",
            //전송 방식 (GET/POST)
            type:"GET",
            //파라미터 넣기
            data:{part:'snippet',playlistId:user_palylistId,key:user_key,maxResults:max_results,pageToken:token},
            //로딩 성공
            success:function(json){
            	
            	const items=json.items;
                token=json.nextPageToken;
                
                // 각 제목과 주소 붙이기
                for(idx in items){
                    let $content=$("<div class='content'>");
                    let $ingredient=null;
                    //각 영상의 id
                    const $id=items[idx].snippet.resourceId.videoId;
                    //각 영상의 제목
                    const $cooking=items[idx].snippet.title;
                    //각 영상의 썸네일
                    const $thumbnail=items[idx].snippet.thumbnails.medium.url;                    
                    //각 영상의 uri(id)
                    const $url="https://www.youtube.com/watch?v="+$id;
                    //각 영상의 등록일
                    const $date=items[idx].snippet.publishedAt;
                    const $regdate =$date.substring(0, 10)+" "+$date.substring(11,19);
                    //영상 uri로 연결되는 제목
                    const $title=$("<a href='"+$url+"' class='title'>").text($cooking);
                    //youtube 영상 본문 내용
                    const ing=items[idx].snippet.description;
                    
                    //본문 내용 중 [재료] 부분을 기준으로 문자열 자르기
                    const ingredientArr=ing.split(/\[재료\][ A-Za-z0-9ㄱ-힣]*\n|\[ 재료 \][ A-Za-z0-9ㄱ-힣]*\n/);
                    //[재료] 뒷 부분 문자열 가져오기
                    let ingredients=ingredientArr[1]+"";
                    //~하는 법 이라는 글자가 나오는지 확인
                    const match=ingredients.match(/\[[가-힣|\s]+법/);
                    //~하는 법이라는 글자가 있다면
                    if(match!=null){
	                    //매칭이 되는 부분을 앞부분만 남기기
                        $ingredient=ingredients.substring(0,ingredients.indexOf(match[0]));
                    }
                    
                    //div 요소를 추가한 재료 텍스트
                    const $ingredients=$("<div class='ingredient'>").text($ingredient);
                    
                    //[재료] 뒷 부분 문자열이 존재한다면 (재료 정보가 표기되어 있다면)
                    if(ingredients!="undefined"){
                    	//재목과 재료를 content div에 붙이고, 이를 contents div에 붙이기
                    	$content.append($title,$ingredients).appendTo($contents);
                    	//id, cooking(제목), thumbnail(썸네일 주소), ingredientStr(재료 문자열)을 넘기기
                    	$.ajax({
                    		url:"/ajaxInput",
                    		type:"POST",
                    		data:{id: $id, cooking:$cooking, thumbnail:$thumbnail, ingredientStr:$ingredient, regdate:$regdate},
                    		success:function(){
                    			//alert("성공!");
                    		},
                    		error:function(){
                    			//alert("실패!");
                    		}
                    	})//ajax end
                    }//if end
                }//for end
                
                //nextPageToken가 undefined이 아니라면 재귀(다음 페이지 호출)
                if (typeof token!=="undefined"){
                    getYoutube(token);
                }
            },
            //로딩 실패시
            error:function(){
                alert("서버점검중!")
            }
        })//ajax end
    }//getYoutube() end

    getYoutube();


</script>
</body>
</html>