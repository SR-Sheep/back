package org.sheep.back.dao;

import java.util.List;

import org.sheep.back.vo.Ingredient;

public interface IngredientDAO {
	//21-07-08 18:17 요리 재료 등록
	public int insertIngredient(Ingredient ingredient);
	
	//21-07-08 18:50 요리 재료 이름으로 요리 재료 정보 가져오기
	public Ingredient selectOneIngredient(Ingredient ingredient);
	
	//21-07-21 23:29 재료 검색시 재료 리스트 불러오기
	public List<Ingredient> selectListByKeyword(String ingredient);
	
	//21-10-02 16:50 모든 재료 제거
	public int deleteIngredient();
	
}
