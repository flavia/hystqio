package net.nineapps.hystqio.controller;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import net.nineapps.hystqio.model.Link;
import net.nineapps.hystqio.util.HibernateUtil;
import net.nineapps.hystqio.util.ShortyUtils;

public class HibernateLinkController extends HibernateUtil implements LinkController {


	public Link get(String shortCode) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Link where shortcode = :shortcode");
		query.setString("shortcode", shortCode);
		Link link = (Link) query.uniqueResult();
		if(null != link) {
			link.setClicks(link.getClicks());
			session.save(link);
		}
		session.getTransaction().commit();
	
		return link;
	
	}
	
	public Link add(Link link) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Link where url = :url");
		query.setString("url", link.getUrl());
		Link oldLink = (Link) query.uniqueResult();
		if(null != oldLink)
			return oldLink;
		
		session.save(link);
		if(null == link.getShortCode()) {
			link.setShortCode(ShortyUtils.base48Encode(link.getId()));
			session.save(link);
		}
		session.getTransaction().commit();
		return link;
	}
}
