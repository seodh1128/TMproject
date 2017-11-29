package com.tm.iot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.iot.dao.SensorDao;
import com.tm.iot.model.Sensor;

@Service
public class SensorServiceImpl implements SensorService{
	
	@Autowired
	private SensorDao dao;
	
	@Override
	public List<Sensor> getList() {
		return dao.getList();
	}
}
