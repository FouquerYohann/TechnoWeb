package service;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 
 * Classe retournant des objet JSON permettant d'uiliser 
 * les servlets. 
 */
public class ServiceTools {
	
	public static JSONObject ServiceRefused(String message, int code) throws JSONException{
		JSONObject retour= new JSONObject();
		retour.put(message, code);
		return retour;
	}
	
	public static JSONObject ServiceAccepted() throws JSONException{
		JSONObject retour= new JSONObject();
		retour.put("OK", 0);
		return retour;
	}
}
