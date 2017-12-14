package com.tm.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.iot.model.IotData;
import com.tm.iot.service.IotDataService;

/**************************************
 *  Title : IotData Rest controller
 *  Author : 이종석
 *  Date : 2017. 12
 *  
 *  retrofit으로 insert 요청이 왔을 때
 *  iot_data table에 insert 함
 *************************************/

@RestController
@RequestMapping("/api/iotData")
public class IotDataRestController {
	
	@Autowired
	private IotDataService service;
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public boolean insert(@RequestBody IotData iotData) {
		return service.insert(iotData);
	}
}
