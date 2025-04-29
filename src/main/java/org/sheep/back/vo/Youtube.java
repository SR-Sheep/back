package org.sheep.back.vo;

import org.jsoup.Jsoup;
//21-10-01 유튜브 자동 업로드를 위한 클래스 선언
public class Youtube {
	//받아오기 위한 개인 키
	private final String KEY = "***"; //보안을 위한 개인키 삭제
	//채널 id (백종원의 요리비책)
	private final String PLAYLISTID = "UUyn-K7rZLXjGl7VXGweIlcA";
	//최대 결과수 50, 이후 토큰을 통해 다음 페이지를 로딩함
	private final int MAXRESULTS=50;
	//유튜브 api 기본 uri
	private final String URL = "https://www.googleapis.com/youtube/v3/playlistItems";
	private String json,token;
	
	public Youtube() {}

	public String getJson() {
		//url = 기본 url + key+ maxResult
		String url=URL+"?part=snippet&playlistId="+this.PLAYLISTID+"&key="+this.KEY+"&maxResults="+this.MAXRESULTS;
		//다음 토큰이 있으면 url+=token
		if(token!=null) {
			url+="&pageToken="+token;
		} 
		try {
			//Jsoup 라이브러리를 통해 json 받아오기
			this.json=Jsoup.connect(url).ignoreContentType(true).execute().body();
			return json;
		}catch (Exception e) {
			System.out.println(e.getStackTrace());
			//출력 내용이 없으면 null
			return null;
		}
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
