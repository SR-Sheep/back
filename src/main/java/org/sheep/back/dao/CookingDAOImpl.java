package org.sheep.back.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.sheep.back.vo.Cooking;
import org.sheep.back.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CookingDAOImpl implements CookingDAO {

	@Autowired
	private SqlSession session;
	
	//21-07-06 11:19 요리 제목 입력
	@Override
	public int insertCooking(Cooking cooking) {
		return session.insert("cooking.insertCooking", cooking);
	}
	//21-07-07 23:33 id로 cooking 정보 가져오기
	@Override
	public Cooking selectOneCooking(String id) {
		return session.selectOne("cooking.selectOneCooking",id);
	}
	//21-07-17 23:20 모든 cooking 정보 불러오기
	//21-07-26 00:54 pageVO 추가하여 불러오기
	@Override
	public List<Cooking> selectListAll(PageVO pageVO) {
		return session.selectList("cooking.selectListAll",pageVO);
	}
	//21-07-27 22:46 재료를 통해 요리 정보 불러오기
	@Override
	public List<Cooking> selectListByIngredients(PageVO pageVO) {
		return session.selectList("cooking.selectListByIngredients",pageVO);
	}
	
	//21-10-02 16:50 모든 DB 제거
	@Override
	public int deleteCooking() {
		return session.delete("cooking.deleteCooking");
	}
	
	
	
}
