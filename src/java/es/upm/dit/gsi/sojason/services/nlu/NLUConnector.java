package es.upm.dit.gsi.sojason.services.nlu;

import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_CURRENCY_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_DATES_DEPART_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_DATES_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_DATES_RETURN_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_DOMAINS_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_FROM_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_LOCATIONS_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_MAX_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_MIN_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_NUMBER_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_PRICE_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_TIME_DEPART_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_TIME_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_TIME_RETURN_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_TO_NODENAME;
import static es.upm.dit.gsi.sojason.services.nlu.NLUModel.JSON_TRAVEL_NODENAME;
import jason.asSyntax.Literal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import es.upm.dit.gsi.jason.utils.NotationUtils;
import es.upm.dit.gsi.sojason.beans.NLUTravel;
import es.upm.dit.gsi.sojason.services.WebServiceConnector;

/**
 * Project: Web40SOJason
 * Package: es.upm.dit.gsi.sojason.services.nlu
 * Class: NLUConnector
 *
 * @author Miguel Coronado (miguelcb@dit.upm.es)
 * @version Feb 27, 2012
 *
 */
public class NLUConnector implements WebServiceConnector{

	/** The url of the service */
	private String serviceUrl;
	/** */
	private Logger logger = Logger.getLogger("Web40SOJason." + NLUConnector.class.getName());
	
	
	/** Constructor */
	public NLUConnector(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	/**
	 * 
	 */
	public Collection<Literal> call(String... params) {
		
		/* Are parameters correct */
		if (!validateParams(params)){ 
			logger.info("Parameters are not valid:" + Arrays.toString(params));
			return null; 
		}
		
		try {
			// Prepare the request
			String urlRequest = prepareRequest(params[0], params[1]);
			
			URL url = new URL(urlRequest);
			URLConnection connection = url.openConnection();
			connection.connect();
		
			// Parse the data received (using Jackson lib)
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readValue(connection.getInputStream(), JsonNode.class); // src can be a File, URL, InputStream etc
			JsonNode travelNode = rootNode.with(JSON_DOMAINS_NODENAME).get(JSON_TRAVEL_NODENAME);
			
			NLUTravel travel = new NLUTravel();
			travel.setDepartureDate(travelNode.with(JSON_DATES_NODENAME).get(JSON_DATES_DEPART_NODENAME).getTextValue());
			travel.setReturnDate(travelNode.with(JSON_DATES_NODENAME).get(JSON_DATES_RETURN_NODENAME).getTextValue());
			
			travel.setCurrency(travelNode.with(JSON_PRICE_NODENAME).get(JSON_CURRENCY_NODENAME).getTextValue());
			travel.setPriceMax(travelNode.with(JSON_PRICE_NODENAME).get(JSON_MAX_NODENAME).getTextValue());
			travel.setPriceMin(travelNode.with(JSON_PRICE_NODENAME).get(JSON_MIN_NODENAME).getTextValue());
			
			travel.setLocationFrom(travelNode.with(JSON_LOCATIONS_NODENAME).get(JSON_FROM_NODENAME).getTextValue());
			travel.setLocationTo(travelNode.with(JSON_LOCATIONS_NODENAME).get(JSON_TO_NODENAME).getTextValue());
			
			travel.setNumber(travelNode.get(JSON_NUMBER_NODENAME).getTextValue());
			
			travel.setReturnTime(travelNode.with(JSON_TIME_NODENAME).get(JSON_TIME_RETURN_NODENAME).getTextValue());
			travel.setDepartureTime(travelNode.with(JSON_TIME_NODENAME).get(JSON_TIME_DEPART_NODENAME).getTextValue());
			
			travel.setQueryId(params[0]);
			
//			System.out.println(travel);
		
			return travel.toPercepts();
	
		} catch (MalformedURLException e) {
//			return CollectionUtils.wrapList("error(malformed_url, \"The given url is not valid\")");
			logger.info("MalformedURLException:" + e.getMessage()); return null;
		} catch (UnsupportedEncodingException e) {
//			return CollectionUtils.wrapList("error(undupported_encodig, \"The encoding given is not supported\")");
			logger.info("UnsupportedEncodingException:" + e.getMessage()); return null;
		} catch (IOException e) {
//			return CollectionUtils.wrapList("error(io_exception, \"Someio exception ocurr\")");
			logger.info("IOException:" + e.getMessage()); return null;
		}
		
	}

	/**
	 * This generates a String used as http GET request to access the service
	 * including the parameters given by the user
	 *  
	 * @param queryid
	 * @param message
	 * @return the url service string (utf-8 encoded)
	 * @throws UnsupportedEncodingException 
	 */
	String prepareRequest(String queryid, String message) throws UnsupportedEncodingException {
		String urlRequest = this.serviceUrl;
		urlRequest = urlRequest.concat("?text=");
		message = NotationUtils.removeQuotation(message);
		urlRequest = urlRequest.concat(URLEncoder.encode(message, "utf-8"));
//		urlRequest = urlRequest.concat(URLEncoder.encode("&query_id=", "utf-8"));
		urlRequest = urlRequest.concat("&query_id=");
		urlRequest = urlRequest.concat(URLEncoder.encode(queryid, "utf-8"));
		
		logger.info(urlRequest);
		return urlRequest ;
	}

	/**
	 * This validates the parameters received. The 
	 * {@link NLUConnector#call(String...)} method expects to receive two
	 * parameters of the nature and characteristics described below:
	 * 
	 * <ul>
	 *   <li>The first parameter is que query id. It is an alphanumeric string
	 *   which normally will contain numbers, but other non-digit characters
	 *   are permitted. <b>No alphanumeric values are not allowed</b></li>
	 *   <li></li>
	 * </ul>
	 * 
	 */
	public boolean validateParams(String... params) {
		if (params.length != 2){
			return false;
		}
		// TODO: check other things
		return true;
	}
	
}
