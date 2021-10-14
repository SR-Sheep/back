package org.sheep.back.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.sheep.back.vo.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class IngredientDAOImpl implements IngredientDAO {
	
	@Autowired
	private SqlSession session;
	
	//21-07-08 18:20 요리 재료 등록
	@Override
	public int insertIngredient(Ingredient ingredient) {
		return session.insert("ingredient.insertIngredient", ingredient);
	}
	//21-07-08 18:48 요리 재료 이름으로 요리 재료 정보 가져오기
	@Override
	public Ingredient selectOneIngredient(Ingredient ingredient) {
		return session.selectOne("ingredient.selectOneIngredient", ingredient);
	}
	//21-07-21 23:29 재료 검색시 재료 리스트 불러오기
	@Override
	public List<Ingredient> selectListByKeyword(String ingredient) {
		return session.selectList("ingredient.selectListByKeyword", ingredient);
	}
	
	//21-10-02 16:50 모든 재료 제거
	@Override
	public int deleteIngredient() {
		return session.delete("ingredient.deleteIngredient");
	}
	
	

}
