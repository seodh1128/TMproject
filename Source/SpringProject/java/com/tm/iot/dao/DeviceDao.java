package com.tm.iot.dao;

import java.util.List;

import com.tm.iot.model.Device;

public interface DeviceDao {
	List<Device> selectList();
	List<Device> selectDeviceDetail(String deviceCode);
}
