package com.example.seodh.iothomegateway;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SeoDH on 2017-12-07.
 */

public class IotData implements Serializable {

    // 변수 : DB 컬럼(IoT시스템 전반에서 송/수신할 Data)을 기준으로 선정 - Web/Arduino/Android 공통요소
    private long dataId;
    private String sensorCode;
    private double sensorValue;
    private String deviceCode;
    private double deviceState;
    private double autoMode;
    private Date regTime;

    public IotData(String sensorCode, double sensorValue, String deviceCode, double deviceState, double autoMode, Date regTime) {
        this.sensorCode = sensorCode;
        this.sensorValue = sensorValue;
        this.deviceCode = deviceCode;
        this.deviceState = deviceState;
        this.autoMode = autoMode;
        this.regTime = regTime;
    }

    @Override
    public String toString() {
        return "IotData{" +
                "dataId=" + dataId +
                ", sensorCode='" + sensorCode + '\'' +
                ", sensorValue=" + sensorValue +
                ", deviceCode='" + deviceCode + '\'' +
                ", deviceState=" + deviceState +
                ", autoMode=" + autoMode +
                ", regTime=" + regTime +
                '}';
    }

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(double sensorValue) {
        this.sensorValue = sensorValue;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public double getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(double deviceState) {
        this.deviceState = deviceState;
    }

    public double getAutoMode() {
        return autoMode;
    }

    public void setAutoMode(double autoMode) {
        this.autoMode = autoMode;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }
}
