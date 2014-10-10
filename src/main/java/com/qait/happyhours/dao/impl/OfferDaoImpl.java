package com.qait.happyhours.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.qait.happyhours.dao.OfferDao;
import com.qait.happyhours.domain.DealOffers;
import com.qait.happyhours.util.HappyHoursUtil;

@Repository("offerDao")
public class OfferDaoImpl extends GenericDaoImpl<DealOffers, Long> implements OfferDao  {

	private static final Logger logger = Logger.getLogger(OfferDaoImpl.class);
	
	@Override
	public boolean saveOffer(DealOffers offer) {
		boolean offerSaved = true;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(offer);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			offerSaved = false;
			e.printStackTrace();
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.flush();
			session.close();
		}
		return offerSaved;
	}

	@Override
	public DealOffers getOffer(Long offerID) {
		DealOffers offer = null;
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			offer = (DealOffers)session.get(DealOffers.class, offerID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.close();
		}
		return offer;
	}

}
