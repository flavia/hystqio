/*
    Copyright (C) 2010 Viral Patel    
    Copyright (C) 2011 9Apps.net

    This file is part of hystqio.

    hystqio is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    hystqio is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with hystqio. If not, see <http://www.gnu.org/licenses/>.
*/

package net.nineapps.hystqio.controller;

import java.sql.Date;

import net.nineapps.hystqio.model.Link;
import net.nineapps.hystqio.util.HibernateUtil;
import net.nineapps.hystqio.util.HystqioUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

/**
 * Struts controller which uses Hibernate to persist
 * the links in a relational database.
 *
 */
public class HibernateLinkDAO extends HibernateUtil implements LinkDAO {

	private static Log log = LogFactory.getLog(HibernateLinkDAO.class);

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
			link.setShortCode(generateShortcode(session));
			link.setClicks(new Long(0));
			link.setCreated(new Date(new java.util.Date().getTime()));
			session.save(link);
		}
		session.getTransaction().commit();
		return link;
	}
	
	public void incrementClicks(Link link) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("from Link where url = :url");
		query.setString("url", link.getUrl());

		Link oldLink = (Link) query.uniqueResult();
		oldLink.setClicks(oldLink.getClicks() + 1);
		
		session.getTransaction().commit();
	}

	private boolean existsShortcode(String shortcode, Session session) {
		Query query = session.createQuery("from Link where shortcode = :code");
		query.setString("code", shortcode);
		Link link = (Link) query.uniqueResult();
		return (link != null);
	}

	/**
	 * Generate a short url which has not been assigned to any URL yet.
	 * Or at least try 10 times :)
	 */
	public String generateShortcode(Session session) {
		String shortcode;
		int attempts = 0;
		do {
			shortcode = HystqioUtils.generateShortCode();
			attempts++;
			// check if the shortcode doesn't already exist
		} while (existsShortcode(shortcode, session) && attempts < 10);
		if (attempts > 1) {
			log.error("WARNING: " +attempts+ " attempts to create a shortcode which is not taken.");
		}
		return shortcode;
	}

}
