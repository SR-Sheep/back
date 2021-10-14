package org.sheep.back.vo;

import java.sql.Timestamp;

public class TitleIng {
	
	private int no,titleNo,ingNo;
	private String ingredient, title;
	private Timestamp regdate;
	
	public TitleIng(int titleNo, int ingNo) {
		this.titleNo=titleNo;
		this.ingNo=ingNo;
	}
	
	public TitleIng() {
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getTitleNo() {
		return titleNo;
	}

	public void setTitleNo(int titleNo) {
		this.titleNo = titleNo;
	}

	public int getIngNo() {
		return ingNo;
	}

	public void setIngNo(int ingNo) {
		this.ingNo = ingNo;
	}

	public Timestamp getRegdate() {
		return regdate;
	}

	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	

}
