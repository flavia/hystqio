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

package net.nineapps.hystqio.view;


import javax.servlet.http.HttpServletRequest;

import net.nineapps.hystqio.controller.HibernateLinkController;
import net.nineapps.hystqio.controller.LinkController;
import net.nineapps.hystqio.model.Link;
import net.nineapps.hystqio.util.ShortyUtils;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class LinkAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private final static String DETAIL = "detail";
	private String url;
	private Link link;
	
	private LinkController linkController;
	
	private HttpServletRequest request;
	
	public LinkAction() {
		linkController = new HibernateLinkController();
	}
	public String add() {
		link = new Link();
		link.setUrl(this.url);
		link = linkController.add(link);
		
		return SUCCESS;
	}
	public String get() {
		
		String uri = request.getRequestURI();
		
		uri = ShortyUtils.getShortCodeFromURL(uri);

		if(uri.charAt(uri.length()-1) == '+') {
			uri = uri.substring(0, uri.length()-1);
			this.link = this.linkController.get(uri);
			return DETAIL;
		}
		
		this.link = this.linkController.get(uri);
		if(null == this.link) {
			addActionError(getText("error.url.unavailable"));
			return INPUT;
		} else {
			
			setUrl(link.getUrl());
			return SUCCESS;
		}
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
