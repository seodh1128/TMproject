package com.tm.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.iot.model.Device;
import com.tm.iot.service.DeviceService;

@RestController
@RequestMapping("/dashboard")
public class DeviceRestController {
	
	@Autowired
	private DeviceService service;
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public boolean insert(@RequestBody Device device) {
		return service.insert(device);
	}
}
