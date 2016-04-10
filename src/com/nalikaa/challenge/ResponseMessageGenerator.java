package com.nalikaa.challenge;

public class ResponseMessageGenerator {	
	
	public String generateUnAuthorizedMessage(int errorCode, String message )
	{
		return "<result>" +
				"<success>" +
				"false" + "</success>" +
				"<errorCode>" + errorCode + "</errorCode>" +
				"<message>" + message + "</message>" +
				"</result>" ;
	}
	
	public String generateSuccessResponse(String type){
		String message = null;
		switch(type){
		case "SUBSCRIPTION_ORDER": message = generateSubscriptionOrderResponse(); 
		      break;
		case "SUBSCRIPTION_CHANGE": message = generateOtherResponse("Order changed success");
		      break;
		case "SUBSCRIPTION_CANCEL": message = generateOtherResponse("Subscription cancelled successfully");
		     break;
		case "SUBSCRIPTION_NOTICE": message = generateOtherResponse("Notice sent successfully");
		     break;
		}
		
		return message;
	}
	
	private String generateSubscriptionOrderResponse()
	{
		return
				"<result>" +
						"<success>" +
						"true" + "</success>" +						
						"<accountIdentifier>" + "new-account-identifier" + "</accountIdentifier>" +
						"</result>"
						;
		
	}
	
	private String generateOtherResponse(String message)
	{
		return
				"<result>" + "<success>" +	"true" + "</success>" +	
				"<message>" + message + "</message>" +
		        "</result>";
		
	}

}
