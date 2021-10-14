package org.sheep.back.dao;

import org.apache.ibatis.session.SqlSession;
import org.sheep.back.vo.Title;
import org.sheep.back.vo.TitleIng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TitleDAOImpl implements TitleDAO {

	@Autowired
	private SqlSession session;
	
	//21-07-07 22:46 레시피 부제목 입력	
	@Override
	public int insertTitle(Title title) {
		return session.insert("title.insertTitle", title);
	}
	//21-07-07 22:46 레시피 부제목 입력	
	@Override
	public Title selectOneByTitle(Title title) {
		return session.selectOne("title.selectOneByTitle", title);
	}
	//21-07-09 02:37 title_ing 입력	
	@Override
	public int insertTitleIng(TitleIng titleIng) {
		return session.insert("title.insertTitleIng", titleIng);
	}
	
	//21-10-02 16:50 모든 부제목 제거

	@Override
	public int deleteTitle() {
		return session.delete("title.deleteTitle");
	}
	
	//21-10-02 16:50 모든 부제목-재료 제거
	@Override
	public int deleteTitleIng() {
		return session.delete("title.deleteTitleIng");
	}
}
