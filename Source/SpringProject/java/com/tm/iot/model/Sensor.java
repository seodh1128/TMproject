package com.tm.iot.model;

public class Sensor {
	private int sensorCode;
	private String sensorType;
	private String sensorName;
	private String sensorUnits;
	
	public Sensor() {}
	
	public Sensor(int sensorCode, String sensorType, String sensorName, String sensorUnits) {
		super();
		this.sensorCode = sensorCode;
		this.sensorType = sensorType;
		this.sensorName = sensorName;
		this.sensorUnits = sensorUnits;
	}

	public int getSensorCode() {
		return sensorCode;
	}

	public void setSensorCode(int sensorCode) {
		this.sensorCode = sensorCode;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getSensorUnits() {
		return sensorUnits;
	}

	public void setSensorUnits(String sensorUnits) {
		this.sensorUnits = sensorUnits;
	}

	@Override
	public String toString() {
		return "Sensor [sensorCode=" + sensorCode + ", sensorType=" + sensorType + ", sensorName=" + sensorName
				+ ", sensorUnits=" + sensorUnits + "]";
	}
}
