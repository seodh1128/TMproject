package com.tm.iot.dao;

import java.util.List;

import com.tm.iot.model.Dashboard;

/**************************************
 *  Author : 
 *  Date : 2017. 12
 *  Dashboard Data Access Object
 *************************************/

public interface DashboardDao {

	List<Dashboard> selectList();
	List<Dashboard> selectDetailList(Dashboard dashboard);
}
