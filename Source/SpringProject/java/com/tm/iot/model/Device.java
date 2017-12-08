package com.tm.iot.model;

import java.io.Serializable;

public class Device implements Serializable {
	
	private int deviceCode;
	private String deviceName;
	private int pinNumber;
	
	public Device() {}
	
	public Device(int deviceCode, String deviceName, int pinNumber) {
		super();
		this.deviceCode = deviceCode;
		this.deviceName = deviceName;
		this.pinNumber = pinNumber;
	}

	public int getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getPinNumber() {
		return pinNumber;
	}

	public void setPinNumber(int pinNumber) {
		this.pinNumber = pinNumber;
	}

	@Override
	public String toString() {
		return "Device [deviceCode=" + deviceCode + ", deviceName=" + deviceName + ", pinNumber=" + pinNumber + "]";
	}
}
