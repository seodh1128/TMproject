package com.tm.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.iot.model.Dashboard;
import com.tm.iot.service.DashboardService;

/**************************************
 *  Title : Dashboard rest controller
 *  Author : ������
 *  Date : 2017. 12
 *  
 *  retrofit���� �ȵ���̵忡�� ��û�� ���� ��
 *  �� URL�� �°� ó������
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
