package com.yanjiuyanjiu.text.classification;

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
 * <b>Description:</b>中文分词</br> 
 * @author: 
 * @Date: 
 * @version 1.0
 */

//http://my.oschina.net/BreathL/blog/42477
//http://my.oschina.net/twosnail/blog/370744

public class CheckTheSame {
	
	/**
	 * 分词
	 * @author: Administrator
	 * @Date: 2015年1月22日
	 * @param str
	 * @return
	 */
public static Vector<String> participle( String str ) {
	
	Vector<String> str1 = new Vector<String>() ;//对输入进行分词
	
  try {
		
	    StringReader reader = new StringReader( str ); 
	    IKSegmenter ik = new IKSegmenter(reader,true);//当为true时，分词器进行最大词长切分 	    
	    Lexeme lexeme = null ;			
		
	    while( ( lexeme = ik.next() ) != null ) {
			str1.add( lexeme.getLexemeText() ); 
		}			
		
	    if( str1.size() == 0 ) {
	    	return null ;
	    }
	    
	  //分词后
	    System.out.println( "str分词后：" + str1 );
	    
	} catch ( IOException e1 ) {
		System.out.println();
	}
	
  
  try {
		
		//String text="基于java语言开发的轻量级的中文分词工具包";  
      //创建分词对象  
      Analyzer anal=new IKAnalyzer(true);       
      StringReader readerIK=new StringReader(str);  
      //分词  
      TokenStream ts=anal.tokenStream("", readerIK);  
      CharTermAttribute term=(CharTermAttribute)ts.getAttribute(CharTermAttribute.class); 
      ts.reset();
      //遍历分词数据  
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
		
		
		
		//分词
		Vector<String> strs1 = participle( "厨房的的的吊灯开" ) ;
		Vector<String> strs2 = participle( "请打开吊灯厨房" ) ;
		
		//相似度
		/*
		double same = 0 ;
		try {
			same = IKAnalyzerUtil.getSimilarity( strs1 , strs2 );
		} catch (Exception e) {
			System.out.println( e.getMessage() );
		}
		
		System.out.println( "相似度：" + same );
		*/
	   String b="厨房";
	   if(b.trim().equals("厨房")){
		   System.out.println( "相似度：" + "100%" );
	   }
		
	   
//	   NGramDistance ng = new NGramDistance();
//       float score1 = ng.getDistance("厨房吊灯开", "请打开吊灯厨房");
//       System.out.println(score1);
//       float score2 = ng.getDistance("girl", "girlfriend");
//       System.out.println(score2);
       
       
       
       String s = "dd add addd adddd 编码规范从根本上解决了程序维护员的难题；规范的编码阅读和理解起来更容易，也可以快速的不费力气的借鉴别人的编码。对将来维护你编码的人来说，你的编码越优化，他们就越喜欢你的编码，理解起来也就越快。";  
       StringReader sr = new StringReader(s); 
     //N-gram模型分词器  
       //Tokenizer tokenizer = new NGramTokenizer(Version.LUCENE_45,sr);
       

	}

}
