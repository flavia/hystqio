package net.viralpatel.shorty.controller;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import net.viralpatel.shorty.model.Link;
import net.viralpatel.shorty.util.HibernateUtil;
import net.viralpatel.shorty.util.ShortyUtil;

public class LinkController extends HibernateUtil {


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
			link.setShortCode(ShortyUtil.base48Encode(link.getId()));
			session.save(link);
		}
		session.getTransaction().commit();
		return link;
	}
}
