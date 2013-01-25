package controllers;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import play.Play;
import play.mvc.Http;

import com.salesforce.canvas.CanvasRequest;



public class AppProperties {
	//this is the key of the canvas app session info in the user session object
	public static final String SESSION_KEY_CANVAS_REQUEST = "canv_req"; 
	//a simple way to get global application settings (application.conf file)
	public static final String CANVAS_CONSUMER_SECRET = Play.application().configuration().getString("canvas.consumer.secret");
	
	/*
	 * Serialize the Canvas session object to the current user session
	 */
	public static CanvasRequest getSessionCanvasRequest(Http.Session session)
	{

		CanvasRequest request = null;

		String json = session.get(SESSION_KEY_CANVAS_REQUEST);
		if(json == null) return null;

		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(CanvasRequest.class);

		try {
			request = (CanvasRequest)reader.readValue(json);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return request;
	}
	
	/*
	 * De-Serialize the Canvas session object to the current user session
	 */
	public static void setSessionCanvasRequest(CanvasRequest request, Http.Session session)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			session.put(SESSION_KEY_CANVAS_REQUEST, mapper.writeValueAsString(request));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

