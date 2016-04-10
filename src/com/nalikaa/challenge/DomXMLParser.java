package com.nalikaa.challenge;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomXMLParser {
	
	public static String parse(String xml) throws SAXException, IOException, ParserConfigurationException
	{		
		String type=null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xml);
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());	    
	    NodeList nodes = doc.getDocumentElement().getChildNodes();
	    
	    for (int i=0; i<nodes.getLength();i++)
	    {
	    	Node node = nodes.item(i);
	    	if (node.getNodeType()==Node.ELEMENT_NODE)
	    	{
	    		Element element = (Element) node;
	    		if(element.getTagName().equals("type"))
	    		{
		        	type = element.getTextContent();
		        	System.out.println("type is : " + type);
		        }
	    	}
	    }
		return type;
	}

}
