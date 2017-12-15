package com.tm.iot.service;

import java.util.List;

import com.tm.iot.model.Dashboard;;

/**************************************
 *  Title : Dashboard Service Interface
 *  Author : 
 *  Date : 2017. 12
 *************************************/

public interface DashboardService {
	List<Dashboard> getList();
	List<Dashboard> getDetailList(Dashboard dashboard);
}
