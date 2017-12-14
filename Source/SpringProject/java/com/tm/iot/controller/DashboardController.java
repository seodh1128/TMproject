package com.tm.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tm.iot.model.Dashboard;
import com.tm.iot.service.DashboardService;

/**************************************
 *  Title : Dashboard controller
 *  Author : 이종석
 *  Date : 2017. 12
 *************************************/

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	@Autowired
	private DashboardService service;

	// 각 항목의 최신 데이터를 리턴
	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("list", service.getList());
		return "dashboard/list";
	}
	
	// 항목의 상세 데이터를 리턴
	@RequestMapping("/view")
	public String view(Dashboard dashboard, Model model) {
		model.addAttribute("list", service.getDetailList(dashboard));
		return "dashboard/view";
	}
}