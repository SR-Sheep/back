<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   	<div id="header">
		<div id="headerWrap">
			<div id="logoWrap">
				<a href="/">
					<img src="/img/back_logo_middle.png" alt="요리비책요리재료로고">
				</a>
			</div>
			<div class="search_wrap">
				<div class="search_box">
					<span><i class="fas fa-search"></i></span>
					<input id="search" type="search" autocomplete="off">
					<ul class="autocomplete"></ul>
				</div>
				<ul class="ingredient_list"></ul>
			</div>
			<%--.search_wrap end --%>
			<form id="btnForm" action="/ingredient" method="get">
				<input readonly type="hidden" id="submitIng" name="ingredient"/>
				<button id="submitBtn" type="submit">
					<i class="fas fa-search"></i>
					<h4>요리검색</h4>
				</button>
			</form>
		</div>
		<%--#headerWrap end --%>
	</div>
	<%--#header end --%>