package com.tm.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.iot.model.Dashboard;
import com.tm.iot.service.DashboardService;

/**************************************
 *  Title : Dashboard rest controller
 *  Author : 이종석
 *  Date : 2017. 12
 *  
 *  retrofit으로 안드로이드에서 요청이 왔을 때
 *  각 URL에 맞게 처리해줌
 *************************************/

@RestController
public class DashboardRestController {
	
	@Autowired
	private DashboardService service;
	
	@RequestMapping("/api/list")
	public List<Dashboard> getList() {
		return service.getList();
	}
	
	@RequestMapping("/api/view/{deviceCode}/{sensorCode}")
	public List<Dashboard> getDetail(Dashboard dashboard) {
		return service.getDetailList(dashboard);
	}
}
