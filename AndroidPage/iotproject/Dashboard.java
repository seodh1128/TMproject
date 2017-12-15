package com.yangdoll.android.iotproject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by student on 2017-12-12.
 */

public class Dashboard {
    private long dataId;
    private int sensorCode;
    private double sensorValue;
    private int deviceCode;
    private int autoMode;
    private int deviceState;
    private Date regDate;
    private String deviceName;
    private int pinNumber;
    private String sensorType;
    private String sensorName;
    private String sensorUnits;


    public Dashboard() {}

    public Dashboard(int sensorCode, double sensorValue, int deviceCode, int autoMode, int deviceState, Date regDate,
                     String deviceName, int pinNumber, String sensorType, String sensorName, String sensorUnits) {
        super();
        this.sensorCode = sensorCode;
        this.sensorValue = sensorValue;
        this.deviceCode = deviceCode;
        this.autoMode = autoMode;
        this.deviceState = deviceState;
        this.regDate = regDate;
        this.deviceName = deviceName;
        this.pinNumber = pinNumber;
        this.sensorType = sensorType;
        this.sensorName = sensorName;
        this.sensorUnits = sensorUnits;
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
        return "Dashboard [dataId=" + dataId + ", sensorCode=" + sensorCode + ", sensorValue=" + sensorValue
                + ", deviceCode=" + deviceCode + ", autoMode=" + autoMode + ", deviceState=" + deviceState
                + ", regDate=" + newDate(regDate) + ", deviceName=" + deviceName + ", pinNumber=" + pinNumber + ", sensorType="
                + sensorType + ", sensorName=" + sensorName + ", sensorUnits=" + sensorUnits + "]";
    }

    public void setSensorValue(String sensorValue) {
    }
    public String newDate(Date now) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd E HH:mm:ss");
        // System.out.println(format.format(now));
        return format.format(now);
    }
}