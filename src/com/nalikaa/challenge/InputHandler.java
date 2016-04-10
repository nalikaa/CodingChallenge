package com.nalikaa.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

public class InputHandler extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int UNAUTHORIZED=javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
	public static final int FORBIDDEN=javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
	public static final int NO_CONTENT =javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
	
	
	private ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator();		
	private static final String CONSUMER_KEY = "nalikasproduct-100415";
	private static final String CONSUMER_SECRET = "6qgVCW7lMzhA6o8L";
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		processRequest(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

		processRequest(req, res);

	}
	
	public void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
		/*Sample Header : 
		GET https://www.appdirect.com/api/billing/v1/orders HTTP/1.1
		Host: www.appdirect.com
		Content-Type: application/xml
		Authorization: OAuth realm="",
		oauth_nonce="72250409",
		oauth_timestamp="1294966759",
		oauth_consumer_key="Dummy",
		oauth_signature_method="HMAC-SHA1",
		oauth_version="1.0",
		oauth_signature="IBlWhOm3PuDwaSdxE/Qu4RKPtVE="
		*/
		
		// get the key and value from authorization
		String authHeader = req.getHeader("Authorization");
		if (authHeader == null)
		{
			generteNSendFailedMessage(res,UNAUTHORIZED, "Empty Authorization Header");
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
			    String authValues[] = authHeader.split(",");
		
		for(String value : authValues)
		{
          final String split[] = value.split("=");
          if(split.length > 1)
            map.put(split[0],split[1]);
		}
		//Get key & secret
		String oauthConsumerKey = map.get("oauth_consumer_key");
		String oauthSignature = map.get("oauth_signature");
		System.out.println("key is : " + oauthConsumerKey);
		System.out.println("oauthSignature is : " + oauthSignature);
		
		if ((oauthConsumerKey ==null) || (oauthSignature==null))
		{
			generteNSendFailedMessage(res,FORBIDDEN, "Empty oauthConsumerKey or oauthSignature");
			return;
		}
		
		if (!oauthConsumerKey.equals(CONSUMER_KEY) || !oauthSignature.equals(CONSUMER_SECRET) )
		{
			generteNSendFailedMessage(res,FORBIDDEN, "oauthConsumerKey or oauthSignature not valid");
			return;
		}
		else //OAuth signature is valid. Proceed
		{
			processEvent(req, res);
		}
	}
	
	private void processEvent(HttpServletRequest req, HttpServletResponse res)
	{
		String type=null;
		String url=null;
		
		//read the url from notification
		url = req.getParameter("url");
		System.out.println("url is :" + url);
		if(url == null)
		{
			generteNSendFailedMessage(res,FORBIDDEN, "URL is missing in notification");
			return;
		}
		String xml =signOutGoingReqNReadEvent(res,url);	                 
		//Parse xml
		try {
			type = DomXMLParser.parse(xml);
			//create the response based on type
			res.setContentType("text/xml");
			String out = messageGenerator.generateSuccessResponse(type);
			res.getWriter().println(out);
		}
		catch (Exception e) {
			e.printStackTrace();
			generteNSendFailedMessage(res,NO_CONTENT, "Error parsing XML file");
			return;
		}
	}
	
	private String signOutGoingReqNReadEvent(HttpServletResponse res,String urlString)
	{
		OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		String inputLine = null;
		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			consumer.sign(request);
			request.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			inputLine =in.readLine();
			in.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			generteNSendFailedMessage(res,FORBIDDEN, "URL is missing in notification");
			
		}
		return inputLine;
	}
	
	public void generteNSendFailedMessage(HttpServletResponse res,int errorCode, String message)
	{
		String messageToSend = messageGenerator.generateUnAuthorizedMessage(errorCode,message );
		res.setContentType("text/xml");
		try {
			res.getWriter().println(messageToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

}
