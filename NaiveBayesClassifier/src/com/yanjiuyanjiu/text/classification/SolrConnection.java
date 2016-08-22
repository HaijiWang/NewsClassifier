package com.yanjiuyanjiu.text.classification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.*;
import java.util.*;

import org.apache.solr.client.solrj.SolrServer.*;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.*;

public class SolrConnection {

	private String SOLR_URL = null;

	private HttpSolrServer server = null;

	private final HashMap<String, String> query_params = new HashMap<String, String>(8);

	public SolrConnection() {
		SOLR_URL = "http://localhost:8983/solr/Test1"; // default
		// private final String SOLR_URL =
		// "http://172.24.60.110:8983/solr/test";
		// private final String SOLR_URL = "http://58.213.107.34/solr/";

		server = new HttpSolrServer(SOLR_URL);

		query_params.put("q", "*:*");
		query_params.put("fq", null);
		query_params.put("fl", null);
		query_params.put("start", "0");
		query_params.put("rows", "1"); // default to one row
		query_params.put("sort", null);
		query_params.put("hl", null);
		query_params.put("facet", null);
	}

	public void setSolrURL(String url) {
		SOLR_URL = url;
		server = new HttpSolrServer(SOLR_URL);
		// optional settings:
				server.setMaxRetries(1);
				server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
				server.setConnectionTimeout(5000); // 5 seconds to establish TCP
				server.setParser(new XMLResponseParser());
				server.setSoTimeout(1000); // socket read timeout
				server.setDefaultMaxConnectionsPerHost(100);
				server.setMaxTotalConnections(100);
				server.setFollowRedirects(false); // defaults to false
	}

	public void setParams(String key, String value) {
		if (query_params.containsKey(key)) {
			query_params.put(key, value);
		} else {
			System.out.println("query params does not have key:" + key);
		}

	}

	/**
	 * List<String> ids new ArrayList<String>() ids.add("the id");
	 */
	public void deleteContentById(List<String> ids) {
		System.out.println("Deleting Doc...");
		try {
			server.deleteById(ids);
			server.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Finished deleting Doc.");
	}

	/**
	 * Map<String,String> fieldModifier = new HashMap<String,String>();
	 * fieldModifier.put("set","George W"); doc_in.addField("president",
	 * fieldModifier);
	 */
	public void updateContent(Map<String, Object> update_params) throws SolrServerException, IOException {
		SolrInputDocument doc_in = new SolrInputDocument();

		Iterator<String> itr = update_params.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			
			doc_in.addField(key, update_params.get(key));
			itr.remove(); // avoids a ConcurrentModificationException
		}
		
		
		//System.out.println("Debug.");
		System.out.println("Debug: Doc_in: " + doc_in.toString());
		System.out.println("updating content...");
		UpdateResponse UD_response = server.add(doc_in);
		server.commit();
		System.out.println("Debug: "+UD_response.toString());
		System.out.println("Finished updating solr.");

	}

	public QueryResponse getContent() throws SolrServerException, IOException {
		
		SolrQuery query = new SolrQuery();
		Iterator<String> itr = query_params.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			query.set(key, query_params.get(key));
		}

		QueryResponse my_response = server.query(query);

		System.out.println("Successful! Executed SampleCrawler.getContent(params). Found:"
				+ my_response.getResults().getNumFound() + "rows.");

		return my_response;
	}
	
	//======================================================================================
	//========================= Main =======================================================
	
	public static void main_delete(String args[]){
	//public static void main(String args[]){
		SolrConnection tmp_conn = new SolrConnection();
		List<String> ids = new ArrayList<String>();
		//ids.add("3");
		//ids.add("8");
		//ids.add("7");
		ids.add("1677c8a1-8374-4c8a-9fe8-f3ad665b0b4a");
		tmp_conn.deleteContentById(ids);
	}

	public static void main_add(String args[]){
		SolrConnection tmp_conn = new SolrConnection();
		tmp_conn.setSolrURL("http://localhost:8983/solr/Test1");
		
		HashMap<String,Object> update_param = new HashMap<String,Object>();
		update_param.put("id","6");
		update_param.put("title","title2");
		try{
			tmp_conn.updateContent(update_param);
		} catch(Exception e){
			System.out.println(e);
		}
	}

	public static void main(String args[]) {

		QueryResponse solr_response = null;

		SolrConnection tmp_conn = new SolrConnection();
		tmp_conn.setParams("rows", "1");
		//tmp_conn.setSolrURL("http://58.213.107.34/solr/");
		try {
			solr_response = tmp_conn.getContent();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//SolrDocument doc = solr_response.getResults().get(0);
		for (SolrDocument doc : solr_response.getResults()){
			System.out.println("Got id" + doc.getFieldValue("id").toString());
			System.out.println("Got content: " + doc.getFieldValue("content").toString());
			
			HashMap<String,Object> update_param = new HashMap<String,Object>();
			
			Iterator<String> itr = doc.getFieldNames().iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				if (!key.equals("_version_")){
					update_param.put(key, doc.getFieldValue(key));
				}
				itr.remove(); // avoids a ConcurrentModificationException
			}
			System.out.println("Debug!!!!!");
			//update_param.put("id","2");
			//update_param.put("title","title2");
			tmp_conn.setSolrURL("http://localhost:8983/solr/Test1");
			try{
				System.out.println("Debug");
				tmp_conn.updateContent(update_param);
			} catch(Exception e){
				System.out.println(e);
			}
			
		}

		/*
		 * if (doc.getFieldValue("machineTags") != null) {
		 * System.out.println("MachineTags" +
		 * doc.getFieldValue("machineTags").toString()); } else {
		 * System.out.println("MachineTags: null"); }
		 */

	}

}
