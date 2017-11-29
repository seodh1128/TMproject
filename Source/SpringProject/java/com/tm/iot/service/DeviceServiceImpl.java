package com.tm.iot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.iot.dao.DeviceDao;
import com.tm.iot.model.Device;

@Service
public class DeviceServiceImpl implements DeviceService{
	
	@Autowired
	private DeviceDao dao;
	
	@Override
	public List<Device> getList() {
		return dao.selectList();
	}

	@Override
	public List<Device> getDeviceDetail(String deviceCode) {
		return dao.selectDeviceDetail(deviceCode);
	}
}
