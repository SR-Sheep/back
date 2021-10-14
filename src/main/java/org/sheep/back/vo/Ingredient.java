package org.sheep.back.vo;

import java.sql.Timestamp;

public class Ingredient {
	
	private int no;
	private String ingredient;
	private Timestamp regdate;
	
	public Ingredient(String ingredient) {
		this.ingredient = ingredient;
	}
	
	public Ingredient() {
		// TODO Auto-generated constructor stub
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	public Timestamp getRegdate() {
		return regdate;
	}

	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}

}
