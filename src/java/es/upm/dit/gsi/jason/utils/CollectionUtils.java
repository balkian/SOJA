/**
 * 
 */
package es.upm.dit.gsi.jason.utils;

import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.util.JSONUtils;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.impl.JsonParserBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Project: Web40SOJason
 * Package: es.upm.dit.gsi.jason.utils
 * Class: CollectionUtils
 *
 * @author Miguel Coronado (miguelcb@dit.upm.es)
 * @version Mar 9, 2012
 *
 */
public abstract class CollectionUtils {

	/**
	 * This wraps a Literal in a collection
	 * @param literal	The literal
	 * @return			Collection containing the literal given
	 */
	public static Collection<Literal> wrapList(Literal literal) {
		Collection<Literal> res = new LinkedList<Literal>();
		res.add(literal);
		return res;
	}
	
	/**
	 * This wraps a Literal in a collection
	 * @param literal	The string that represents a literal
	 * @return			Collection containing the literal given
	 */
	public static Collection<Literal> wrapList(String literal) {
		Collection<Literal> res = new LinkedList<Literal>();
		res.add(Literal.parseLiteral(literal));
		return res;
	}
	
	/**
	 * This
	 * @param collection
	 * @return
	 */
	public static String[] toStringArray (Collection<? extends Object> collection){
		String[] strArray = new String[collection.size()];
		
		int index = 0;
		for(Object obj : collection){
			if(obj == null) { 
				strArray[index] = "null"; 
			}
			else {
				strArray[index] = obj.toString();
			}
			index++;
		}
		
		return strArray;
	}
	
	/**
	 * 
	 * @param items
	 * @return
	 */
	public static String[] toStringArray (Object[] items){
		String[] strArray = new String[items.length];
		
		for(int index = 0; index < items.length; index++){
			Object obj = items[index];
			if(obj == null){
				strArray[index] = "null";
			}
			else{
				strArray[index] = items[index].toString();
			}
		}
		
		return strArray;
	}
	
	/**
	 * 
	 * @param jso JSONObject to transform to Percepts
	 * @return	String representation of the JSONObject as Percent
	 */
	
	private static String toPerceptsObject(JSONObject jso){
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = jso.keys();
		while(it.hasNext()){
			String key = it.next();
			try {
				Object temp= jso.get(key);
				key = NotationUtils.compact(key);
				String res = toPercepts(temp);
				sb.append(key+"(");
				sb.append(res);
				sb.append(")");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(it.hasNext()){
				sb.append(",");
			}
		}
		return sb.toString();
		
	}
	
	/**
	 * 
	 * @param jsa JSONArray to be converted
	 * @return String Percept representation of the JSONArray
	 */
	
	private static String toPerceptsArray(JSONArray jsa){
		StringBuilder sb = new StringBuilder();
		int top = jsa.length();
		int topm = top-1;
		sb.append("[");
		for(int i=0;i<jsa.length();i++){
			try{
				sb.append(toPercepts(jsa.get(i)));
			}catch(JSONException ex){
				ex.printStackTrace();
			}
			if(i<topm){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	/**
	 * String representation of a Percept, from a JSON-like object
	 * 
	 * JSON strings are represented as quoted strings in jason, as it seems to
	 * be the easiest and most compatible way.
	 * 
	 * Examples:
	 * {"key 1":"value 1"} => key_1("value")
	 * 
	 * {"key1":"value1", "key2":2} => key1("value1"),key2(2)
	 * 
	 * {"testArray":["value1","value2","value3"],"testKey":"testValue","specialKey":"Ñoña-2_3","testMix":["one",{"twoDic":[1,2,"2",2,2.1]}],"testDic":{"key2":"value2","key1":"value1"}}
	 * => testarray(["value1","value2","value3"]),testkey("testValue"),specialkey("Ñoña-2_3"),testmix(["one",twodic([1,2,"2",2,2.1])]),testdic(key2("value2"),key1("value1"))
	 * 
	 * @param obj Object to be translated to Percepts
	 * @return String representation of the Percept
	 */
	
	public static String toPercepts(Object obj){
		if(obj instanceof JSONArray){
			return toPerceptsArray((JSONArray) obj);
		}
		else if (obj instanceof JSONObject) {
			return toPerceptsObject((JSONObject) obj);
		}
		else if (obj instanceof Number){
			return obj.toString();
		}
		else{
			return "\""+obj.toString()+"\"";
		}
				
		//journey(madrid,barcelona,time(15,30),time(18,40),fare(preferente_nino,105.9))
		//journey(madrid,barcelona,time(17,0),time(19,30),fare(turista_nino,83.3))
//;
		
		// Para nuestro ejemplo:
		// journey(["madrid","barcelona",time(15,30),time(18,40),fare("preferente nino",105.9)])
//		n.
	}
	
	/**
	 * Example of conversion:
	 * 
	 * Percept: journey(["madrid","barcelona",time(15,30),time(18,40),fare("preferente nino",105.9)])
	 * Result:  { "journey" : { "madrid", "barcelona", { "time" : { 15, 30 } } , { "time" : { 18, 40 } } , { "fare" : { "preferente nino", 105.9 } }  } } 
	 * 
	 * @param percept Perception as a String
	 * @return String representation of the JSON
	 */
	public static String perceptToJSON(String percept){
		//System.out.println("Pasando con:"+percept);
		String result = "";
		String end = "";
		Literal lit = Literal.parseLiteral(percept);
		String fun = lit.getFunctor();
		if(!fun.equals("")){
			result+= "{ \""+ fun + "\" : { ";
			end = " } } ";
		}	
		Term[] terms = lit.getTermsArray();
		int last = terms.length-1;
		for(int i=0;i<=last;i++){
			Term term = terms[i];
			result+=termToString(term);
			if(i<last){
				result+=", ";
			}
		}
		return result + end;
	}
	
	public static String termToString(Term term){
		String result = "";
		if(term.isNumeric()){
			System.out.println("Numeric:"+ term.toString());
			result = term.toString();
		}
		else if(term.isLiteral()){
			System.out.println("Literal:"+ term.toString());
			result = perceptToJSON(term.toString());
		}
		else if (term.isString()){
			System.out.println("String:"+ term.toString());
			result = term.toString();
		}
		else if (term.isList()){
			System.out.println("List:"+ term.toString());
			int last =((List<Term>) term).size()-1;
			for(int i=0;i<=last;i++){
				Term t = ((List<Term>) term).get(i);
				result+=(termToString(t));
				if(i<last){
					result+=", ";
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args){
		String st = "journey([\"madrid\",\"barcelona\",time(15,30),time(18,40),fare(\"preferente nino\",105.9)])";
		String json = perceptToJSON(st);
		System.out.println("String: "+st);
		System.out.println("Resultado: "+json);
	}

}
