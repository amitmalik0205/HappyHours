package com.qait.happyhours.service;

import com.qait.happyhours.domain.DealOffers;

public interface OfferService {

	public boolean saveOffer(DealOffers offer);
	
	public DealOffers getOffer(Long offerID);
}
