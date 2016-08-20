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


//import org.apache.lucene.search.spell.*;
//
//
//import java.util.Iterator;  
//import org.apache.lucene.analysis.Tokenizer;  
//import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;  
//import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;  
//import org.apache.lucene.analysis.ngram.Lucene43EdgeNGramTokenizer;  
//import org.apache.lucene.analysis.ngram.Lucene43NGramTokenizer;  
//import org.apache.lucene.analysis.ngram.NGramTokenizer;  
//import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;  
//import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;  
//import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;  
//import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;  
//import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;  
//import org.apache.lucene.analysis.tokenattributes.TypeAttribute;  
//import org.apache.lucene.util.Attribute;  
//import org.apache.lucene.util.Version;

/**
 * <b>Description:</b>���ķִ�</br> 
 * @author: 
 * @Date: 
 * @version 1.0
 */

//http://my.oschina.net/BreathL/blog/42477
//http://my.oschina.net/twosnail/blog/370744

public class CheckTheSame {
	
	/**
	 * �ִ�
	 * @author: Administrator
	 * @Date: 2015��1��22��
	 * @param str
	 * @return
	 */
public static Vector<String> participle( String str ) {
	
	Vector<String> str1 = new Vector<String>() ;//��������зִ�
	
  try {
		
	    StringReader reader = new StringReader( str ); 
	    IKSegmenter ik = new IKSegmenter(reader,true);//��Ϊtrueʱ���ִ����������ʳ��з� 	    
	    Lexeme lexeme = null ;			
		
	    while( ( lexeme = ik.next() ) != null ) {
			str1.add( lexeme.getLexemeText() ); 
		}			
		
	    if( str1.size() == 0 ) {
	    	return null ;
	    }
	    
	  //�ִʺ�
	    System.out.println( "str�ִʺ�" + str1 );
	    
	} catch ( IOException e1 ) {
		System.out.println();
	}
	
  
  try {
		
		//String text="����java���Կ����������������ķִʹ��߰�";  
      //�����ִʶ���  
      Analyzer anal=new IKAnalyzer(true);       
      StringReader readerIK=new StringReader(str);  
      //�ִ�  
      TokenStream ts=anal.tokenStream("", readerIK);  
      CharTermAttribute term=(CharTermAttribute)ts.getAttribute(CharTermAttribute.class); 
      ts.reset();
      //�����ִ�����  
      while(ts.incrementToken()){  
          System.out.print(term.toString()+"|");  
      }  
      anal.close();
      readerIK.close();  
      System.out.println();
	    
	} catch ( IOException e1 ) {
		System.out.println();
	}
	
	
    
	
	return str1;
}


//private static void testtokenizer(Tokenizer tokenizer) {  
//    
//    try {         
//        /*            
//        Iterator<Class<? extends Attribute>> iterator = tokenizer 
//                .getAttributeClassesIterator(); 
//        while (iterator.hasNext()) { 
//            Class<? extends Attribute> attrClass = iterator.next(); 
//            System.out.println(attrClass.getSimpleName()); 
//        }*/                       
//        tokenizer.reset();  
//        while(tokenizer.incrementToken())  
//        {  
//            //CharTermAttribute  
//            //TermToBytesRefAttribute  
//            //PositionIncrementAttribute  
//            //PositionLengthAttribute  
//            //OffsetAttribute  
//            CharTermAttribute charTermAttribute=tokenizer.addAttribute(CharTermAttribute.class);  
//            TermToBytesRefAttribute termToBytesRefAttribute=tokenizer.addAttribute(TermToBytesRefAttribute.class);  
//            PositionIncrementAttribute positionIncrementAttribute=tokenizer.addAttribute(PositionIncrementAttribute.class);  
//            PositionLengthAttribute positionLengthAttribute=tokenizer.addAttribute(PositionLengthAttribute.class);  
//            OffsetAttribute offsetAttribute=tokenizer.addAttribute(OffsetAttribute.class);  
//            TypeAttribute typeAttribute = tokenizer.addAttribute(TypeAttribute.class);  
//            //System.out.println(attribute.toString());  
//            System.out.println("term="+charTermAttribute.toString()+","+offsetAttribute.startOffset()+"-"+offsetAttribute.endOffset()  
//                    +",type="+typeAttribute.type()+",PositionIncrement="+positionIncrementAttribute.getPositionIncrement()  
//                    +",PositionLength="+positionLengthAttribute.getPositionLength());  
//              
//        }             
//        tokenizer.end();  
//        tokenizer.close();  
//    } catch (IOException e) {  
//        e.printStackTrace();  
//    }         
//}  
	
	public static void main(String[] args) {
		
		
		
		//�ִ�
		Vector<String> strs1 = participle( "�����ĵĵĵ��ƿ�" ) ;
		Vector<String> strs2 = participle( "��򿪵��Ƴ���" ) ;
		
		//���ƶ�
		/*
		double same = 0 ;
		try {
			same = IKAnalyzerUtil.getSimilarity( strs1 , strs2 );
		} catch (Exception e) {
			System.out.println( e.getMessage() );
		}
		
		System.out.println( "���ƶȣ�" + same );
		*/
	   String b="����";
	   if(b.trim().equals("����")){
		   System.out.println( "���ƶȣ�" + "100%" );
	   }
		
	   
//	   NGramDistance ng = new NGramDistance();
//       float score1 = ng.getDistance("�������ƿ�", "��򿪵��Ƴ���");
//       System.out.println(score1);
//       float score2 = ng.getDistance("girl", "girlfriend");
//       System.out.println(score2);
       
       
       
       String s = "dd add addd adddd ����淶�Ӹ����Ͻ���˳���ά��Ա�����⣻�淶�ı����Ķ���������������ף�Ҳ���Կ��ٵĲ��������Ľ�����˵ı��롣�Խ���ά������������˵����ı���Խ�Ż������Ǿ�Խϲ����ı��룬�������Ҳ��Խ�졣";  
       StringReader sr = new StringReader(s); 
     //N-gramģ�ͷִ���  
       //Tokenizer tokenizer = new NGramTokenizer(Version.LUCENE_45,sr);
       

	}

}
