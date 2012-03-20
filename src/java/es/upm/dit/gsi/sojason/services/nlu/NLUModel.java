package es.upm.dit.gsi.sojason.services.nlu;
import jason.asSyntax.Literal;

/**
 * 
 *
 * Project: Web40
 * Package: es.upm.dit.gsi.qa.services.nlu
 * Class:  
 *
 * @author Miguel Coronado (miguelcb@dit.upm.es)
 * @version Feb 28, 2012
 *
 */
public class NLUModel {

	
	public final static String JSON_DOMAINS_NODENAME = "domains";
	
	public final static String JSON_TRAVEL_NODENAME = "travel";
	
	public final static String JSON_DATES_NODENAME = "dates";
	
	public final static String JSON_DATES_DEPART_NODENAME = "depart";
	
	public final static String JSON_DATES_RETURN_NODENAME = "return";
	
	public final static String JSON_PRICE_NODENAME = "price";
	
	public final static String JSON_CURRENCY_NODENAME = "currency";
	
	public final static String JSON_MAX_NODENAME = "max";
	
	public final static String JSON_MIN_NODENAME = "min";
	
	public final static String JSON_LOCATIONS_NODENAME = "locations";
	
	public final static String JSON_FROM_NODENAME = "from";
	
	public final static String JSON_TO_NODENAME = "to";
	
	public final static String JSON_NUMBER_NODENAME = "number";
	
	public final static String JSON_TIME_NODENAME = "time";
	
	public final static String JSON_TIME_DEPART_NODENAME = "depart";
	
	public final static String JSON_TIME_RETURN_NODENAME = "return";
	
	
	
	public static Literal getLiteralPriceMin (String query, String domain, double price) {
		return Literal.parseLiteral("price(min," + price + ")[query("+ query +"),domain("+ domain +")]");
	}
	
	public static Literal getLiteralPriceMax (String query, String domain, double price) {
		return Literal.parseLiteral("price(max," + price + ")[query("+ query +"),domain("+ domain +")]");
	}
	
	public static Literal getLiteralCurrency (String query, String domain, String currency) {
		return Literal.parseLiteral("currency(" + currency + ")[query("+ query +"),domain("+ domain +")]");
	}
	
	public static Literal getLiteralLocationFrom (String query, String domain, String place) {
		return Literal.parseLiteral("location(from," + place + ")[query("+ query +"),domain("+ domain +")]");
	}
	
	public static Literal getLiteralLocationTo (String query, String domain, String place) {
		return Literal.parseLiteral("location(to," + place + ")[query("+ query +"),domain("+ domain +")]");
	}
	
	public static Literal getLiteralDateDeparture (String query, String domain, String day, String month, String year) {
		return Literal.parseLiteral("date(departure," + day + "," + month + "," + year + ")[query("+ query +"),domain("+ domain +")]");
	}
	
	public static Literal getLiteralDateReturn (String query, String domain, String day, String month, String year) {
		return Literal.parseLiteral("date(return," + day + "," + month + "," + year + ")[query("+ query +"),domain("+ domain +")]");
	}
	
	public static Literal getLiteralTimeDeparture (String query, String domain, String hour, String min) {
		return Literal.parseLiteral("time(departure," + hour + "," + min + ")[query("+ query +"),domain("+ domain +")]");
	}

	public static Literal getLiteralTimeReturn (String query, String domain, String hour, String min) {
		return Literal.parseLiteral("time(return," + hour + "," + min + ")[query("+ query +"),domain("+ domain +")]");
	}
	
	public static Literal getLiteralType (String query, String domain, String type) {
		return Literal.parseLiteral("type(" + type + ")[query("+ query +"),domain("+ domain +")]");
	}
	
}
