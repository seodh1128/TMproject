package com.tm.iot.model;

import java.io.Serializable;
import java.util.Date;

public class Device implements Serializable {
	
	private long deviceId;
	private String deviceType;
	private String deviceCode;
	private double deviceValue;
	private Date deviceTime;
	
	public Device() {}

	public Device(String deviceType, String deviceCode, double deviceValue, Date deviceTime) {
		super();
		this.deviceType = deviceType;
		this.deviceCode = deviceCode;
		this.deviceValue = deviceValue;
		this.deviceTime = deviceTime;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceName(String deviceName) {
		this.deviceCode = deviceName;
	}

	public double getDeviceValue() {
		return deviceValue;
	}

	public void setDeviceValue(double deviceValue) {
		this.deviceValue = deviceValue;
	}

	public Date getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(Date deviceTime) {
		this.deviceTime = deviceTime;
	}

	@Override
	public String toString() {
		return "Device [deviceId=" + deviceId + ", deviceType=" + deviceType + ", deviceCode=" + deviceCode
				+ ", deviceValue=" + deviceValue + ", deviceTime=" + deviceTime + "]";
	}
}
