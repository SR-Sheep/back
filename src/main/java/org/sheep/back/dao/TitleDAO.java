package org.sheep.back.dao;

import org.sheep.back.vo.Title;
import org.sheep.back.vo.TitleIng;

public interface TitleDAO {
	//21-07-07 22:19 레시피 부제목 입력
	public int insertTitle(Title title);
	
	//21-07-08 18:38 부제목 이름으로 부제목 정보 가져오기
	public Title selectOneByTitle(Title title);

	//21-07-09 02:37 title_ing 입력
	public int insertTitleIng(TitleIng titleIng);
	
	//21-10-02 16:50 모든 부제목 제거
	public int deleteTitle();
	
	//21-10-02 16:50 모든 부제목-재료 제거
	public int deleteTitleIng();
}
