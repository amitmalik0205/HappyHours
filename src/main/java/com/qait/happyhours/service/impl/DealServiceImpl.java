package com.qait.happyhours.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qait.happyhours.dao.DealDao;
import com.qait.happyhours.domain.Deal;
import com.qait.happyhours.service.DealService;

@Service("dealService")
public class DealServiceImpl implements DealService {

	@Autowired
	private DealDao dealDao;

	@Override
	public List<Deal> getMatchingDealsBySearchStr(String searchStr) {
		return dealDao.getMatchingDealsBySearchStr(searchStr);
	}

	@Override
	public List<Deal> getAllActiveDealsList() {
		return dealDao.getAllActiveDealsList();
	}

	@Override
	public boolean saveDeal(Deal deal) {
		// TODO Auto-generated method stub
		return dealDao.saveDeal(deal);
	}

}