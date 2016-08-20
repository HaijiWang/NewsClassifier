<%@ page language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ page import="java.io.IOException"
		import="java.util.*"
		import="org.apache.solr.client.solrj.SolrQuery"
		import="org.apache.solr.client.solrj.SolrQuery.ORDER"
		import="org.apache.solr.client.solrj.SolrServerException"
		import="org.apache.solr.client.solrj.impl.BinaryRequestWriter"
		import="org.apache.solr.client.solrj.impl.HttpSolrServer"
		import="org.apache.solr.client.solrj.impl.XMLResponseParser"
		import="org.apache.solr.client.solrj.response.QueryResponse"
		import="org.apache.solr.common.SolrDocument" 
		import="solr_classification.com.SolrConnection" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%!private static String versionId = null; %>
	
	<%
	out.print(request.getQueryString());
	%>
	
	<%!
	//private static final String SOLR_URL = "http://58.213.107.34/solr/";
	%>
	<%!/*
	private static QueryResponse getContent(String solrUrl) throws SolrServerException, IOException{
		HttpSolrServer server = new HttpSolrServer(solrUrl);
		
		// optional settings: 
		server.setMaxRetries(1);
		server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
		server.setConnectionTimeout(5000); // 5 seconds to establish TCP
		server.setParser(new XMLResponseParser());
		server.setSoTimeout(1000); // socket read timeout
		server.setDefaultMaxConnectionsPerHost(100);
		server.setMaxTotalConnections(100);
		server.setFollowRedirects(false); // defaults to false
		
		// allowCompression defaults to false.
		// Server side must support gzip or deflate for this to have any effect.
		server.setAllowCompression(true);

		//server.setRequestWriter(new BinaryRequestWriter());
		SolrQuery query = new SolrQuery();
		query.setQuery("content:医");
		query.setFields("id","content");
		//query.setSort("price", ORDER.asc);
		query.setStart(0);
		query.setRows(1);
		//query.set("wt","xml");
		//query.setRequestHandler("/select");
		QueryResponse my_response = server.query( query );
		
		// Get results
		System.out.println("Found:" + my_response.getResults().getNumFound());
		
		return my_response;
	}
	*/
	%>
	
		
		
	<%
		try {
			HashMap<String, String> query_params = new HashMap<String, String>();
			query_params.put("q",
					"content:急诊 AND content:门诊 AND content:住院 AND content: 质量 AND content:服务 AND content:整形");
			query_params.put("fq", null);
			query_params.put("fl", "id,_version_,content,machineTags,artificialTags");
			query_params.put("start", "0");
			query_params.put("rows", "1");
			query_params.put("sort", null);
			query_params.put("hl", null);
			query_params.put("facet", null);
			
		
			SolrConnection sConn= new SolrConnection(); 
			QueryResponse solr_response = sConn.getContent(query_params);
			int iRow = 1;
			for (SolrDocument doc : solr_response.getResults()) {
				System.out.println("----------" + iRow + "------------");
				System.out.println("version: " + doc.getFieldValue("_version_").toString());
				System.out.println("content: " + doc.getFieldValue("content").toString());
				
				
				versionId = doc.getFieldValue("_version_").toString();
				
				out.println("<table border=2>");
				out.println("<tr><th>");
				out.println("version: " + doc.getFieldValue("_version_").toString());
				out.println("</th><th>");
				out.println("content: " + doc.getFieldValue("content").toString());
				out.println("</th></tr>");
				out.println("</table>");
				out.println();
				
				
				
				if (doc.getFieldValue("machineTags")!=null){
					out.println("Machine class:" + doc.getFieldValue("machineTags"));
				} 
				else{
					out.println("Machine class: null"); 
				}
				
				iRow++;
			}
		} catch(Exception e){
			System.out.println(e);
		}
	%>
	
	<form action="test1.jsp" method="GET" target="_blank">
	<% 
	out.println("<input type = \"hidden\" name = \"version\" value = \"" + versionId + "\" />");
	%>

	<input type="checkbox" name="medical" checked="checked" /> medical
	<input type="checkbox" name="non_medical"  /> non_medical
	<input type="submit" value="submit" />
	</form>
		
		
	
	



  
</body>
</html>