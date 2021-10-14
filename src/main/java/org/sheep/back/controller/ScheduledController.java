package org.sheep.back.controller;


import org.sheep.back.service.CookingService;
import org.sheep.back.vo.Youtube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledController {
	
	@Autowired
	private CookingService service;
	//매 30분마다 작동
	@Scheduled(cron = "0 0/30 * * * *")
	public void inputScheduled() {
		service.getYoutubeJson(new Youtube());
	}
}
