package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import com.salesforce.canvas.CanvasRequest;
import com.salesforce.canvas.SignedRequest;

public class Application extends Controller {

	public static Result index() {

		String message = "";
		CanvasRequest canvasInfo = AppProperties.getSessionCanvasRequest(Controller.session());
		if(canvasInfo != null)
		{
			message += "Hi <b>"+canvasInfo.getContext().getUserContext().getUserName()+"</b>! <br/>Canvas Session Info has already been given. Enjoy Canvas App!";
		}
		else
		{
			message += "Canvas App Session info not already set. <br/>";
			if(request().method()== "POST")
			{
				message += "Incoming POST request...<br/>";
				
				//this is the post info got from Force.com POST request
				String[] signedRequest = request().body().asFormUrlEncoded().get("signed_request");
				
				if(signedRequest!=null)
				{
					try
					{
						canvasInfo = SignedRequest.verifyAndDecode(signedRequest[0], AppProperties.CANVAS_CONSUMER_SECRET);
						AppProperties.setSessionCanvasRequest(canvasInfo,Controller.session());
						message += "Hi <b>"+canvasInfo.getContext().getUserContext().getUserName()+"</b>! <br/>Canvas Session Info has just been correctly given. Enjoy Canvas App!";
					}
					catch(Exception e)
					{
						e.printStackTrace();
						message += "Error occurred (see log): "+e.getLocalizedMessage();
					}
				}
				else
				{
					message += "No session info provided.<br/>";
				}
			}
		}
		return ok(index.render(message));
	}

}