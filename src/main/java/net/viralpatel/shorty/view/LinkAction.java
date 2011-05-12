package net.viralpatel.shorty.view;


import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import net.viralpatel.shorty.controller.LinkController;
import net.viralpatel.shorty.model.Link;
import net.viralpatel.shorty.util.ShortyUtil;

import com.opensymphony.xwork2.ActionSupport;

public class LinkAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private final static String DETAIL = "detail";
	private String url;
	private Link link;
	
	private LinkController linkController;
	
	private HttpServletRequest request;
	
	public LinkAction() {
		linkController = new LinkController();
	}
	public String add() {
		link = new Link();
		link.setUrl(this.url);
		link = linkController.add(link);
		
		return SUCCESS;
	}
	public String get() {
		
		String uri = request.getRequestURI();
		
		uri = ShortyUtil.getShortCodeFromURL(uri);

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
