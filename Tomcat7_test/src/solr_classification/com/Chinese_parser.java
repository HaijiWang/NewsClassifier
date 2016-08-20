package solr_classification.com;

import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute; 
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Chinese_parser {
	
    
	public Chinese_parser() {
    }
	
	/**
	 * JE splitter
	 * @param text
	 * @return
	 */
	
	/*
	// Has to inclue lib/lucene-core-2.92.jar in build-path, replacing lucene-core-5.51.jar
	public static String[] participle( String text ) {
		System.out.println("Using JE analyzer");
		ChineseSpliter textSpliter = new ChineseSpliter();
		String[] terms = null;
		terms = textSpliter.split(text, " ").split(" ");
        terms = ChineseSpliter.dropStopWords(terms); // ȥ��ͣ�ôʣ�����Ӱ�����
        return terms;
	}
	*/
	
	// Use lucene-core-5.51.jar
	public static String[] participle( String text ) {	
		System.out.println("Using IK analyzer");
		  Vector<String> result_str = new Vector<String>();
		  try {
			  // IK_analyzer
			  //String text="����java���Կ����������������ķִʹ��߰�";  
		      //�����ִʶ���  
		      Analyzer anal=new IKAnalyzer(true);       
		      StringReader readerIK=new StringReader(text);  
		      //�ִ�  
		      TokenStream ts=anal.tokenStream("", readerIK);  
		      CharTermAttribute term=(CharTermAttribute) ts.getAttribute(CharTermAttribute.class); 
		      ts.reset();
		      //�����ִ�����  
		      while(ts.incrementToken()){  
		          result_str.add(term.toString());  
		      }  
		      anal.close();
		      readerIK.close();  
		      //System.out.println();
			    
			} catch ( IOException e1 ) {
				System.out.println("oops");
				System.out.println();
			}
		    		
			return result_str.toArray(new String[result_str.size()]);
			
	}
	
	
	
	public static void main(String[] args) {
		String[] my_str = participle("��򿪵��Ƴ���");
		System.out.println(my_str[0]+"|"+my_str[1]);

	}
	
}
