package com.nalikaa.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.servlet.ServletException;

import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;

import com.nalikaa.challenge.InputHandler;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;

public class InputHandlerTest {
	private static final String CONSUMER_KEY = "nalikasproduct-100415";
	private static final String CONSUMER_SECRET = "6qgVCW7lMzhA6o8L";
	private final String USER_AGENT = "Mozilla/5.0";
	private InputHandler servlet;
//    private MockHttpServletRequest request;
//    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        servlet = new InputHandler();
//        request = new MockHttpServletRequest();
//        response = new MockHttpServletResponse();
    }

    @Test
    public void oAuthTest() throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException
    {
    	String urlStr ="http://localhost:8080/CodingChallenge2/InputHandler";
    	//Instantiate an HttpClient        
        URL url = new URL(urlStr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setRequestProperty("Accept", "application/text");
//        urlConnection.setRequestProperty("Content-Type", "application/text; charset=utf8");
        urlConnection.setRequestMethod("GET");
//        urlConnection.setDoOutput(true);
        
        urlConnection.setRequestProperty("User-Agent", USER_AGENT);
        urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

       
      //Sign the request
        OAuthConsumer dealabsConsumer = new DefaultOAuthConsumer (CONSUMER_KEY, CONSUMER_SECRET);
        HttpParameters doubleEncodedParams =  new HttpParameters();
        doubleEncodedParams.put("realm", urlStr);
        dealabsConsumer.setAdditionalParameters(doubleEncodedParams);
        dealabsConsumer.sign(urlConnection);
        //Send the request and read the output
        try {
        	int responseCode = urlConnection.getResponseCode();
    		System.out.println("\nSending 'GET' request to URL : " + url);
    		System.out.println("Response Code : " + responseCode);
    		
        	System.out.println("Response: " + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String inputStreamString = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
            System.out.println(inputStreamString);
        }
        finally {
            urlConnection.disconnect();
        }
    }
    
    @Test
    public void correctUsernameInRequest() throws ServletException, IOException {
//        request.addParameter("username", "scott");
//        request.addParameter("password", "tiger");
//
//        servlet.doPost(request, response);
//
//        assertEquals("text/html", response.getContentType());

        // ... etc
    }

}
