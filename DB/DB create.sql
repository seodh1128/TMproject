create table sensor (
    sensor_code number,
    sensor_type varchar2(20) not null,
    sensor_name varchar2(20) not null,
    sensor_value number not null,
    sensor_units varchar2(20) not null,
    reg_date date not null,
    constraint pk_sensor primary key(sensor_code)
);

create table device (
    device_name varchar2(30),
    sensor_code number,
    pin_number number,
    constraint pk_device primary key(device_name),
    constraint fk_device foreign key(sensor_code) references sensor(sensor_code)
);

create table iot_data (
    data_id number,
    sensor_code number,
    sensor_value number,
    device_code number,
    auto_mode number,
    device_state number,
    reg_date date,
    constraint pk_iot_data primary key(data_id),
    constraint fk_iot_data_sensor_code foreign key(sensor_code) references sensor(sensor_code),
    constraint fk_iot_data_device_code foreign key(device_code) references device(device_code)
);

create table account (
    user_id varchar2(30),
    password varchar2(40),
    constraint pk_account primary key(user_id)
);

create table controll (
    ctrl_id number,
    device_code number,
    device_state number,
    auto_mode number,
    reg_date date,
    constraint pk_controll primary key(ctrl_id),
    constraint fk_device_code foreign key(device_code) references device(device_code),
    constraint fk_device_state foreign key(device_state) references device(device_state),
    constraint fk_auto_mode foreign key(auto_mode) references device(auto_mode)
);

    
    
