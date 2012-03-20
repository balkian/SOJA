package es.upm.dit.gsi.jason.utils;


/**
 * This Utils class is used to validate string according to the Jason atom 
 * notation criteria, and transform an invalid notation into a valid one
 * and vice versa.
 * 
 * This is useful in some context where the agents need to interact with an
 * uncontrollable environment such as the Web. 
 * 
 * In Jason notation, white-spaces are not allowed, neither, words that starts
 * with capital letter. 
 * 
 * @author gsi.dit.upm.es
 *
 */
public class NotationUtils {

	/**
	 * @param toCheck
	 * @return
	 */
	public static boolean isValidAtom (String toCheck) {
		String lowerCase = toCheck.toLowerCase();
		return !toCheck.contains(" ") && !toCheck.contains(",") && toCheck.equals(lowerCase);
	}
	
	/**
	 * 
	 * @param toCheck
	 * @return
	 */
	public static boolean isCompactable (String toCheck) {
		return true;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String compact(String str) {
		
		if (isValidAtom (str)) {return str;}
		if (!isCompactable(str)) {return null;}
		
		str = str.replace("_", "___");
		str = str.replace(" ", "_");
		str = str.replace("ñ", "n");
		return str.toLowerCase();
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String uncompact(String str) {
		str = str.replace("___", "#");
		str = str.replace("_", " ");
		str = str.replace("#", " ");
		return str;
	}
	
	/**
	 * <p>This removes the quotation mark from the string given. If that
	 * string has no quotation marks it returned trimmed.</p>
	 * 
	 * <p>The quotation marks are only removed from the beginning and the
	 * end of the string, so any quotation mark inserted in the middle of
	 * the string will be kept.</p>
	 * 
	 * @return 	the string without the quotation marks
	 */
	public static String removeQuotation (String str) {
		String message = str.trim();
		if(message.startsWith("\"")) message = message.substring(1);
		if(message.endsWith("\"")) message = message.substring(0, message.length()-1);
		return message;
	}
	
}
