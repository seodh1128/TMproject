package com.tm.iot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.iot.dao.IotDataDao;
import com.tm.iot.model.IotData;

@Service
public class IotDataServiceImpl implements IotDataService {

	@Autowired
	private IotDataDao dao;


	@Override
	@Transactional
	public boolean insert(IotData iotData) {
		int result = dao.insert(iotData); 
		return result == 1;
	}
}
