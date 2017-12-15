package com.tm.iot.model;

import java.io.Serializable;
import java.util.Date;
import com.tm.iot.model.Device;
import com.tm.iot.model.Sensor;

/**************************************
 *  Author : 이종석
 *  Date : 2017. 12
 *  IotData Data Transfer Object
 *************************************/

public class IotData implements Serializable {
	private long dataId;
	private int sensorCode;
	private double sensorValue;
	private int deviceCode;
	private int autoMode;
	private int deviceState;
	private Date regDate;
	
	public IotData() {
	}

	public IotData(int sensorCode, double sensorValue, int deviceCode, int autoMode, int deviceState, Date regDate) {
		super();
		this.sensorCode = sensorCode;
		this.sensorValue = sensorValue;
		this.deviceCode = deviceCode;
		this.autoMode = autoMode;
		this.deviceState = deviceState;
		this.regDate = regDate;
	}

	public long getDataId() {
		return dataId;
	}

	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	public int getSensorCode() {
		return sensorCode;
	}

	public void setSensorCode(int sensorCode) {
		this.sensorCode = sensorCode;
	}

	public double getSensorValue() {
		return sensorValue;
	}

	public void setSensorValue(double sensorValue) {
		this.sensorValue = sensorValue;
	}

	public int getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
	}

	public int getAutoMode() {
		return autoMode;
	}

	public void setAutoMode(int autoMode) {
		this.autoMode = autoMode;
	}

	public int getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(int deviceState) {
		this.deviceState = deviceState;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "IotData [dataId=" + dataId + ", sensorCode=" + sensorCode + ", sensorValue=" + sensorValue
				+ ", deviceCode=" + deviceCode + ", autoMode=" + autoMode + ", deviceState=" + deviceState
				+ ", regDate=" + regDate + "]";
	}
	
}
