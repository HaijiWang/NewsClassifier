<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="javax.xml.parsers.*"%>	  
<%@ page import="java.net.*" %>
 
    
<html>
<head>
  <title>XML PARSOR</title>
</head>
<body>
	<%!
	
	
	
	%>
	
	<%!
	private void traverseTree(Node node, 
		JspWriter out) throws Exception{
		
	}
	%>



	<%
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	docFactory.setNamespaceAware(true);
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
	System.out.println("This does not get printed on webpage.");
	out.println("<table border=2>");
	out.println("<tr><th>date</th></tr>");
	out.println();
	
	if(request.getParameter("city")!=null){
		if(request.getParameter("city").equals("aaa")){
			//doc = docBuilder.parse("http://58.213.107.34/solr/collection1/select?q=*%3A*&start=1&rows=2&wt=xml&indent=true");
		}
	}
	
	String url= "http://58.213.107.34/solr/collection1/select?q=*%3A*&start=1&rows=2&wt=xml&indent=true";
	Document doc = docBuilder.parse(
			new URL(url).openStream());
	
	System.out.println(doc);
	//doc.getDocumentELement().normalize();
	out.println("<p>doc = ...parse(url) executed<p>");
	//out.println("Root element: "+
	//	doc.getDocumentElemnet().getNodeName());
	
	
	NodeList nlist=doc.getElementsByTagName("responseHeader");
	out.println("<p>doc.getElementsByTagaName succeeded<p>");

	Node nNode = nlist.item(0);
	out.println("node name: "+nlist.getLength());
	//String value = "foo";
 	//nlist.item(0).getNodeValue();
	//out.println("<p>nlist.item(0).getNodeValue(); succeeded<p>");

	%>
</body>
</html>