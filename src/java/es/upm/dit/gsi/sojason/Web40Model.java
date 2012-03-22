package es.upm.dit.gsi.sojason;

import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import es.upm.dit.gsi.jason.utils.CollectionUtils;
import es.upm.dit.gsi.sojason.services.nlu.NLUConnector;
import es.upm.dit.gsi.sojason.services.travel.RenfeScrapper;

/**
 * 
 *
 * Project: Web40
 * Package: es.upm.dit.gsi.qa
 * Class: Web40Model
 *
 * @author Miguel Coronado (miguelcb@dit.upm.es)
 * @version Feb 29, 2012
 *
 */
public class Web40Model extends SOModel{

	/** */
	public final static String NLU_SERVICE_URL = "http://46.4.52.82:3333/nlu";
	/** */
	private NLUConnector nluConnector;
	/** */
	private RenfeScrapper renfeScrapper;
	/** */
	private Logger logger = Logger.getLogger("Web40SOJason." + Web40Model.class.getName());
	
	/** Constructor 
	 * @throws IOException */
	public Web40Model () throws IOException {
		super();
		this.nluConnector = new NLUConnector(NLU_SERVICE_URL);
		this.renfeScrapper = new RenfeScrapper();
	}
	
	/**
	 * This calls the NLU service
	 * 
	 * Internally this modifies the model so it reports the agent
	 *  
	 * @param agName 	the name of the agent that will be reported with the 
	 * 					results of the call.
	 * @param terms		The parameters
	 * @return			
	 */
	public boolean sendNlu (String agName, Collection<Term> params) {

		logger.info("Entering sendNLU...");
		try{
			String[] strParams = CollectionUtils.toStringArray(params);
			Collection<Literal> serviceData = nluConnector.call(strParams);
			if(serviceData == null){ 
				logger.info("Could not complete action sendNLU: no service data found");
				return false; 
			}
			
			// put data into mailbox
			this.setDataInbox(agName, serviceData);
		} 
		catch (Exception e){
			logger.info("Could not complete action sendNLU:" + e.getMessage());
			return false;	
		}
		
		logger.info("NLU call completed successfully");
		return true;
	}
	
	/**
	 * 
	 * @param agName
	 * @param terms
	 * @return
	 */
	public boolean findTravel (String agName, Collection<Term> params) {
		
		logger.info("Entering findTravel...");
		try{
			String[] strParams = CollectionUtils.toStringArray(params);
			Collection<Literal> serviceData = renfeScrapper.call(strParams);
			if(serviceData == null){ return false; }
			
			// put data into mailbox
			this.setDataInbox(agName, serviceData);
		} 
		catch (Exception e){ return false; }
		logger.info("Scraping completed successfully");
		return true;
		
	}
	
}
