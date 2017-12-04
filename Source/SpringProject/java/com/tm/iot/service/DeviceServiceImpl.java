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

	@Override
	public boolean insert(Device device) {
		int result = dao.insert(device);
		return result == 1;
	}
}
