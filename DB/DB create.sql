CREATE TABLE SENSOR (
    SENSOR_CODE NUMBER,
    SENSOR_TYPE VARCHAR2(20) NOT NULL,
    SENSOR_NAME VARCHAR2(20) NOT NULL,
    SENSOR_UNITS VARCHAR2(20) NOT NULL,
    CONSTRAINT PK_SENSOR PRIMARY KEY(SENSOR_CODE)
);

CREATE TABLE DEVICE (
    DEVICE_CODE NUMBER,
    DEVICE_NAME VARCHAR2(30) NOT NULL,
    PIN_NUMBER NUMBER NOT NULL,
    CONSTRAINT PK_DEVICE PRIMARY KEY(DEVICE_CODE)
);

CREATE TABLE IOT_DATA (
    DATA_ID NUMBER,
    SENSOR_CODE NUMBER,
    SENSOR_VALUE NUMBER NOT NULL,
    DEVICE_CODE NUMBER,
    AUTO_MODE NUMBER NOT NULL,
    DEVICE_STATE NUMBER NOT NULL,
    REG_DATE DATE NOT NULL,
    CONSTRAINT PK_IOT_DATA PRIMARY KEY(DATA_ID),
    CONSTRAINT FK_IOT_DATA_SENSOR_CODE FOREIGN KEY(SENSOR_CODE) REFERENCES SENSOR(SENSOR_CODE),
    CONSTRAINT FK_IOT_DATA_DEVICE_CODE FOREIGN KEY(DEVICE_CODE) REFERENCES DEVICE(DEVICE_CODE)
);
  
   
CREATE TABLE IOT_ACCOUNT (
    USER_ID VARCHAR2(30),
    PASSWORD VARCHAR2(40) NOT NULL,
    CONSTRAINT PK_ACCOUNT PRIMARY KEY(USER_ID)
);


CREATE TABLE CONTROLL (
    CONTROLL_ID NUMBER,
    DEVICE_CODE NUMBER NOT NULL,
    REG_DATE DATE NOT NULL,
    CONSTRAINT PK_CONTROLL PRIMARY KEY(CONTROLL_ID)
);
