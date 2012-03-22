package es.upm.dit.gsi.sojason.services.socketio;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import jason.asSyntax.Literal;

import java.net.MalformedURLException;
import java.util.logging.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
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
	
	private static SocketIO socket;
	static IOCallback callback = new IOCallback() {

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
			
		}

		@Override
		public void onMessage(org.json.JSONObject arg0, IOAcknowledge arg1) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	//TODO Remove try-catch
	/** Constructor */
	public SocketIOConnector(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		
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
					Literal lit = Literal.parseLiteral("success(yes)");
					model.setDataInbox(agName, lit);
					logger.info("DataInbox set");
				}
			}, new JSONObject().put("Hello", "Planeta"));
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
	
}
