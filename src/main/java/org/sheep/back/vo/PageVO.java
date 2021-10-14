package org.sheep.back.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageVO {
	
	private int start, end,pageNo;
	//1페이지당 10개씩 출력
	private int numPage=10;
	private String ingredientsStr;
	private List<String> ingredients;

	public PageVO() {
		// TODO Auto-generated constructor stub
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		this.end = pageNo*numPage;
		this.start = end - numPage + 1; 
	}
	
	//21-07-28 00:19 input의 string이 입력되면 띄어쓰기를 제거하고 list에 값 입력
	public void setIngredientsStr(String ingredientsStr) {
		this.ingredientsStr = ingredientsStr.trim();
		this.ingredients=new ArrayList<String>
			(Arrays.asList(this.ingredientsStr.split(" ")));
	}
	
	public PageVO(int pageNo) {
		this.pageNo = pageNo;
		this.end = pageNo*numPage;
		this.start = end - numPage + 1; 
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public String getIngredientsStr() {
		return ingredientsStr;
	}

	public int getPageNo() {
		return pageNo;
	}


	public int getNumPage() {
		return numPage;
	}

	public void setNumPage(int numPage) {
		this.numPage = numPage;
		this.end = pageNo*numPage;
		this.start = end - numPage + 1;
	}
	
	

	
}
