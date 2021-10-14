<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>요리재료 검색</title>
<c:import url="/WEB-INF/view/template/link.jsp" />
<link rel="stylesheet" href="/css/header.css" />
<link rel="stylesheet" href="/css/footer.css" />
<style>

#contents {
	padding-top : 100px;
	width: 1100px;
	margin: auto;
}


.recipe {
	cursor: pointer;
	min-height: 180px;
	margin: 20px 0;
	clear: both;
	transition: .2s ease;
	border-bottom: #f0f0f0 2px solid;
	padding: 10px 0;
}

.recipe:hover {
	transform: scale(1.02);
	background: #fff4e8;
}

.recipe:after {
	content: "";
	display: block;
	clear: both;
}

.recipe_img {
	float: left;
	margin: 10px;
	user-select: none;
}

.recipe_content {
	width: 760px;
	float: left;
}

.recipe_title {
	color: #000;
	margin: 5px 10px;
	font-size: 20px;
}

.recipe_detail {
	float: left;
	width: 233px;
	margin: 10px;
}

.recipe_detail.one_detail {
	width: 760px;
}

.recipe_detail.two_detail {
	width: 360px;
}

.recipe_subtitle {
	margin-left: 5px;
	color: #424242;
	font-size: 17px;
}

.recipe_subtitle:before {
	content: " 🥗 ";
}

.ings {
	margin-top: 10px;
}

.ing {
	float: left;
	margin: 5px;
	padding: 5px;
	border-radius: 10px;
	min-width: 50px;
	min-height: 30px;
	background: #eaeaea;
	text-align: center;
	line-height: 30px;
	user-select: none;
}

.ing.check {
	background: #ef780d;
}

.line {
	float: left;
	width: 780px;
	border: gray 2px transprent;
}

#loadingImg {
	line-height : 800px;
	text-align:center;
	height: 800px;
	width: 1100px;
}

#loadingImg img{
	width: 200px;
}




.searched_item {
	background: #ef780d;
	color: #fff;
	text-shadow: 1px 1px #606060;
	font-weight: 900;
}
</style>
</head>
<body>
<c:import url="/WEB-INF/view/template/header.jsp"></c:import>

	<div id="contents">
		<ul class="recipes">
			<%--ajax 처리 --%>
		</ul>
		<div id="loadingImg">
			<img src="/img/back_loading.gif"/>
		</div>
	</div>
	<div id="footer"></div>

	<script type="text/template" id="cookingListTmpl">
	<@ _.forEach(cookings,function(cooking) { @>
        <li data-id="<@=cooking.id@>" class="recipe">
            <div class="recipe_img">
                <img src="<@=cooking.thumbnail@>" alt=""/>
            </div>
            <div class="recipe_content">
                <h3 class="recipe_title"><@=cooking.cooking@></h3>
				<@_.forEachRight(cooking.subtitles,function(subtitle,idx){@>
					
					<@ if((cooking.subtitles.length-idx)%3==1){  @>
               			<div class="line"></div>
					<@ }@>
                	<div class="recipe_detail">
                    	<h4 class="recipe_subtitle"><@=subtitle.title@></h4>
                    	<ul class="ings">
							<@_.forEach(subtitle.ingredients,function(ingredient){@>
                       			 <li class="ing"><@=ingredient.ingredient@></li>
                    		<@ })@> 
                    	</ul>
               		</div>
				<@ })@> 
            </div>
        </li>
	<@ })@> 
	</script>
	
	<script src="/js/jquery.js"></script>
	<script src="/js/back-search.js"></script>
	<script src="/js/lodash-min.js"></script>
	<script>
	//언더스코어(lodash)는 기본적으로 %을 사용하는데 %는  jsp에서 사용할 수 없기 때문에 % -> @ 로변경하는 코드
	_.templateSettings.interpolate= /\<\@\=(.+?)\@\>/gim;
	_.templateSettings.evaluate = /\<\@([\s\S]+?)\@\>/gim;
	_.templateSettings.escape = /\<\@\-(.+?)\@\>/gim;	

    //21-07-21 18:40 변수 선언
	//레시피 내용
    const $content=$(".recipe_content");
    //레시피 템플렛
    const $cookingListTmpl= _.template($("#cookingListTmpl").html());
    //전체 레시피를 담고 있는 ul
    const $recipes=$(".recipes");
    //로딩 이미지 영역
    const $loadingImg=$("#loadingImg");
    //검색 키워드 배열
    const itemList="${ingredient}".split(" ");
    //기본 페이지
    let page=1;
    
    //21-07-27 17:38 ajax로딩 완료시 부제목 갯수에 따른 클래스 붙이기 실행
    $(document).ajaxComplete(function(){
    	countSubtitle();
   		searchIngredient();
    });
    
    //21-07-21 18:55 부제목의 갯수에 따른 클래스 붙이기
    function countSubtitle(){
    	$(".recipe_content").each(function(){
	  	  	//21-07-21 18:58 부제목의 갯수가 1개면 one_detail 클래스 붙이기
	    	if($(this).children('.recipe_detail').length==1){
	    		$(this).children('.recipe_detail').addClass('one_detail')
	  	  	//21-07-21 18:58 부제목의 갯수가 2개면 two_detail 클래스 붙이기
	    	}else if($(this).children('.recipe_detail').length==2){
	    		$(this).children('.recipe_detail').addClass('two_detail')
	    	}
	    })
    }
    
    //21-07-21 20:21 검색 키워드와 맞는 레시피일시 클래스 추가
    function searchIngredient(){
    	$(".ing").each(function(){
    		for(idx in itemList){
    			if($(this).text()==itemList[idx]){
    				$(this).addClass("searched_item");
    			}
    		}
    	})
    }
    	
    //21-07-28 03:29 맨 처음 페이지 로딩시 요리 목록 가져오기
    loadRecipe();
    
    //21-07-26 01:24 ajax로 요리 레시피 가져오기
    function loadRecipe(){
		$.ajax({
            //맵핑 url 설정
            url:"/loadRecipe",
            //전송 방식 (GET/POST)
            type:"GET",
            //파라미터 넣기
            data:{pageNo: page
            	<c:if test="${ingredient!=null}">
            	, ingredientsStr: '${ingredient}'
            	</c:if>
            	},
            //로딩 성공
            success:function(json){
            	//가져올 레시피 목록이 있다면
            	if(json.cookings.length!=0){
	            	//로딩 이미지 삭제
	            	$loadingImg.remove();
	            	//페이지 증가
	            	page++;
	  	         	//리스트 불러오기
	           		$recipes.append($cookingListTmpl({cookings : json.cookings}));
            	//가져올 레시피 목록이 없다면
            	}else{
            		//로딩 이미지를 no_recipe로 변경
            		//$("#loadingImg img").attr("src","/img/no_recipe.jpg");
            	}
            },
            //로딩 실패시
            error:function(){
            }
   		})//ajax end
	}
    
    
    //21-07-27 17:01 스크롤시 추가 요리 정보 로딩
    $(window).on("scroll", function(){
    	let scrollT = $(this).scrollTop(); //스크롤바의 상단위치
    	let scrollH = $(this).height(); //스크롤바를 갖는 div의 높이
    	let contentsH = $('#contents').height(); //문서 전체 내용을 갖는 div의 높이
    	if(scrollT + scrollH +1 >= contentsH){
    		loadRecipe();
    	}
    });
		
    //21-07-28 18:13 새 창으로 레시피 영상 띄우기
	$(".recipes").on("click","li",function(){
		const id = $(this).data("id");
		window.open("https://www.youtube.com/watch?v="+id);
	})
	
	/*21-07-22 18:40 접시에 재료 등록 함수 선언 */
	function serving(target){
		tmpStr="";
		$ingList.empty();
	    ingSet.add(target);
	    for(const ing of ingSet){
	        $("<li class='checked_ingredient'>").text(ing).appendTo($ingList);
	        tmpStr+=ing+" ";
	    }
	    tmpStr=tmpStr.substring(0,tmpStr.length-1);
	    $submitIng.val(tmpStr);
    };
    
    
    



</script>
</body>
</html>