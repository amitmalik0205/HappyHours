package com.qait.happyhours.service;

import java.util.List;

import com.qait.happyhours.domain.Deal;

public interface DealService {

	List<Deal> getMatchingDealsBySearchStr(String searchStr);

	List<Deal> getAllActiveDealsList();

	boolean saveDeal(Deal deal);

}