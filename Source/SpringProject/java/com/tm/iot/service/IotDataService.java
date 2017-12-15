package com.tm.iot.service;

import com.tm.iot.model.IotData;

/**************************************
 *  Title : IotData Service Interface
 *  Author : 이종석
 *  Date : 2017. 12
 *  Dashboard Data Transfer Object
 *************************************/

public interface IotDataService {
	boolean insert(IotData iotData);
}
