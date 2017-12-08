package com.tm.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.iot.model.IotData;
import com.tm.iot.service.IotDataService;

@RestController
@RequestMapping("/dashboard")
public class IotDataRestController {
	
	@Autowired
	private IotDataService service;
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public boolean insert(@RequestBody IotData iotData) {
		return service.insert(iotData);
	}
}
