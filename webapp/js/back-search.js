	//자동 완성 재료
	const $autoIng=$(".auto_ingredient");
	//재료 리스트 구역
	const $ingList=$(".ingredient_list");
	//선택 중인 재료
	const $ing=$(".checked_ingredient");
	//재료 검색 입력
	const $search=$("#search");
	//검색 박스
	const $search_box=$(".search_box");
	//자동 완성 구역
	const $auto=$(".autocomplete");
	//제출 폼 부분
	const $submitIng=$("#submitIng");
	//document
	const $document=$(document);
	//목록이 있는 재료인지 여부 판별
	let isIngredient = false;
	//중복된 재료 제거를 위한 
	let ingSet=new Set();
	//임시로 사용할 string
	let tmpStr="";
	
	/*21-07-22 00:06 검색창에서 키보드를 눌렀을 때 자동완성 */
	$search.keyup(function(e){
		console.log("키업"+e.keyCode);
		/*21-07-22 17:06 아래 방향키 누를경우 */
		if(e.keyCode===40){
	    	/*21-07-22 17:06 selected 클래스가 없거나 맨 마지막 요소에 selected 클래스인 경우 맨 위에 selected 클래스 부여 */
			if(!$auto.children().hasClass("selected")||$auto.children().last().hasClass("selected")){
				$auto.children().removeClass("selected").first().addClass("selected");
	    	/*21-07-22 17:09 나머지 경우 다음 요소에 selected 클래스 부여 */
			}else{
				$auto.children(".selected").removeClass("selected").next().addClass("selected");
			}
	    	/*21-07-22 17:19 input의 값을 selected의 값으로 변경 */
			$search.val($(".selected").text());
		/*21-07-22 17:13 윗 방향키 누를경우 */
		}else if(e.keyCode===38){
	    	/*21-07-22 17:14 selected 클래스가 없거나 맨 처음 요소에 selected 클래스인 경우 맨 마지막에 selected 클래스 부여 */
			if(!$auto.children().hasClass("selected")||$auto.children().first().hasClass("selected")){
				$auto.children().removeClass("selected").last().addClass("selected");
	    	/*21-07-22 17:15 나머지 경우 이전 요소에 selected 클래스 부여 */
			}else{
				$auto.children(".selected").removeClass("selected").prev().addClass("selected");
			}
	    	/*21-07-22 17:19 input의 값을 selected의 값으로 변경 */
			$search.val($(".selected").text());
			
		/*21-07-22 18:08 엔터를 쳤을 때 -> 아래 사항으로 변경*/
		/*22-01-14 23:18 엔터를 치지 않았을 때
			키업 이벤트시 엔터가 두번 쳐지는 문제를 해결하기 위해 keypress이벤트로 변경*/
		}else if(e.keyCode!==13){
			/*자동 완성에 selected 클래스가 있다면 접시에 재료 올리기 
			$auto.children().each(function(){
				if($(this).text()==$search.val()){
					isIngredient=true;
					return false;
				}
			})
			if(isIngredient){
				serving($search.val());
				$search.val('');
				isIngredient=false;
			}
			*/
			/*22-01-14 엔터시 삭제
			$auto.empty();*/
		/*21-07-22 00:13 나머지 다른 키 입력시*/
		    /*키 입력마다 재료 list 불러오기 */
			loadIngredients();	
		}
		
		//else{
		//    loadIngredients();
		//}
		//else end
		
	})//keyup event end
	/*22-01-14 23:18 엔터를 치지 않았을 때
			키업 이벤트시 엔터가 두번 쳐지는 문제를 해결하기 위해 keypress이벤트로 변경*/
	$search.keypress(function(e){
		if(e.keyCode===13){
			/*자동 완성에 selected 클래스가 있다면 접시에 재료 올리기 */
			$auto.children().each(function(){
				if($(this).text()==$search.val()){
					isIngredient=true;
					return false;
				}
			})
			if(isIngredient){
				serving($search.val());
				$search.val('');
				isIngredient=false;
			}
			/*22-01-14 엔터시 삭제*/
			$auto.empty();
		}
	})//keypress event end                
	
	/*21-07-22 00:13 ajax를 이용하여 재료 list 불러오기 */
	function loadIngredients(){
		$.ajax({
	        //맵핑 url 설정
	        url:"/autoComplete",
	        //전송 방식 (GET/POST)
	        type:"GET",
	        //파라미터 넣기
	        data:{ingredient : $search.val()},
	        //로딩 성공
	        success:function(json){
	       	 //자동완성 비우기
	       	 $auto.empty();
	       	 //리스트 불러오기
	       	 $(json).each(function(index) {
	       		$('<li>').addClass('auto_ingredient').text(this.ingredient).appendTo($auto); 
	       	 })
	        },
	        //로딩 실패시
	        error:function(){
	            alert("서버점검중!")
	        }
		})//ajax end
	}
	   
	/*21-07-22 18:22 input에 포커스가 오면 재료 list 불러오기 
	$search.on("focus",function(){
		loadIngredients();
	});
	*/
	
	/*21-07-22 18:24 input에 포커스가 없어지면 자동완성 삭제 */
	$search.on("blur",function(){
		$search.val('');
		$auto.empty();
	});
		
	/*21-07-22 18:55 (블러 이전에) 마우스 클릭시 접시에 재료 등록 */
	$auto.on("mousedown","li",function (){
		serving($(this).text());
		$auto.empty();
	})
	   
	    
	/*21-07-25 19:23 접시 위 재료 클릭시 해당 재료 삭제 */
	$ingList.on("click","li",function (){
		tmpStr="";
		/*set에서 해당 재료 제거*/
	    ingSet.delete($(this).text());
		/*접시 비우기*/
	    $(this).detach();
		/*input value를 변경된 set 값으로 변경하기*/
	    for(const ing of ingSet){
	        tmpStr+=ing+" ";
	    }
	    $submitIng.val(tmpStr);
	})