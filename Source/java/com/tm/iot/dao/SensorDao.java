package com.tm.iot.dao;

import java.util.List;

import com.tm.iot.model.Sensor;

public interface SensorDao {
	List<Sensor> getList();
	List<Sensor> getDataList(String sensorCode);
}
