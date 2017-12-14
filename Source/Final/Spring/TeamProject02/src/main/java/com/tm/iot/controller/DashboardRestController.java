package com.tm.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.iot.model.Dashboard;
import com.tm.iot.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardRestController {
	
	@Autowired
	private DashboardService service;
	
	@RequestMapping("/api/list")
	public List<Dashboard> getList() {
		System.out.println("안드로이드에서 접속이 감지되었습니다!");
		return service.getList();
	}
	
	@RequestMapping("/api/view/{deviceCode}/{sensorCode}")
	public List<Dashboard> getDetail(Dashboard dashboard) {
		return service.getDetailList(dashboard);
	}
}
