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

public class MyTest3_addSolr {
	public static final String SOLR_URL = "http://localhost:8983/solr/Test1";

	public static void main(String[] args) {
		//通过浏览器查看结果
		//http://172.168.63.233:8983/solr/collection1/select?q=name%3A%E6%94%B9%E9%9D%A9&wt=json&indent=true
		AddDocs();
		//delDocs();
	}

	public static void AddDocs() {
		
		
			SolrInputDocument doc1 = new SolrInputDocument();
			doc1.addField("bb", "7");
			//Map<String,String> fieldModifier = new HashMap<String,String>();
			//fieldModifier.put("set","GeorgeWBush"); 
			//doc1.addField("title",fieldModifier);
			doc1.addField("title","titleChanged");
			System.out.println("Debug:\nDoc_in: " + doc1.toString());
			
			
			
		try {
			HttpSolrServer server = new HttpSolrServer(SOLR_URL);
			server.setMaxRetries(1);
			server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
			server.setConnectionTimeout(5000); // 5 seconds to establish TCP
			server.setParser(new XMLResponseParser());
			server.setSoTimeout(1000); // socket read timeout
			server.setDefaultMaxConnectionsPerHost(100);
			server.setMaxTotalConnections(100);
			server.setFollowRedirects(false); // defaults to false
			
			UpdateResponse update_response = server.add(doc1);
			//server.add(doc1);
			server.commit();
			System.out.println(update_response.toString());
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("FInished adding:");
	}
}
