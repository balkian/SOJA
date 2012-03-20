package es.upm.dit.gsi.sojason.beans;

import jason.asSyntax.Literal;

import java.util.LinkedList;
import java.util.List;

import es.upm.dit.gsi.jason.utils.NotationUtils;
import es.upm.dit.gsi.sojason.services.nlu.NLUModel;

public class NLUTravel implements Perceptable {

	/**  */
	public final static String TRAVEL_DOMAIN = "travel";
	
	/** The departure date. */
	private String departureDate = null;
	/** The return date. It may be null. */
	private String returnDate = null;
	
	/**	The maximum price the user is willing to pay */
	private double priceMin = -1;
	/**	The minimum price */
	private double priceMax = -1;
	/**	The currency */
	private String currency = null;
	
	/** The departure location */
	private String locationFrom = null;
	/** The destination location */
	private String locationTo = null;
	
	/** The preferred departure Time */
	private String departureTime = null;
	/** the preferred return Time*/
	private String returnTime = null;
	
	/** the number of people is traveling */
	private int number = 1;
	
	/**  */
	private TravelType type = null;
	/**  */
	private boolean scales = false;
	
	/** The unique id for the dialog. It is used to group dialog entries */
	private String queryId = null;
	
	/**
	 * @return the departureDate
	 */
	public String getDepartureDate() {
		return departureDate;
	}
	
	/**
	 * @param departureDate the departureDate to set
	 */
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	
	/**
	 * @return the returnDate
	 */
	public String getReturnDate() {
		return returnDate;
	}
	
	/**
	 * @param returnDate the returnDate to set
	 */
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	
	/**
	 * @return the priceMin
	 */
	public double getPriceMin() {
		return priceMin;
	}
	
	/**
	 * @param priceMin the priceMin to set
	 */
	public void setPriceMin(double priceMin) {
		this.priceMin = priceMin;
	}
	
	/**
	 * @param priceMax the priceMax to set
	 */
	public void setPriceMin(String priceMin) {
		if (priceMin == null){ return; }
		
		try{
			this.priceMin = Double.parseDouble(priceMin);
		}
		catch (NumberFormatException nfe){
			// Do nothing
		}
	}
	
	/**
	 * @return the priceMax
	 */
	public double getPriceMax() {
		return priceMax;
	}
	
	/**
	 * @param priceMax the priceMax to set
	 */
	public void setPriceMax(double priceMax) {
		this.priceMax = priceMax;
	}
	
	/**
	 * @param priceMax the priceMax to set
	 */
	public void setPriceMax(String priceMax) {
		if (priceMax == null){ return; }
		
		try{
			this.priceMax = Double.parseDouble(priceMax);
		}
		catch (NumberFormatException nfe){
			// Do nothing
		}
	}
	
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * @return the locationFrom
	 */
	public String getLocationFrom() {
		return locationFrom;
	}
	
	/**
	 * @param locationFrom the locationFrom to set
	 */
	public void setLocationFrom(String locationFrom) {
		this.locationFrom = locationFrom;
	}
	
	/**
	 * @return the locationTo
	 */
	public String getLocationTo() {
		return locationTo;
	}
	
	/**
	 * @param locationTo the locationTo to set
	 */
	public void setLocationTo(String locationTo) {
		this.locationTo = locationTo;
	}
	
	/**
	 * @return the departureTime
	 */
	public String getDepartureTime() {
		return departureTime;
	}
	
	/**
	 * @param departureTime the departureTime to set
	 */
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	
	/**
	 * @return the returnReturn
	 */
	public String getReturnTime() {
		return returnTime;
	}
	
	/**
	 * @param returnReturn the returnReturn to set
	 */
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}
	
	/**
	 * @return the type
	 */
	public TravelType getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(TravelType type) {
		this.type = type;
	}
	
	/**
	 * @return the scales
	 */
	public boolean isScales() {
		return scales;
	}
	
	/**
	 * @param scales the scales to set
	 */
	public void setScales(boolean scales) {
		this.scales = scales;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	public void setNumber(String number){
		if(number == null){ return; }
		try{
			this.number = Integer.parseInt(number);
		}
		catch(NumberFormatException nfe){
			// Ok
		}
	}

	/**
	 * @return the queryId
	 */
	public String getQueryId() {
		return queryId;
	}

	/**
	 * @param queryId the queryId to set
	 */
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
	
	public String toString() {
		
		String toString = queryId + number;
		toString += "ticket(s) from: ";
		toString += locationFrom;
		toString += " to: ";
		toString += locationTo;
		toString += " the day ";
		toString += departureDate;
		toString += " at ";
		toString += departureTime;
		toString += " returning the day ";
		toString += returnTime;
		toString += " at ";
		toString += returnTime;
		toString += " for ";
		toString += priceMax + "->" + priceMin;
		toString += " ";
		toString += currency;
		return toString;
	}

	/**
	 * 
	 */
	public List<Literal> toPercepts() {
		
		List<Literal> list = new LinkedList<Literal>();
		
		if(departureDate != null){
			list.add(NLUModel.getLiteralDateDeparture(queryId, TRAVEL_DOMAIN, "03", "04", "2012"));
		}
		
		if(returnDate != null){
			list.add(NLUModel.getLiteralDateReturn(queryId, TRAVEL_DOMAIN, "03", "04", "2012"));
		}
		
		if(departureTime != null){
			list.add(NLUModel.getLiteralTimeDeparture(queryId, TRAVEL_DOMAIN, "09", "00"));
		}
		
		if(returnTime != null){
			list.add(NLUModel.getLiteralTimeReturn(queryId, TRAVEL_DOMAIN, "09", "00"));
		}
		
		if(priceMax >= 0){
			list.add(NLUModel.getLiteralPriceMax(queryId, TRAVEL_DOMAIN, priceMax));
		}
		
		if(priceMin >= 0){
			list.add(NLUModel.getLiteralPriceMin(queryId, TRAVEL_DOMAIN, priceMin));
		}
		
		if(currency != null){
			list.add(NLUModel.getLiteralCurrency(queryId, TRAVEL_DOMAIN, NotationUtils.compact(currency)));
		}
		
		if(locationFrom != null){
			list.add(NLUModel.getLiteralLocationFrom(queryId, TRAVEL_DOMAIN, NotationUtils.compact(locationFrom)));
		}
		
		if(locationTo != null){
			list.add(NLUModel.getLiteralLocationTo(queryId, TRAVEL_DOMAIN, NotationUtils.compact(locationTo)));
		}

		if(scales){
			list.add(Literal.parseLiteral("scales[query(" + queryId + "), domain(" + TRAVEL_DOMAIN + ")]"));
		}
		else{
			list.add(Literal.parseLiteral("~scales[query(" + queryId + "), domain(" + TRAVEL_DOMAIN + ")]"));
		}
		
		list.add(Literal.parseLiteral("done[query(" + queryId + "), domain(" + TRAVEL_DOMAIN + ")]"));
		
		return list;
	}
	
}
