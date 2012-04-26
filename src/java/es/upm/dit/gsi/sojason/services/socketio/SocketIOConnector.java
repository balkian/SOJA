package es.upm.dit.gsi.sojason.services.socketio;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import jason.asSyntax.Literal;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import es.upm.dit.gsi.jason.utils.CollectionUtils;
import es.upm.dit.gsi.sojason.SOModel;
import es.upm.dit.gsi.sojason.services.AsyncWebServiceConnector;

/**
 * Project: Web40SOJason
 * Package: es.upm.dit.gsi.sojason.services.nlu
 * Class: NLUConnector
 *
 * @author Miguel Coronado (miguelcb@dit.upm.es)
 * @version Feb 27, 2012
 *
 */
public class SocketIOConnector implements AsyncWebServiceConnector{

	/** The url of the service */
	private String serviceUrl;
	/** */
	private Logger logger = Logger.getLogger("Web40SOJason." + SocketIOConnector.class.getName());
	
	private SocketIO socket;
	
	private SOModel model;

	IOCallback callback = new IOCallback() {

		public void onMessage(JSONObject json, IOAcknowledge ack) {
			System.out.println("Message:" + json.toString());
		}

		@Override
		public void onMessage(String data, IOAcknowledge ack) {
			System.out.println("Message:" + data);
		}

		@Override
		public void onError(SocketIOException socketIOException) {
			System.out.println("Error");
			socketIOException.printStackTrace();
		}

		@Override
		public void onDisconnect() {

		}

		@Override
		public void onConnect() {
		}

		@Override
		public void on(String event, IOAcknowledge ack, Object... args) {
			logger.info("Package ack'ed");
			JSONObject jso = new JSONObject();
			String name = (String) jso.get("id");
			String agName = "";
			if(agentsID.containsKey(name)){
				agName = agentsID.get(name);
			}
			else if(agentsEvent.containsKey(event)){
				agName = agentsEvent.get(event);
			}
			String str = CollectionUtils.toPercepts(jso);
			Literal lit = Literal.parseLiteral(str);
			model.setDataInbox(agName, lit);
			logger.info("DataInbox set to "+str);
		}

		@Override
		public void onMessage(org.json.JSONObject arg0, IOAcknowledge arg1) {
			// TODO Auto-generated method stub
			
		}
	};
	private HashMap<String,String> agentsID;
	private HashMap<String,String> agentsEvent;
	
	
	//TODO Remove try-catch
	/** Constructor */
	public SocketIOConnector(String serviceUrl, SOModel model) {
		this.model = model;
		this.serviceUrl = serviceUrl;
		this.agentsID = new HashMap<String,String>();
		this.agentsEvent = new HashMap<String,String>();
		
		
		try {
			socket = new SocketIO();
			socket.connect(serviceUrl, callback);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public boolean call(final SOModel model, final String agName, String... params) {
		logger.info("Calling SocketIOConnector with"+params[0]);
		try {
			socket.emit("test", new IOAcknowledge() {
				@Override
				public void ack(Object... args) {
					logger.info("Package ack'ed");
					String str = CollectionUtils.toPercepts(args);
					Literal lit = Literal.parseLiteral(str);
					model.setDataInbox(agName, lit);
					logger.info("DataInbox set to "+str);
				}
			}, new JSONObject().put("Sending", "From Jason"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * This validates the parameters received. The 
	 * {@link SocketIOConnector#call(String...)} method expects to receive two
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
	
	
	public void addAgentForID(String name, String id){
		agentsID.put(name, id);
	}
	
	public void removeAgentForID(String name){
		agentsID.remove(name);
	}
	

	public void removeAgentsForID(String id){
		for(String key : agentsID.keySet()){
			if(agentsID.get(key).equals(id)){
				agentsID.remove(id);
			}
		}
	}
	public void addAgentForEvent(String name, String id){
		agentsEvent.put(name, id);
	}
	
	public void removeAgentForEvent(String name){
		agentsEvent.remove(name);
	}

	public void removeAgentsForEvent(String id){
		for(String key : agentsEvent.keySet()){
			if(agentsEvent.get(key).equals(id)){
				agentsEvent.remove(id);
			}
		}
	}
	public void removeAgentsByName(String name){
		for(String key : agentsEvent.keySet()){
			if(agentsEvent.get(key).equals(name)){
				agentsEvent.remove(name);
			}
		}
		for(String key : agentsID.keySet()){
			if(agentsID.get(key).equals(name)){
				agentsID.remove(name);
			}
		}
	}
}
