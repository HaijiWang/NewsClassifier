package com.yanjiuyanjiu.text.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

public class LocalSample {

	private File path = new File("./data/");
	private HashMap<Integer, String> classifications = new HashMap<Integer, String>();

	public LocalSample() {

	}

	public void queryClasses() {
		// query solr for classes
		classifications.put(new Integer(0), "medical");
		classifications.put(new Integer(1), "non_medical");

	}

	public void allocateFiles(File dest_path, HashMap<String, String> query_params) {

		

		// Query for content
		QueryResponse solr_response = null;

		try {
			SolrConnection_old my_crawler = new SolrConnection_old();
			solr_response = my_crawler.getContent(query_params);
		} catch (Exception e) {
			System.out.println("Error: solrConnection.getContent() called by LocalSample.allocateFiles().");
			System.out.println(e);
		}

		try {
			// Write to file
			
			String dest_fileName = null;
			File dest_file = null;
			
			OutputStreamWriter osw = null;

			if (dest_path.exists() && dest_path.isDirectory()) {

				int iRow = 1;
				for (SolrDocument doc : solr_response.getResults()) {
					System.out.println("----------" + iRow + "------------");
					System.out.println("id: " + doc.getFieldValue("id").toString());
					System.out.println("_version_: " + doc.getFieldValue("_version_").toString());
					if (doc.getFieldValue("artificialTags") != null){
						System.out.println("artifical Tag: " + doc.getFieldValue("artificialTags").toString());
					} else {
						System.out.println("artifical Tag: null");
					}
					if (doc.getFieldValue("machineTags") != null){
						System.out.println("machine Tag: " + doc.getFieldValue("machineTags").toString());
					} else {
						System.out.println("machine Tag: null");
					}
					

					//System.out.println("content: " + doc.getFieldValue("content").toString());

					dest_fileName = doc.getFieldValue("_version_").toString().concat(".txt");
					dest_file = new File(dest_path, dest_fileName);

					System.out.println("Writing file " + dest_fileName + "to local...");
					try {
						osw = new OutputStreamWriter(new FileOutputStream(dest_file),"UTF-8");
						osw.write(doc.getFieldValue("content").toString());
					} catch (Exception e) {
						System.out.println("Error: writer to " + dest_file + "failed.");
						System.out.println(e);
					} finally {
						try {
							osw.flush();
							osw.close();
						} catch (Exception e) {
							System.out.println(e);
						}
					}
					System.out.println("Finished writing file (" + dest_file + ").");
					iRow++;
				}
			} else {
				System.out.println("Error: destination path (" + dest_file + ") does not exist.");
			}
		} catch (Exception e) {
			System.out.println("Error: in LocalSample.allocateFiles().");
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		File dest_path_1 = new File("./data/Sample3_solr/medical/");
		File dest_path_2 = new File("./data/Sample3_solr/non_medical/");
		LocalSample tmp = new LocalSample();
		
		HashMap<String, String> query_params = new HashMap<String, String>();

		//query_params.put("q",
		//		"content:���� AND content:���� AND content:סԺ AND content: ���� AND content:���� AND content:���� AND content:�����");
		query_params.put("q",
				"content:���� AND content:���� AND content:סԺ AND content: ���� AND content:����");
		
		query_params.put("fq", null);
		query_params.put("fl", "id,content,_version_,artificialTags,machineTags");
		query_params.put("start", "0");
		query_params.put("rows", "100");
		query_params.put("sort", null);
		query_params.put("hl", null);
		query_params.put("facet", null);
		
		//tmp.allocateFiles(dest_path_1,query_params);
		
		query_params.put("q",
				"!content:���� !content:���� !content:סԺ !content: ���� !content:����");
		
		query_params.put("fq", null);
		query_params.put("rows", "50");
		tmp.allocateFiles(dest_path_2,query_params);
		
	}

}
