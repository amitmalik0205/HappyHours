package com.qait.happyhours.dao;

import com.qait.happyhours.domain.DealOffers;

public interface OfferDao {

	public boolean saveOffer(DealOffers offer);
	
	public DealOffers getOffer(Long offerID);

}
