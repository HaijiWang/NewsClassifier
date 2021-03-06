<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ page import="java.io.IOException" import="java.util.*"
	import="org.apache.solr.client.solrj.SolrQuery"
	import="org.apache.solr.client.solrj.SolrQuery.ORDER"
	import="org.apache.solr.client.solrj.SolrServerException"
	import="org.apache.solr.client.solrj.impl.BinaryRequestWriter"
	import="org.apache.solr.client.solrj.impl.HttpSolrServer"
	import="org.apache.solr.client.solrj.impl.XMLResponseParser"
	import="org.apache.solr.client.solrj.response.QueryResponse"
	import="org.apache.solr.common.SolrDocument"
	import="solr_connect_new.SolrConnection"
	
	import="org.apache.lucene.queryParser.QueryParser"
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>News Classifier</title>
</head>
<body>
	<%!private static String id = null; 
	   private static SolrConnection sConn= new SolrConnection();
	   private static QueryResponse solr_response = new QueryResponse();
	   private static SolrDocument doc = new SolrDocument();
	%>
	<%
	sConn.setSolrURL("http://localhost:8983/solr/Test1/");
	sConn.resetQueryParams();
	%>
	
	
	
	<%
	
	// Find classification name and set ArtificialTags
	ArrayList<String> classes = sConn.getClasses();
	Enumeration<String> class_names = request.getParameterNames();
	String oneClass = null;
	while (class_names.hasMoreElements()){
		oneClass = class_names.nextElement();
		if(classes.contains(oneClass) && request.getParameter(oneClass).equals("on")){
			System.out.println("Tag as:"+oneClass);
			break;
		};
	}
	out.println("request string: "+request.getQueryString()+"\n");
	
	if (request.getParameter("id")!=null){
	/*
	// Test Delete
	List<String> ids = new ArrayList<String>();
	ids.add(QueryParser.escape(request.getParameter("id")));
	sConn.deleteContentById(ids);
	*/
	
	// Update ArtificialTags
	
	System.out.println("Debug: Going to update id:"+QueryParser.escape(request.getParameter("id")));
	Map<String, Object> update_params = new HashMap<String, Object>();
	Map<String,String> fieldModifier = new HashMap<String,String>();
	fieldModifier.put("set",oneClass); 
	update_params.put("artificialTags",fieldModifier);
	update_params.put("id",request.getParameter("id"));
	// Somehow it does not have to be parsed, what can I say !!!!!
	//update_params.put("id",QueryParser.escape(request.getParameter("id"))); 
	
	sConn.updateContent(update_params);
	
	
	// Verify the previous update is successful
	//sConn.resetQueryParams();
	HashMap<String,String>  params  = new HashMap<String,String>();
	params.put("q", "id:" + QueryParser.escape(request.getParameter("id")));
	params.put("start","0");
	params.put("rows","1");
	sConn.setQueryParams(params);
	
	//sConn.setQueryParams("q", "id:" + QueryParser.escape(request.getParameter("id")));
	//sConn.setQueryParams("start", "0");
	//sConn.setQueryParams("rows", "1");	
	
	try {
		solr_response = sConn.getContent();	
		//System.out.println("Degbug: solr_response: "+solr_response.toString());
	} catch(Exception e){
		System.out.println(e);
	}
	doc = solr_response.getResults().get(0);
	id = doc.getFieldValue("id").toString();
	System.out.println("Again got : id: " + id);
	System.out.println("Again got : content:" + doc.getFieldValue("content").toString());
	System.out.println("ArtificialTags: " + doc.getFieldValue("artificialTags").toString());
	}
	%>

	<%
	
		//QueryResponse solr_response = new QueryResponse();
		
		//sConn.setSolrURL("http://172.24.60.110:8983/solr/test");
		//sConn.setSolrURL("http://localhost:8983/solr/Test1/");
		//sConn.setQueryParams("q","content:急诊 AND content:门诊 AND content:住院 AND content: 质量 AND content:服务 AND content:整形");
		//sConn.resetQueryParams();
		Random rn = new Random();
		int startNum = rn.nextInt(10);
		
		System.out.println("random number: "+startNum);
		sConn.resetQueryParams();
		
		HashMap<String,String>  params  = new HashMap<String,String>();
		params.put("start", String.valueOf(startNum));
		params.put("rows","1");
		sConn.setQueryParams(params);
		//sConn.setQueryParams("start", String.valueOf(startNum));
		//sConn.setQueryParams("rows", "1");	
		try {
			solr_response = sConn.getContent();
			//System.out.println("Degbug: solr_response_2: "+solr_response.toString());
		} catch(Exception e){
			System.out.println(e);
		}
		
		doc = solr_response.getResults().get(0);
		id = doc.getFieldValue("id").toString();
		System.out.println("id : " + id);
		System.out.println("content: " + doc.getFieldValue("content").toString());
		
		out.println("<table border=2>");
		out.println("<tr><th>");
		out.println("id: ");
		out.println("</th><th>");
		out.println("content");
		out.println("</th></tr>");
		out.println("<tr><td>");
		out.println(id);
		out.println("</td><td>");
		out.println(doc.getFieldValue("content").toString());
		out.println("</td></tr>");
		out.println("</table>");
		out.println();
			
		if (doc.getFieldValue("machineTags") != null) {
			out.println("<p>Machine class:" + doc.getFieldValue("machineTags")+"</p>");
		} else {
			out.println("<p>Machine class: null</p>");
		}
		if (doc.getFieldValue("artificialTags") != null) {
			out.println("<p>artificial class:" + doc.getFieldValue("artificialTags")+"</p>");
		} else {
			out.println("<p>artificial class: null</p>");
		}
	%>

	<form action="test1.jsp" name="classification" method="GET" target="_blank" 
	onsubmit="alert('Are you sure?')">
	<% 
	out.println("<input type = \"hidden\" name = \"id\" value = \"" + id + "\" />");
	%>
	<input type="checkbox" name="medical" checked="checked" /> medical 
	<input type="checkbox" name="non_medical" /> non_medical 
	<input type="submit" value="submit"/>
	</form>








</body>
</html>