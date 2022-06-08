package org.sheep.back.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sheep.back.dao.CookingDAO;
import org.sheep.back.dao.IngredientDAO;
import org.sheep.back.dao.TitleDAO;
import org.sheep.back.vo.Cooking;
import org.sheep.back.vo.Ingredient;
import org.sheep.back.vo.Item;
import org.sheep.back.vo.PageVO;
import org.sheep.back.vo.Title;
import org.sheep.back.vo.TitleIng;
import org.sheep.back.vo.Youtube;
import org.sheep.back.vo.YoutubeJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class CookingServiceImpl implements CookingService {
	
	@Autowired
	private CookingDAO cookingDAO;
	@Autowired
	private TitleDAO titleDAO;
	@Autowired
	private IngredientDAO ingredientDAO;
	
	//21-07-21 23:33 재료 검색시 재료 리스트 불러오기
	@Override
	public List<Ingredient> searchIngredient(String ingredient) {
		//21-07-22 01:16 키워드가 비어있을 시 null return
		if(ingredient.equals("")) return null;
		return ingredientDAO.selectListByKeyword(ingredient);
	}
	
	
	//21-09-28 스케쥴링을 통한 자동 input을 위한 메서드
	@Override
	public void getYoutubeJson(Youtube youtube) {
		String json=youtube.getJson();
		if(json==null) {
			return;
		}
		ObjectMapper om = new ObjectMapper();
		try {
			YoutubeJson youtubeJson = om.readValue(json, YoutubeJson.class);
			String nextToken =youtubeJson.getNextPageToken();
			
			for(Item item:youtubeJson.getItems()) {
				//input 실행
				inputRecipe(item);
			}
			//nextToken이 존재하면 token을 재설정하고 재귀
			if(nextToken!=null) {
				youtube.setToken(nextToken);
				getYoutubeJson(youtube);
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//getYoutubeJson end
	
	//21-07-06 레시피 정보 입력하기
	@Override
	public void inputRecipe(Cooking cooking,String ingredientStr) {
		//21-07-08 18:06 이미 id가 등록이 되어있으면 리턴
		if(getCooking(cooking.getId())!=null) return;
		//21-07-07 요리 제목 등록
		int cookingNo=registCooking(cooking);
		
		//21-07-06 공백 제거 후 엔터 기준 재료 분리
		String[] ingredientArr=ingredientStr.trim().split("\n");
		//21-07-07 22:53 부제목이 없으면 주재료라고 넣음 
		String subTitle="주재료";
		//21-07-11 14:30 부제목과 재료 구분을 위해 map 선언
		ConcurrentHashMap<String, List<String>> recipes = new ConcurrentHashMap<String, List<String>>(); 
		
		for(String s:ingredientArr) {
			//21-07-11 14:33 비어 있으면 패쓰
			if(s.length()==0) continue;
			//21-07-12 19:43 --- 일 경우 패쓰
			if(s.matches("\\-{4,}")) continue;
			//21-07-06 .으로 끝나면 부가적인 설명임으로 패쓰
			if(s.endsWith(".")) continue;
			//21-07-07 22:58 *이나 [로 시작하면 부제목에 해당
			if(s.startsWith("*")||s.startsWith(" *")||s.startsWith("[")||s.endsWith("-")) {
				subTitle=s.split("\\*|\\[|\\]|\\-")[1].trim(); continue;
			}
			//21-07-06 23:00 숫자나 약 분수 특수문자, 적당량으로 시작하면 용량이므로 앞부분만 남기기 
			s=s.split("[0-9|약|½|⅓|⅔|¼|¾|⅛|⅜|⅝|⅞]|적당[가-힣]*|조금|소량|전량|약간")[0];
			//21-07-22 17:30 ver만 나오는 경우만 ver1 초고추장으로 변경
			if(s.equals("Ver")) s="Ver1 초고추장";
			//21-07-06 23:02 괄호 제거
			if(s.contains("(")) s=s.substring(0,s.lastIndexOf("("));
			if(s.contains("꼬집")) s=s.substring(0, s.length()-3);
			//22-01-21 21:47 기호에따라, 개인취향에 맞게 라는 표현이 들어가면 앞부분만 취함
			if(s.contains("기호")) s=s.split("기호")[0];
			if(s.contains("개인")) s=s.split("개인")[0];
			//21-07-13 06:02 재료명 앞에 삶은 붙으면 제거
			if(s.matches("^삶은[ |가-힣]*")) s=s.substring(2,s.length());
			//21-07-06 23:05 공백 제거
			s=s.replaceAll(" ", "");
			//21-07-12 비어있는 문자열이면 패쓰
			if(s.equals("")) continue;
			
			//21-07-11 14:35 부제목이 없으면 recipes map생성
			recipes.putIfAbsent(subTitle, new ArrayList<String>());
			//21-07-11 14:39 부제목에 재료 추가
			recipes.get(subTitle).add(s);
		}//for end
		
		/*21-07-18 23:42 멀티쓰레드로 인해 insert와 select간의 gap이 발생하여 이전 코드 폐기
		 따로 메서드를 선언함으로 순차적으로 진행되도록 설계함*/
		//21-07-18 23:43 map의 key(부제목) 불러오기
		for(String title:recipes.keySet()) {
			//21-07-18 23:44 부제목을 등록하고 부제목no 가져오기	
			int titleNo=registTitle(title, cookingNo);
			//21-07-18 23:45 map의 key로 부제목을 입력하고 재료 list 불러오기
			for(String ingredient:recipes.get(title)) {
				//21-07-18 23:45 각 재료를 등록하고 재료no 가져오기
				int ingredientNo=registIngredient(ingredient);
				//21-07-18 23:45 부제목과 재료 연결 테이블에 입력하기
				registTitleIng(titleNo, ingredientNo);
			}//for end
		}//for end
	}//inputRecipe end
		
		
	//21-10-01 레시피 정보 자동 입력하기위한 item 객체 사용
	@Override
	public void inputRecipe(Item item) {
		//21-07-08 18:06 이미 id가 등록이 되어있으면 리턴
		if(getCooking(item.getVideoId())!=null) return;
		//* 21-10-01 19:50 쿠킹 객체 생성
		Cooking cooking = new Cooking(item);
		//21-07-07 요리 제목 등록
		int cookingNo=registCooking(cooking);
		
		//21-07-06 공백 제거 후 엔터 기준 재료 분리
		String[] ingredientArr=cooking.getContext().split("\n");
		
		//21-07-07 22:53 부제목이 없으면 주재료라고 넣음 
		String subTitle="주재료";
		
		//21-07-11 14:30 부제목과 재료 구분을 위해 map 선언
		ConcurrentHashMap<String, List<String>> recipes = new ConcurrentHashMap<String, List<String>>(); 
		
		for(String s:ingredientArr) {
			s=s.trim();
			
			//21-07-11 14:33 비어 있으면 패쓰
			if(s.length()==0) continue;
			//21-07-12 19:43 --- 일 경우 패쓰
			if(s.matches("\\-{4,}")) continue;
			//21-07-06 .으로 끝나면 부가적인 설명임으로 패쓰
			if(s.endsWith(".")) continue;
			//21-07-07 22:58 *로 시작하면 부제목에 해당 
			if(s.startsWith("*")||s.startsWith(" *")||s.startsWith("[")||s.endsWith("-")) {
				subTitle=s.split("\\*|\\[|\\]|\\-")[1].trim(); continue;
			}
			//21-07-06 23:00 숫자나 약 분수 특수문자, 적당량으로 시작하면 용량이므로 앞부분만 남기기 
			s=s.split("[0-9|약|½|⅓|⅔|¼|¾|⅛|⅜|⅝|⅞]|적당[가-힣]*|조금|소량|전량|약간")[0];
			//21-07-22 17:30 ver만 나오는 경우만 ver1 초고추장으로 변경
			if(s.equals("Ver")) s="Ver1 초고추장";
			//21-07-06 23:02 괄호 제거
			if(s.contains("(")) s=s.substring(0,s.lastIndexOf("("));
			if(s.contains("꼬집")) s=s.substring(0, s.length()-3);
			//21-07-13 06:02 재료명 앞에 삶은 붙으면 제거
			if(s.matches("^삶은[ |가-힣]*")) s=s.substring(2,s.length());
			//21-07-06 23:05 공백 제거
			s=s.replaceAll(" ", "");
			//21-07-12 비어있는 문자열이면 패쓰
			if(s.equals("")) continue;
			
			//21-07-11 14:35 부제목이 없으면 recipes map생성
			recipes.putIfAbsent(subTitle, new ArrayList<String>());
			//21-07-11 14:39 부제목에 재료 추가
			recipes.get(subTitle).add(s);
			//21-07-13 05:53 재료명 뒤 용량을 표기하는 조금, 소량, 전량, 약간, ~꼬집 제거
		}
	
		/*21-07-18 23:42 멀티쓰레드로 인해 insert와 select간의 gap이 발생하여 이전 코드 폐기
						 따로 메서드를 선언함으로 순차적으로 진행되도록 설계함*/
		//21-07-18 23:43 map의 key(부제목) 불러오기
		for(String title:recipes.keySet()) {
			//21-07-18 23:44 부제목을 등록하고 부제목no 가져오기
			int titleNo=registTitle(title, cookingNo);
			//21-07-18 23:45 map의 key로 부제목을 입력하고 재료 list 불러오기
			for(String ingredient:recipes.get(title)) {
				//21-07-18 23:45 각 재료를 등록하고 재료no 가져오기
				int ingredientNo=registIngredient(ingredient);
				//21-07-18 23:45 부제목과 재료 연결 테이블에 입력하기
				registTitleIng(titleNo, ingredientNo);
			}
		}
	}
	
	//21-07-18 23:30 cooking 등록 후 cookingNo 출력하기
	public int registCooking(Cooking cooking) {
		cookingDAO.insertCooking(cooking);
		return cooking.getNo();
	}
	
	//21-07-18 23:32 title 등록 후 titleNo 출력하기
	public int registTitle(String title, int cookingNo) {
		Title t = new Title(cookingNo, title);
		titleDAO.insertTitle(t);
		return t.getNo();
	}
	
	//21-07-18 23:34 ingredient 등록 혹은 검색 후 ingredientNo 출력하기
	public int registIngredient(String ingredient) {
		Ingredient ing= new Ingredient(ingredient);
		//21-07-18 23:36 ingredient 등록이 되지 않으면 해당 재료 정보 가져오기
		if(ingredientDAO.insertIngredient(ing)==0) {
			ing=ingredientDAO.selectOneIngredient(ing);
		}
		return ing.getNo();
	}
	//21-07-18 23:38 부제목-재료 등록
	public int registTitleIng(int titleNo, int ingredientNo) {
		return titleDAO.insertTitleIng(new TitleIng(titleNo, ingredientNo));
	}
	
	//21-07-17 19:20 모든 쿠킹 리스트 가져오기
	//21-07-27 22:52 재료를 통해 요리 정보 불러오기 추가
	@Override
	public Map<String, Object> getCookingList(PageVO pageVO) {
		Map<String, Object> modelMap = new ConcurrentHashMap<>();
		//21-07-27 23:01 불러온 재료가 없으면 전체 리스트 출력
		if(pageVO.getIngredients().get(0).equals("")) {
			modelMap.put("cookings",cookingDAO.selectListAll(pageVO));
		//21-07-27 23:01 불러온 재료가 있으면 검색 후 리스트 출력
		}else {
			modelMap.put("cookings",
					cookingDAO.selectListByIngredients(pageVO));
		}
		return modelMap;
	}
	
	@Override
	public Map<String, Object> getCookingListByIngredient(PageVO pageVO) {
		Map<String, Object> modelMap = new ConcurrentHashMap<>();
		modelMap.put("cookings",cookingDAO.selectListByIngredients(pageVO));
		return modelMap;
	}
	
	@Override
	public Cooking getCooking(String id) {
		return cookingDAO.selectOneCooking(id);
	}
	
	//21-10-02 16:50 모든 DB 제거, 일련의 작업이므로 transaction 선언
	@Override
	@Transactional
	public void deleteDB() {
		//부제목-재료 제거
		titleDAO.deleteTitleIng();
		//부제목 제거
		titleDAO.deleteTitle();
		//재료 제거
		ingredientDAO.deleteIngredient();
		//제목 제거
		cookingDAO.deleteCooking();
	}

}
