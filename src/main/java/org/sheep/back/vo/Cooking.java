package org.sheep.back.vo;

import java.sql.Timestamp;
import java.util.List;

public class Cooking {
	
	private int no;
	private String cooking,thumbnail,id,context;
	private Timestamp regdate;
	private List<Title> subtitles;
	
	public Cooking(Item item) {
		this.cooking=item.getTitle();
		this.id=item.getVideoId();
		this.thumbnail=item.getThumbnail();
		this.context=item.getDesription();
		String date =item.getPublishedAt();
		this.regdate=Timestamp.valueOf(date.substring(0, 10)+" "+date.substring(11,19));
	}

	public Cooking() {
		// TODO Auto-generated constructor stub
	}
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getCooking() {
		return cooking;
	}

	public void setCooking(String cooking) {
		this.cooking = cooking;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getRegdate() {
		return regdate;
	}

	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}

	public List<Title> getSubtitles() {
		return subtitles;
	}

	public void setSubtitles(List<Title> subtitle) {
		this.subtitles = subtitle;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	
	
	
	

}
