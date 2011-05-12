package net.viralpatel.shorty.model;

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
