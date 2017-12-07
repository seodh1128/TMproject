

create table sensor (
    sensor_code number,
    sensor_type varchar2(20) not null,
    sensor_name varchar2(20) not null,
    sensor_units varchar2(20) not null,
    reg_date date not null,
    constraint pk_sensor primary key(sensor_code)
);

create table device (
    DEVICE_code number,
    device_name varchar2(30) not null,
    pin_number number not null,
    constraint pk_device primary key(device_CODE)
);

create table iot_data (
    data_id number,
    sensor_code number,
    sensor_value number not null,
    device_code number,
    auto_mode number not null,
    device_state number not null,
    reg_date date not null,
    constraint pk_iot_data primary key(data_id),
    constraint fk_iot_data_sensor_code foreign key(sensor_code) references sensor(sensor_code),
    constraint fk_iot_data_device_code foreign key(device_code) references device(device_code)
);
  
   
create table iot_account (
    user_id varchar2(30),
    password varchar2(40) not null,
    constraint pk_account primary key(user_id)
);

create table controll (
    controll_id number,
    device_code number not null,
    reg_date date not null,
    constraint pk_controll primary key(controll_id)
);
