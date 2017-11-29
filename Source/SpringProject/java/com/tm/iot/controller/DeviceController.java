package com.tm.iot.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tm.iot.service.DeviceService;

@Controller
@RequestMapping("/dashboard")
public class DeviceController {
	@Resource(name="deviceServiceImpl")
	DeviceService service;

	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("list", service.getList());
		return "dashboard/list";
	}
	
	@RequestMapping("/view")
	public String view(
			@RequestParam("deviceCode") String deviceCode
			, Model model) {
		model.addAttribute("list", service.getDeviceDetail(deviceCode));
		return "dashboard/view";
	}
}