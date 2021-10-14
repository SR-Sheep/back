package org.sheep.back.controller;

import java.util.List;
import java.util.Map;

import org.sheep.back.service.CookingService;
import org.sheep.back.vo.Cooking;
import org.sheep.back.vo.Ingredient;
import org.sheep.back.vo.PageVO;
import org.sheep.back.vo.Youtube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CookingController {
	@Autowired
	private CookingService service;
	
	//index 관련
	
	//메인 인덱스 접속
	@RequestMapping(value="/",method = RequestMethod.GET)
	public String userManagement() {
		return "index";
	}
	
	//21-07-21 23:36 재료 검색시 자동완성으로 재료 리스트 불러오기
	@RequestMapping(value="/autoComplete",method = RequestMethod.GET)
	@ResponseBody
	public List<Ingredient> auto(String ingredient) {
		return service.searchIngredient(ingredient);
	}
	
	//레시피 리스트 관련
	
	//레시피 페이지 접속
	@RequestMapping(value="/ingredient",method = RequestMethod.GET)
	public String ingredient(Model model,@RequestParam(defaultValue= "") String ingredient) {
		model.addAttribute("ingredient",ingredient);
		return "recipeList";
	}
	
	//input 관련
	
	//레시피 등록 접속
	@RequestMapping(value="/input",method = RequestMethod.GET)
	public String inputRecipes() {
		return "input";
	}
	//레시피 인풋 자료 가져오기
	@RequestMapping(value="/ajaxInput",method = RequestMethod.POST)
	@ResponseBody
	public String content(Cooking cooking,String ingredientStr) {
		service.inputRecipe(cooking,ingredientStr);
		return "성공";
	}
	
	//21-07-27 13:47 ajax로 레시피 정보 불러오기
	@RequestMapping(value="/loadRecipe",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> recipeList(PageVO pageVO) {
		return service.getCookingList(pageVO);
	}
	
	
	//etc
	
	//21-10-01 테스트용
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test1() {
		service.getYoutubeJson(new Youtube());
		return "index";
	}
	
	//21-10-01 DB 제거
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteDB() {
		service.deleteDB();
		return "redirect:/";
	}
	

}
