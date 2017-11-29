package com.tm.iot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tm.iot.model.Device;
import com.tm.iot.service.DeviceService;

@Controller
public class DeviceController {
	@Autowired
	DeviceService service;

	@RequestMapping("/dashboard")
	public String list(Model model) {
		model.addAttribute("list", service.getList()); // jsp에서 "paging", "list" 사용 가능해짐
		return "dashboard/list";
	}
}