/**
 * 
 */
package es.upm.dit.gsi.sojason.beans;

import jason.asSyntax.Literal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.upm.dit.gsi.jason.utils.NotationUtils;

/**
 * 
 * Project: SOJA
 * Package: es.upm.dit.gsi.sojason.beans
 * Class: Journey
 *
 * @author Miguel Coronado (miguelcb@dit.upm.es)
 * @version Mar 22, 2012
 *
 */
public class Journey implements Perceptable {

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
	
	/** The query id. It is an annotation, it is not mandatory */
	private String queryid = null;
	
	/**
	 * @return the departureTime
	 */
	public String getDepartureTime() {
		return departureTime;
	}

	/**
	 * @param departureTime
	 *            the departureTime to set
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
	 * @param arrivalTime
	 *            the arrivalTime to set
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
	 * @param duration
	 *            the duration to set
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
	 * @param oringin
	 *            the oringin to set
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
	 * @param destination
	 *            the destination to set
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
	 * @param fares
	 *            the fares to set
	 */
	public void setFares(Map<String, String> fares) {
		this.fares = fares;
	}
	
	/**
	 * @return the queryid
	 */
	public String getQueryid() {
		return queryid;
	}

	/**
	 * @param queryid the queryid to set
	 */
	public void setQueryid(String queryid) {
		this.queryid = queryid;
	}
	
	/** Textual representation of the journey. Use for debuging purposes only. */
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
		if (fares != null)
			toString = toString.concat(fares.toString());
		else
			toString += null;
		return toString;
	}

	/**
	 * <p>Generates a beliefs representation of the </code>Journey</code>.
	 * In the current implementation of {@link #toPercepts()}, the journey 
	 * is represented by several beliefs, all of them with the same value 
	 * in the from, to, departure and arrival time fields, but different 
	 * fares. The exact representation is presented below:</p>
	 * 
	 * <ul>
	 * 	 <li>journey(madrid, ciudad_real, time(10,15), time(11,5), fare(turista, 22.5))</li>
	 *   <li>journey(madrid, ciudad_real, time(10,15), time(11,5), fare(preferente, 35))</li>
	 * </ul>
	 * 
	 * <p>Notice that in the current implementation the <code>Duration</code> 
	 * attribute is skiped.</p>
	 * 
	 * @return The list with the percepts or an empty list if no fare is given
	 */
	public List<Literal> toPercepts() {
		if (this.fares.size() == 0){ return new LinkedList<Literal>(); }
		return unfoldPercepts();
		//return foldPercepts();
	}

	/**
	 * <p>Generates a beliefs representation of the </code>Journey</code>.
	 * The journey is represented by a single belief, that contains all the 
	 * fares as an array. The exact representation is presented below</p>
	 * 
	 * <ul>
	 * 	 <li>journey(madrid, ciudad_real, time(10,15), time(11,5), [fare(turista, 22.5), fare(preferente, 35)])</li>
	 * </ul>
	 * 
	 * @return	List with literals that represent the journey. With a folded 
	 * 			percept only one percept will be included in the list.
	 */
	@SuppressWarnings("unused")
	private List<Literal> foldPercepts() {
		
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
		for (String fareName : fares.keySet()) {
			percept = percept.concat("fare(");
			percept = percept.concat(NotationUtils.compact(fareName));
			percept = percept.concat(", ");
			percept = percept.concat(fares.get(fareName));
			percept = percept.concat("), ");
		}
		percept = percept.substring(0, percept.lastIndexOf(","));
		percept = percept.concat("])");
		
		// queryid annotation
		percept = percept.concat("[query(");
		percept = percept.concat(this.getQueryid());
		percept = percept.concat(")]");
		
		LinkedList<Literal> ret = new LinkedList<Literal>();
		ret.add(Literal.parseLiteral(percept));

		return ret;
	}
	
	/**
	 * <p>Generates a beliefs representation of the </code>Journey</code>.
	 * The journey is represented by several beliefs, all of them with the 
	 * same value in the from, to, departure and arrival time fields, but 
	 * different fares. The exact representation is presented below:</p>
	 * 
	 * <ul>
	 * 	 <li>journey(madrid, ciudad_real, time(10,15), time(11,5), fare(turista, 22.5))</li>
	 *   <li>journey(madrid, ciudad_real, time(10,15), time(11,5), fare(preferente, 35))</li>
	 * </ul>
	 * 
	 * TODO: when it is unfloded, it might be usefull if it includes, as 
	 * annotation, an id that represents the journey, to easily join the 
	 * literals.
	 *  
	 * @return	List with literals that represent the journey. All of the 
	 * 			literals will have the same, from, to, departure and arrival 
	 * 			time value, but different fares.
	 */
	private List<Literal> unfoldPercepts() {

		LinkedList<Literal> ret = new LinkedList<Literal>();// returning list
		
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
		
		percept = percept.concat("), ");
		
		for(String fareName : fares.keySet()) {
			String perceptFare = percept.concat("fare(");
			perceptFare = perceptFare.concat(NotationUtils.compact(fareName));
			perceptFare = perceptFare.concat(", ");
			perceptFare = perceptFare.concat(fares.get(fareName));
			perceptFare = perceptFare.concat(") ");
			perceptFare = perceptFare.concat(")"); // close journey
			
			// queryid annotation
			perceptFare = perceptFare.concat("[query(");
			perceptFare = perceptFare.concat(this.getQueryid());
			perceptFare = perceptFare.concat(")]");
			
			ret.add(Literal.parseLiteral(perceptFare));
		}
		
		return ret;
	}
	
	/** Try toPercepts method */
	public static void main(String[]args) {
		Journey j = new Journey();
		j.setArrivalTime("10.00");
		j.setDepartureTime("8:50");
		j.setDestination("ciudad real");
		j.setDuration("");
		j.setOrigin("madrid");
		j.setQueryid("123456");
		
		Map<String, String> fares = new HashMap<String, String>();
		fares.put("turista", "22.5");
		fares.put("preferente", "38.5");
		fares.put("turista niño", "16.0");
		fares.put("preferente niño", "28.5");
		j.setFares(fares);
		
		System.out.println(j.toPercepts());
//		System.out.println(j.foldPercepts());
	}
	
}
