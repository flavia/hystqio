package net.nineapps.hystqio.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.nineapps.hystqio.model.Link;
import net.nineapps.hystqio.util.HystqioUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.AmazonServiceException;
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
import com.amazonaws.services.simpledb.model.UpdateCondition;

/**
 * Struts data access object which uses SimpleDB
 * to persist the links.
 * 
 * @author flavia
 *
 */
public class SimpleDBLinkDAO implements LinkDAO {

	private static final String LINKS = "links";
	private static final String URL_ATTRIBUTE = "url";
	private static final String SHORTCODE_ATTRIBUTE = "shortcode";
	private static final String CLICKS_ATTRIBUTE = "clicks";
	private static final String CREATED_ATTRIBUTE = "created";

	private static Log log = LogFactory.getLog(SimpleDBLinkDAO.class);
	
	private AmazonSimpleDB simpleDB = null;
	
	public Link get(String shortCode) {

		AmazonSimpleDB simpleDB = getSimpleDBService();

		Link link = new Link();
		
		GetAttributesResult result = simpleDB.getAttributes(new GetAttributesRequest(LINKS, shortCode));
		for (Attribute attribute : result.getAttributes()) {
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

	public void incrementClicks(Link link) {

		AmazonSimpleDB simpleDB = getSimpleDBService();

		List<ReplaceableAttribute> attribs = new ArrayList<ReplaceableAttribute>();

		boolean clicksOverwritten;
		int attempts = 0;
		
		do {
			try {

				long oldClicks = numberOfClicks(link, simpleDB);
	
				// add 1 to number of clicks
				attribs.add(new ReplaceableAttribute(CLICKS_ATTRIBUTE, "" + (oldClicks + 1), true));

				// use UpdateCondition so we only update 
				// if the previous value for clicks was the same that we just retrieved before
				simpleDB.putAttributes(new PutAttributesRequest(LINKS, link.getShortCode(), attribs, 
						new UpdateCondition(CLICKS_ATTRIBUTE, "" + oldClicks, true)));

				clicksOverwritten = false;
				
			} catch (AmazonServiceException e) {
				log.error(e.getErrorCode());
				if ("ConditionalCheckFailed".equals(e.getErrorCode())) {
					clicksOverwritten = true;
					attempts++;
				} else{
					throw e;
				}
			}
		} while (clicksOverwritten && attempts < 10);

		if (clicksOverwritten && attempts >= 10) {
			log.error("WARNING: Could not update the number of clicks.");
		}
		
	}

	private long numberOfClicks(Link link, AmazonSimpleDB simpleDB) {
		long oldClicks = 0;
		
		// get the current number of clicks
		// do a consistent read to get the latest written value
		GetAttributesRequest request = new GetAttributesRequest(LINKS, link.getShortCode());
		request.setConsistentRead(true);
		GetAttributesResult result = simpleDB.getAttributes(request);
		
		for (Attribute attribute: result.getAttributes()) {
			if (CLICKS_ATTRIBUTE.equals(attribute.getName())) {
				oldClicks = Long.parseLong(attribute.getValue());
				break;
			}
		}
		return oldClicks;
	}
	
	public Link add(Link link) {

		SimpleDateFormat format = dateFormat();
		
		AmazonSimpleDB simpleDB = getSimpleDBService();

		// Check if the URL has already been shortened
		// by doing a consistent read
		SelectRequest request = new SelectRequest(
				"select shortcode from links where url = '" +link.getUrl()+ "'", true);
		
		SelectResult result = simpleDB.select(request);

		String shortcode = null;
		
		// if so, get the already existing short URL
		if (result.getItems().size() > 0) {
			// we assume there is only one item
			Item item = result.getItems().get(0);
			for (Attribute attribute : item.getAttributes()) {
				if (SHORTCODE_ATTRIBUTE.equals(attribute.getName())) {
					shortcode = attribute.getValue();
					break;
				}
			}
		}
		
		// if the url was not yet in the database, generate a new short url
		if (shortcode == null) {
			shortcode = generateShortcode();
		}
		
		link.setShortCode(shortcode);
		
		List<ReplaceableAttribute> attribs = new ArrayList<ReplaceableAttribute>();
		attribs.add(new ReplaceableAttribute(URL_ATTRIBUTE, link.getUrl(), true));
		attribs.add(new ReplaceableAttribute(SHORTCODE_ATTRIBUTE, link.getShortCode(), true));
		attribs.add(new ReplaceableAttribute(CREATED_ATTRIBUTE, format.format(new java.util.Date()), true));
		attribs.add(new ReplaceableAttribute(CLICKS_ATTRIBUTE, "0", true));
		
		simpleDB.putAttributes(new PutAttributesRequest(LINKS, link.getShortCode(), attribs));
		
		return link;
	}

	private java.sql.Date string2Date(Attribute attribute) {
		try {
			return new java.sql.Date(dateFormat().parse(attribute.getValue()).getTime());
		} catch (ParseException pe) {
			log.error(pe);
			throw new IllegalStateException(
					"ParseException while parsing the date stored in SimpleDB: " + attribute.getValue(), pe);
		}
	}

	/**
	 * Generate a short url which has not been assigned to any URL yet.
	 * Or at least try 10 times :)
	 */
	private String generateShortcode() {
		String shortcode;
		int attempts = 0;
		do {
			shortcode = HystqioUtils.generateShortCode();
			attempts++;
			// check if the shortcode doesn't already exist
		} while (existsShortcode(shortcode) && attempts < 10);
		if (attempts > 1) {
			log.error("WARNING: " +attempts+ " attempts to create a shortcode which is not taken.");
		}
		return shortcode;
	}

	public boolean existsShortcode(String shortcode) {
		AmazonSimpleDB simpleDB = getSimpleDBService();

		GetAttributesResult result = simpleDB.getAttributes(new GetAttributesRequest(LINKS, shortcode));
		
		// if attributes is null, then the item doesn't exist
		return result.getAttributes() != null;
	}
	
	private SimpleDateFormat dateFormat() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format;
	}

	private AmazonSimpleDB getSimpleDBService() {
		if (simpleDB == null) {
			initSimpleDBService();
		}
		return simpleDB;
	}

	private void initSimpleDBService() {
		ResourceBundle bundle = ResourceBundle.getBundle ("aws");
		
		AWSCredentials credentials = new BasicAWSCredentials(
				bundle.getString("accessKey"), bundle.getString("secretKey"));
		
		simpleDB = new AmazonSimpleDBClient(credentials);
	}
	
}
