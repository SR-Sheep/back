package org.sheep.back.vo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Title {
	private int no,cookingNo;
	private String title;
	private Timestamp regdate;
	private List<Ingredient> ingredients;
	
	public Title(int cookingNo,String title) {
		this.cookingNo=cookingNo;
		this.title=title;
	}
	
	
	public Title() {
		// TODO Auto-generated constructor stub
	}
	
	public Title(String title) {
		this.title=title;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getCookingNo() {
		return cookingNo;
	}

	public void setCookingNo(int cookingNo) {
		this.cookingNo = cookingNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getRegdate() {
		return regdate;
	}

	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}
	
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	
	@Override
	public String toString() {
		return "cookingNo : "+cookingNo+" title : "+title;
	}
	
	
	
	
	

}
