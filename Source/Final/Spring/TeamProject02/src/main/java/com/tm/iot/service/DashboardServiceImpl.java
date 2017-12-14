package com.tm.iot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.iot.dao.DashboardDao;
import com.tm.iot.model.Dashboard;

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
