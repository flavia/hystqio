package net.nineapps.hystqio.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import net.nineapps.hystqio.model.Link;
import net.nineapps.hystqio.util.HystqioUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class SimpleDBLinkController implements LinkController {

	private static final String LINKS = "links";
	private static final String URL_ATTRIBUTE = "url";
	private static final String SHORTCODE_ATTRIBUTE = "shortcode";
	private static final String CLICKS_ATTRIBUTE = "clicks";
	private static final String CREATED_ATTRIBUTE = "created";
	
	public Link get(String shortCode) {

		AmazonSimpleDB simpleDB = simpleDBService();

		Link link = new Link();
		
		GetAttributesResult result = simpleDB.getAttributes(new GetAttributesRequest(LINKS, shortCode));
		List<Attribute> attributes = result.getAttributes();
		Iterator<Attribute> iter = attributes.iterator();
		while (iter.hasNext()) {
			Attribute attribute = iter.next();
			if (URL_ATTRIBUTE.equals(attribute.getName())) {
				link.setUrl(attribute.getValue());
			} else if (SHORTCODE_ATTRIBUTE.equals(attribute.getName())) {
				link.setShortCode(attribute.getValue());
			} else if (CLICKS_ATTRIBUTE.equals(attribute.getName())) {
				link.setClicks(Long.parseLong(attribute.getValue()));
			} else if (CREATED_ATTRIBUTE.equals(attribute.getName())) {
				link.setCreated(string2Date(attribute));
			}
		}
		
		return link;
	}

	private Date string2Date(Attribute attribute) {
		try {
			return new java.sql.Date(dateFormat().parse(attribute.getValue()).getTime());
		} catch (ParseException pe) {
			System.err.println(pe);
			throw new IllegalStateException(
					"ParseException while parsing the date stored in SimpleDB", pe);
		}
	}

	public Link add(Link link) {

		SimpleDateFormat format = dateFormat();
		
		// TODO put this in a singleton?
		AmazonSimpleDB simpleDB = simpleDBService();

		// TODO clicks are never updated, also not with Hibernate
		
		// Check if the URL has already been shortened
		// by doing a consistent read
		// TODO clicks i don't need i think
		SelectRequest request = new SelectRequest(
				"select shortcode, clicks from links where url = '" +link.getUrl()+ "'", true);
		
		SelectResult result = simpleDB.select(request);

		// TODO no deber’a cambiar los clicks ac‡, s—lo ponerlos en 0 si arranca
		// los clicks cambian cuando el usuario va a ese link...
		long clicks = 0;
		String shortcode = null;
		
		// if so, get the number of clicks and the short URL
		if (result.getItems().size() > 0) {
			// we assume there is only one item
			Item item = result.getItems().get(0);
			Iterator<Attribute> iter = item.getAttributes().iterator();
			while (iter.hasNext()) {
				Attribute attribute = iter.next();
				if (SHORTCODE_ATTRIBUTE.equals(attribute.getName())) {
					shortcode = attribute.getValue();
				} else if (CLICKS_ATTRIBUTE.equals(attribute.getName())) {
					clicks = Long.parseLong(attribute.getValue());
				}
			}
		}
		
		if (shortcode == null) {
			shortcode = generateShortcode();
		}
		
		link.setClicks(clicks + 1);
		link.setShortCode(shortcode);
		
		List<ReplaceableAttribute> attribs = new ArrayList<ReplaceableAttribute>();
		attribs.add(new ReplaceableAttribute(URL_ATTRIBUTE, link.getUrl(), true));
		attribs.add(new ReplaceableAttribute(SHORTCODE_ATTRIBUTE, link.getShortCode(), true));
		attribs.add(new ReplaceableAttribute(CREATED_ATTRIBUTE, format.format(new java.util.Date()), true));
		// TODO use "Expected" to make sure we don't overwrite the clicks
		attribs.add(new ReplaceableAttribute(CLICKS_ATTRIBUTE, link.getClicks().toString(), true));
		
		simpleDB.putAttributes(new PutAttributesRequest(LINKS, link.getShortCode(), attribs));
		
		return link;
	}

	private String generateShortcode() {
		String shortcode;
		int attempts = 0;
		do {
			shortcode = HystqioUtils.generateShortCode();
			attempts++;
			// check if the shortcode doesn't already exist
		} while (existsShortcode(shortcode) && attempts < 10);
		if (attempts > 1) {
			System.err.println("WARNING: " +attempts+ " attempts to create a shortcode which is not taken.");
		}
		return shortcode;
	}

	private boolean existsShortcode(String shortcode) {
		AmazonSimpleDB simpleDB = simpleDBService();

		GetAttributesResult result = simpleDB.getAttributes(new GetAttributesRequest(LINKS, shortcode));
		
		// if attributes is null, then the item doesn't exist
		return result.getAttributes() == null;
	}
	
	private SimpleDateFormat dateFormat() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format;
	}

	private AmazonSimpleDB simpleDBService() {
		ResourceBundle bundle = ResourceBundle.getBundle ("aws");
		
		AWSCredentials credentials = new BasicAWSCredentials(
				bundle.getString("accessKey"), bundle.getString("secretKey"));
		
		AmazonSimpleDB simpleDB = new AmazonSimpleDBClient(credentials);
		return simpleDB;
	}
	
	// TODO details of a link (details.css) are never shown... how are they supposed to be shown?

}
