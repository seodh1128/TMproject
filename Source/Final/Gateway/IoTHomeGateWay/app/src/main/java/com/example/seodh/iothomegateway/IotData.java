package com.example.seodh.iothomegateway;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SeoDH on 2017-12-07.
 */

public class IotData implements Serializable {

    // 변수 : DB 컬럼(IoT시스템 전반에서 송/수신할 Data)을 기준으로 선정 - Web/Arduino/Android 공통요소
    private long dataId;
    private int sensorCode;
    private double sensorValue;
    private int deviceCode;
    private int deviceState;
    private int autoMode;
    private Date regDate;

    public IotData(int sensorCode, double sensorValue, int deviceCode, int deviceState, int autoMode, Date regDate) {
        this.sensorCode = sensorCode;
        this.sensorValue = sensorValue;
        this.deviceCode = deviceCode;
        this.deviceState = deviceState;
        this.autoMode = autoMode;
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

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public int getAutoMode() {
        return autoMode;
    }

    public void setAutoMode(int autoMode) {
        this.autoMode = autoMode;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "IotData{" +
                "dataId=" + dataId +
                ", sensorCode=" + sensorCode +
                ", sensorValue=" + sensorValue +
                ", deviceCode=" + deviceCode +
                ", deviceState=" + deviceState +
                ", autoMode=" + autoMode +
                ", regDate=" + regDate +
                '}';
    }
}
