package com.qait.happyhours.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.qait.happyhours.dao.DealDao;
import com.qait.happyhours.domain.Deal;
import com.qait.happyhours.domain.User;

@Repository("dealDao")
public class DealDaoImpl extends GenericDaoImpl<User, Long> implements DealDao {

	@Override
	public List<Deal> getMatchingDealsBySearchStr(String searchStr) {

		List<Deal> dealList = new ArrayList<Deal>();
		Session session = null;
		try {
			session = getSessionFactory().openSession();

			String queryString = "select distinct d from Deal d join fetch d.dealOffersList ol where d.title like '%"
					+ searchStr
					+ "%' or ol.offerName like '%"
					+ searchStr
					+ "%'";

			Query query = session.createQuery(queryString);
			dealList = query.list();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			session.close();

		}
		return dealList;
	}

	@Override
	public List<Deal> getAllActiveDealsList() {
		
		List<Deal> dealList = new ArrayList<Deal>();
		Session session = null;
		try {
			
			session = getSessionFactory().openSession();
			String queryString = "from Deal d where d.startDate <= :currentDate and d.endDate >= :currentDate";
			Query query = session.createQuery(queryString);
			query.setTimestamp("currentDate", new Date());
			dealList = query.list();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			session.close();

		}
		return dealList;
	}

	@Override
	public boolean saveDeal(Deal deal) {
		boolean dealSaved = true;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(deal);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			dealSaved = false;
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return dealSaved;
	}
}
