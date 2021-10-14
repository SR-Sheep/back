package org.sheep.back.dao;

import java.util.List;

import org.sheep.back.vo.Cooking;
import org.sheep.back.vo.PageVO;

public interface CookingDAO {
	//21-07-06 11:19 요리 제목 입력
	public int insertCooking(Cooking cooking);
	//21-07-07 21:32 id로 cooking 정보 가져오기
	public Cooking selectOneCooking(String id);
	//21-07-17 23:20 모든 cooking 정보 불러오기
	//21-07-26 00:54 pageVO 추가하여 불러오기
	public List<Cooking> selectListAll(PageVO pageVO);
	//21-07-27 22:46 재료를 통해 요리 정보 불러오기
	public List<Cooking> selectListByIngredients(PageVO pageVO);
	//21-10-02 16:50 모든 Cooking 제거
	public int deleteCooking();

}
