﻿package com.tm.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tm.iot.service.DashboardService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	@Autowired
	private DashboardService service;

	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("list", service.getList());
		return "dashboard/list";
	}
	
	@RequestMapping("/view")
	public String view(
			@RequestParam("deviceCode") int deviceCode
			, Model model) {
		model.addAttribute("list", service.getDetailList(deviceCode));
		return "dashboard/view";
	}
}