package com.tm.iot.model;

import java.io.Serializable;
import java.util.Date;

public class Sensor implements Serializable {
	
	private long sensorId;
	private String sensorType;
	private String sensorCode;
	private double sensorValue;
	private Date sensorTime;
	
	public Sensor() {}

	public Sensor(String sensorType, String sensorCode, int pinNumber, double sensorValue, Date sensorTime) {
		super();
		this.sensorType = sensorType;
		this.sensorCode = sensorCode;
		this.sensorValue = sensorValue;
		this.sensorTime = sensorTime;
	}

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public String getSensorCode() {
		return sensorCode;
	}

	public void setSensorName(String sensorName) {
		this.sensorCode = sensorName;
	}

	public double getSensorValue() {
		return sensorValue;
	}

	public void setSensorValue(double sensorValue) {
		this.sensorValue = sensorValue;
	}

	public Date getSensorTime() {
		return sensorTime;
	}

	public void setSensorTime(Date sensorTime) {
		this.sensorTime = sensorTime;
	}

	@Override
	public String toString() {
		return "Sensor [sensorId=" + sensorId + ", sensorType=" + sensorType + ", sensorCode=" + sensorCode
				+ ", sensorValue=" + sensorValue + ", sensorTime=" + sensorTime + "]";
	}
}
