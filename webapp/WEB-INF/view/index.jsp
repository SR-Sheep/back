<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>요리검색</title>
<c:import url="/WEB-INF/view/template/link.jsp"/>
<style>
	#header{
        width: 100%;
        background: #27AE60;
    }
    
    #contents {
		width: 1100px;
		margin: auto;
	}
	
    #logoWrap{
    	margin-top : 50px;
    	text-align: center;
    }
    
    .search_wrap{
    	top : 250px;
    	width:624px;
    	height:400px;
    	margin: 50px auto;
    	z-index: 5;
    }
    
    .search_box{
    	float:left;
        width: 600px;
        /*padding: 10px;*/
        margin :10px auto;
        border-radius: 30px;
        border: #fafafa 2px solid;
        box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
        text-align: center;
    }

    .search_box input{
    	position : relative;
        width: 500px;
        border: transparent 2px solid;
        font-size: 20px;
        padding-top: 15px;
        padding-bottom: 5px;
    }
    .search_box .fa-search{
        position: absolute;
        left : 20px;
        top : 33px;
        font-size: 20px;
        color : #424242;
    }
    .search_box input:focus{
        outline: none;
    }
    .autocomplete{
    	position : relative;
        text-align: left;
        background: #fff;
        border-radius: 0 0 30px 30px;
        padding-bottom: 20px;
        max-height: 280px;
        overflow: auto;
        z-index: 5; 
    }
    .autocomplete>li{
        padding: 20px;
        padding-left: 50px;
        cursor: pointer;
    }

    .autocomplete>li:hover{
        background: #f0f0f0;
    }
    .selected{
    	background: #f0f0f0;
    }
    .last{
    	background: #fff;
        height: 10px;
        padding: 5px;
        cursor: default;
        width: 1px;
        margin-left:50px;
    }
    .autocomplete .last:hover{
    	
    }
 	#dishWrap{
        bottom:0;
        left:0;
        right:0;
        transition: 1s ease;
        animation: upside 2s ease;
    }
    #dishOuter{
        position: relative;
        margin: auto;
        width: 500px;
        height: 250px;
        background: linear-gradient( 45deg, #fafafa, #eaeaea);
        border-radius: 500px 500px 0 0;
        box-shadow: 0 12px 15px 0 rgba(0, 0, 0, 0.24) , 0 17px 50px 0 rgba(0, 0, 0, 0.19);
    }

    #dishInner{
        position: absolute;
        bottom: 0;
        left: 100px;
        width: 300px;
        height: 150px;
        background: linear-gradient( 45deg, #eaeaea, #ffffff);
        border-radius: 500px 500px 0 0;
        box-shadow: 0 6px 20px 0 rgba(0, 0, 0, 0.19) inset,0 0 0 #fafafa;
    }

    @keyframes upside {
        0%{transform: translateY(100px);opacity: 0}
        100%{transform:translateY(0px); opacity: 1 }
    }    
    @keyframes updown {
        0%{transform: translateY(-100px);}
        100%{transform:translateY(0px); }
    }    
    
    .ingredient_list{
		display: block;
		position: absolute;
		bottom: 0;
		left: 50px;
		width: 400px;
		z-index: 1;
		text-align: center;
    }
    .checked_ingredient{
        display: inline-block;
        position: relative;
        font-size: 20px;
        background: #fff4e8;
        padding: 10px;
        margin : 5px;
        border-radius: 20px;
    }
    .checked_ingredient:hover{
        text-decoration: #424242 underline;
        cursor: pointer;
    }
    
    #submitBtn{
    	position : absolute;
    	margin : 0 0 100px -50px;
    	top : 100px;
    	left : 50%;
    	width: 100px;
    	height: 50px;
    	background: orange;
    	color: #fff;
    	border: 0;
    	transition : .2s ease;
    	cursor: pointer;
    }
     #submitBtn:hover{
     	background: #e29303;
     
     }
    
    .hero_wrap{
        width: 1000%;
        position: relative;
        box-sizing: content-box;
    }
    .hero_img{
        float: left;
        display: list-item;
        margin: 10px;
    }
    .hero_img img{
        width: 320px;
        height: 180px;
    }
    
    .fixed{
    	position: fixed;
    	left:0;
    	right:0;
    }
    
</style>
</head>

<body>
<div id="header"></div>

<div id="contents">
	<div id="logoWrap" class="fixed">
		<img src="/img/back_logo.png" alt="요리비책요리재료로고">
	</div>
    <div class="search_wrap fixed">
        <div class="search_box">
            <span><i class="fas fa-search"></i></span>
            <input id="search" type="search" autocomplete="off">
            <ul class="autocomplete">
            </ul>
        </div>
	    <form action="/ingredient" method="get">
	    	<input readonly type="hidden" id="submitIng" name="ingredient" />
	        <button id="submitBtn" type="submit">요리검색  <i class="fas fa-search"></i></button>
	    </form>
    </div>
    <div id="dishWrap" class="fixed">
        <div id="dishOuter">
            <ul class="ingredient_list">
            </ul>
            <div id="dishInner">
            </div>
        </div>
    </div>
</div>

<div id="footer"></div>	

<script src="/js/back-search.js"></script>
<script>
    
	<%--21-07-22 18:40 접시에 재료 등록 함수 선언 --%>
    function serving(target){
    	tmpStr="";
    	//재료 리스트 비우기
    	$ingList.empty();
    	//Set에 해당 재료 추가
        ingSet.add(target);
    	//Set의 요소들을 재료 리스트 영역에 추가
        for(const ing of ingSet){
            $("<li class='checked_ingredient'>").text(ing).appendTo($ingList);
            tmpStr+=ing+" ";
        }
    	//submit할 input 영역에 set에 등록된 재료 입력
        tmpStr=tmpStr.substring(0,tmpStr.length-1);
        $submitIng.val(tmpStr);
        //재료 등록시 튀어오르는 애니메이션
        $(".checked_ingredient").animate({
        	top : '-50px'
        },500,function(){
        	$(".checked_ingredient").animate({
        		top : '0px'
        	},300);
        });
    }
    
    
</script>
</body>
</html>