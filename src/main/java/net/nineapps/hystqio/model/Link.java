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
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with hystqio. If not, see <http://www.gnu.org/licenses/>.
*/

package net.nineapps.hystqio.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LINKS")
public class Link implements Serializable{
	
	private static final long serialVersionUID = -8767337896773261247L;

	private Long id;
	private String shortCode;
	private String url;
	private Long clicks;
	private Date created;

	@Id
	@GeneratedValue
	@Column(name="id")
	// with SimpleDB we will just use the shortcode as item name
	// so we won't use this id
	public Long getId() {
		return id;
	}
	@Column(name = "shortcode")
	public String getShortCode() {
		return shortCode;
	}
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}
	@Column(name = "url")
	public String getUrl() {
		return url;
	}
	@Column(name = "clicks")
	public Long getClicks() {
		return clicks;
	}
	public void setClicks(Long clicks) {
		this.clicks = clicks;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
