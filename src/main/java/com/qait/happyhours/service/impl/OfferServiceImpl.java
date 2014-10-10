package com.qait.happyhours.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.qait.happyhours.dao.OfferDao;
import com.qait.happyhours.domain.DealOffers;
import com.qait.happyhours.service.OfferService;

@org.springframework.stereotype.Service("offerService")
public class OfferServiceImpl implements OfferService {

	@Autowired
	private OfferDao offerDao;
	
	@Override
	public boolean saveOffer(DealOffers offer) {
		return offerDao.saveOffer(offer);
	}

	@Override
	public DealOffers getOffer(Long offerID) {
		return offerDao.getOffer(offerID);
	}

}
