package com.tm.iot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tm.iot.model.Device;

public interface DeviceService {
	List<Device> getList();
	List<Device> getDeviceDetail(String deviceCode);
}
