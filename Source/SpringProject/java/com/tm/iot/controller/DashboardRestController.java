package com.tm.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.iot.model.Dashboard;
import com.tm.iot.service.DashboardService;

@RestController
public class DashboardRestController {
	
	@Autowired
	private DashboardService service;
	
	@RequestMapping("/api/list")
	public List<Dashboard> getList() {
		return service.getList();
	}
	
	@RequestMapping("/api/view/{deviceCode}")
	public List<Dashboard> getDetail(@PathVariable int deviceCode) {
		return service.getDetailList(deviceCode);
	}
}
