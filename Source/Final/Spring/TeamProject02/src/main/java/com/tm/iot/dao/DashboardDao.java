package com.tm.iot.dao;

import java.util.List;

import com.tm.iot.model.Dashboard;

public interface DashboardDao {

	List<Dashboard> selectList();
	List<Dashboard> selectDetailList(Dashboard dashboard);
}
