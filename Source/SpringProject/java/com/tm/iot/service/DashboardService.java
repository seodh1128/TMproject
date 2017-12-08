package com.tm.iot.service;

import java.util.List;

import com.tm.iot.model.Dashboard;;

public interface DashboardService {
	List<Dashboard> getList();
	List<Dashboard> getDetailList(int deviceCode);
}
