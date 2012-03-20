/**
 * 
 */
package es.upm.dit.gsi.sojason.beans;

import jason.asSyntax.Literal;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.upm.dit.gsi.jason.utils.NotationUtils;

/**
 * @author miguel
 *
 */
public class Journey implements Perceptable{

	/** The departure time of the journey */
	private String departureTime;
	
	/** The arrival time of the journey */
	private String arrivalTime;
	
	/** 
	 * The duration of the journey. This is not simply the difference of the 
	 * departure and arrival time because of timezone considerations.
	 */
	private String duration;
	
	/** The origin */
	private String origin;

	/** The destination */
	private String destination;
	
	/** The fee map that contains the different available fee */
	private Map<String, String> fares;

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
	 * @return the arrivalTime
	 */
	public String getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @param arrivalTime the arrivalTime to set
	 */
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * @return the oringin
	 */
	public String getOringin() {
		return origin;
	}

	/**
	 * @param oringin the oringin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the fares
	 */
	public Map<String, String> getFares() {
		return fares;
	}

	/**
	 * @param fares the fares to set
	 */
	public void setFares(Map<String, String> fares) {
		this.fares = fares;
	}
	
	/** Textual representation of the journey. Use for debuging purposes inly.*/
	public String toString() {
		
		String toString = "From: ";
		toString = toString.concat(origin);
		toString = toString.concat(" (");
		toString = toString.concat(departureTime);
		toString = toString.concat(") to: ");
		toString = toString.concat(destination);
		toString = toString.concat(" (");
		toString = toString.concat(arrivalTime);
		toString = toString.concat(") in ");
		toString = toString.concat(duration);
		toString = toString.concat(" for ");
		if(fares != null)
			toString = toString.concat(fares.toString());
		else
			toString += null;
		return toString;
	}
	
	/**
	 * journey(madrid, ciudad_real, 10.15, 11.5, [fare(turista, 22.5), fare(preferente, 35)]
	 * journey(madrid, ciudad_real, time(10,15), time(11,5), [fare(turista, 22.5), fare(preferente, 35)]
	 * 
	 * @return
	 */
	public List<Literal> toPercepts() {
		
//		if (this.fares.size() == 0){ return null; }
//		
//		String percept = "journey(";
//		percept = percept.concat(this.origin);
//		percept = percept.concat(", ");
//		percept = percept.concat(this.destination);
//		percept = percept.concat(", ");
//		percept = percept.concat(this.departureTime);
//		percept = percept.concat(", ");
//		percept = percept.concat(this.arrivalTime);
//		
//		percept = percept.concat(", [");
//		for(String fareName : fares.keySet()) {
//			percept = percept.concat("fare(");
//			percept = percept.concat(fareName);
//			percept = percept.concat(", ");
//			percept = percept.concat(fares.get(fareName));
//			percept = percept.concat("), ");
//		}
//		percept = percept.substring(0, percept.lastIndexOf(","));
//		percept = percept.concat("])");
//		
//		LinkedList<Literal> ret = new LinkedList<Literal>();
//		ret.add(Literal.parseLiteral(percept));
//		
//		return ret;
		
		if (this.fares.size() == 0){ return null; }
		
		String percept = "journey(";
		percept = percept.concat(NotationUtils.compact(this.origin));
		percept = percept.concat(", ");
		percept = percept.concat(NotationUtils.compact(this.destination));
		
		percept = percept.concat(", time(");
		String digits[] = this.departureTime.split("[\\x2E\\x3A]"); // [.:]
		percept = percept.concat(digits[0]);
		percept = percept.concat(", ");
		percept = percept.concat(digits[1]);
		
		percept = percept.concat("), time(");
		digits = this.arrivalTime.split("[\\x2E\\x3A]"); // [.:]
		percept = percept.concat(digits[0]);
		percept = percept.concat(", ");
		percept = percept.concat(digits[1]);
		
		percept = percept.concat("), [");
		for(String fareName : fares.keySet()) {
			percept = percept.concat("fare(");
			percept = percept.concat(NotationUtils.compact(fareName));
			percept = percept.concat(", ");
			percept = percept.concat(fares.get(fareName));
			percept = percept.concat("), ");
		}
		percept = percept.substring(0, percept.lastIndexOf(","));
		percept = percept.concat("])");
		
		LinkedList<Literal> ret = new LinkedList<Literal>();
		ret.add(Literal.parseLiteral(percept));
		
		return ret;
	}

}
