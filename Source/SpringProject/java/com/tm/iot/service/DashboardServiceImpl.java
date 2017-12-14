package com.tm.iot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.iot.dao.DashboardDao;
import com.tm.iot.model.Dashboard;

/**************************************
 *  Author : ������
 *  Date : 2017. 12
 *  Dashboard Data Transfer Object
 *  Dashboard Service Interface Implement
 *************************************/

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private DashboardDao dao;
	
	@Override
	public List<Dashboard> getList() {
		return dao.selectList();
	}

	@Override
	public List<Dashboard> getDetailList(Dashboard dashboard) {
		return dao.selectDetailList(dashboard);
	}

}
