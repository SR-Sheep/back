package org.sheep.back.service;

import java.util.List;
import java.util.Map;

import org.sheep.back.vo.Cooking;
import org.sheep.back.vo.Ingredient;
import org.sheep.back.vo.Item;
import org.sheep.back.vo.PageVO;
import org.sheep.back.vo.Youtube;

public interface CookingService {
	
	//21-06-07 백종원 유튜브의 제목과 주소 가져오기
	public Map<String, Object> youtube();
	
	//21-07-06 백종원 유튜브 정보 입력
	public void inputRecipe(Cooking cooking, String ingredientStr);
	
	//21-07-07 23:36 id로 cooking 정보 가져오기
	public Cooking getCooking(String id);
	
	//21-07-17 19:20 모든 쿠킹 리스트 가져오기
	public Map<String, Object> getCookingList(PageVO pageVO);
	
	//21-07-21 23:30 재료 검색시 재료 리스트 불러오기
	public List<Ingredient> searchIngredient(String ingredient);
	
	//21-07-27 22:46 재료를 통해 요리 정보 불러오기
	public Map<String, Object> getCookingListByIngredient(PageVO pageVO);
	
	//21-09-28 스케쥴링을 통한 자동 input을 위한 메서드
	public void getYoutubeJson(Youtube youtube);
	
	//21-07-06 백종원 유튜브 정보 자동 입력
	public void inputRecipe(Item item);
	
	//21-10-02 16:50 모든 DB 제거
	public void deleteDB();
}
