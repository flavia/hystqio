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
