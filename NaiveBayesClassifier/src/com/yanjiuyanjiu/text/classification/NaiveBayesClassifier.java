package com.yanjiuyanjiu.text.classification;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * 朴素贝叶斯分类器.
 */
public class NaiveBayesClassifier {
	protected TrainnedModel model;
    
	protected transient IntermediateData db;
	
    //private transient ChineseSpliter textSpliter;

    public NaiveBayesClassifier() {
    	//textSpliter = new ChineseSpliter();
    }
    
    /* ===========================================================================*/
    /* =============================   Usage      ================================*/
    /** 打印命令行参数的解释信息. */
    private static void usage() {
    	// 根据中间数据文件，训练产生模型文件
        System.err.println("usage:\t -t <中间文件> <模型文件>");
        // 对已经分类好的文本库，用某个模型文件来分类，测试正确率
        System.err.println("usage:\t -r <语料库目录> <语料库文本编码> <模型文件>");
        // 用某一个训练好的模型文件，来分类某个文本文件
        System.err.println("usage:\t <模型文件> <文本文件> <文本文件编码>");
    }
    
    /* ===========================================================================*/
    /* ==================== Load Intermediate Data ================================*/
    private final void loadData(String intermediateData_path) {
		try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(
            		intermediateData_path));
            db = (IntermediateData) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
    /* ===========================================================================*/
    /* =============  Load Model created after training  ==========================*/
    /**
     * 加载数据模型文件.
     * 
     * @param modelFile
     *            模型文件路径
     */
	public final void loadModel(final String modelFile) {
        try {
        	System.out.println("model input stream.");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                    modelFile));
            System.out.println("model read input...");
            model = (TrainnedModel) in.readObject();
            System.out.println("model read input done!!.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	/* ========================================================================*/
    /* ============================= Train =====================================*/
    public final void train(String intermediateData_path, String modelFile_path) {
    	// 加载中间数据文件
    	loadData(intermediateData_path);
    	
    	model = new TrainnedModel(db.classifications.length);
    	
    	model.classifications = db.classifications;
    	model.vocabulary = db.vocabulary;
    	System.out.println("Vocabulary lengh: " + model.vocabulary.size());
    	System.out.println("Training model...");
    	// 开始训练
    	calculatePc();
    	calculatePxc();
    	db = null;
    	
    	try {
    		// 用序列化，将训练得到的结果存放到模型文件中
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(modelFile_path));
            out.writeObject(model);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	System.out.println("Trained model created! vocabulary lengh: "+model.vocabulary.size());
    }
    
	/* ===========================================================================*/
    /* ========================== Classify =======================================*/
    /**
     * 对给定的文本进行分类.
     * 
     * @param text
     *            给定的文本
     * @return 分类结果
     */
    public final String classify(final String text) {
        String[] terms = null;
        // 中文分词处理(分词后结果可能还包含有停用词）
        System.out.println("text splitting....");
        long start_time_1 = System.currentTimeMillis();
        
        
        //terms = textSpliter.split(text, " ").split(" ");
        //terms = ChineseSpliter.dropStopWords(terms); // 去掉停用词，以免影响分类
        
        terms = Chinese_parser.participle(text);
        System.out.println("Splitted terms: " 
        		+ terms[0] + " " 
        		+ terms[1] + " "
        		);
        
        long end_time_1 = System.currentTimeMillis();
        System.out.println("text splitting done! time: "+ (end_time_1 - start_time_1) + "ms");
        
        
        long start_time_2 = System.currentTimeMillis(); 
        double probility = 0.0;
        List<ClassifyResult> crs = new ArrayList<ClassifyResult>(); // 分类结果
        for (int i = 0; i < model.classifications.length; i++) {
            // 计算给定的文本属性向量terms在给定的分类Ci中的分类条件概率
            probility = calcProd(terms, i);
            // 保存分类结果
            ClassifyResult cr = new ClassifyResult();
            cr.classification = model.classifications[i]; // 分类
            cr.probility = probility; // 关键字在分类的条件概率
            System.out.println("In process....");
            System.out.println(model.classifications[i] + "：" + probility);
            crs.add(cr);
        }
        long end_time_2 = System.currentTimeMillis(); // 获取结束时间
        System.out.println("所有类别概率计算时间： " + (end_time_2 - start_time_2) + "ms");
        // 找出最大的元素
        ClassifyResult maxElem = (ClassifyResult) java.util.Collections.max(
                crs, new Comparator() {
                    public int compare(final Object o1, final Object o2) {
                        final ClassifyResult m1 = (ClassifyResult) o1;
                        final ClassifyResult m2 = (ClassifyResult) o2;
                        final double ret = m1.probility - m2.probility;
                        if (ret < 0) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

        return maxElem.classification;
    }
    
    
    
    
	
    
    
    
    /* ===========================================================================*/
    /* ========================= Get correct rate ================================*/
    public final double getCorrectRate(String classifiedDir, String encoding, String model) {
        int total = 0;
        int correct = 0;
        
        loadModel(model);
        
        File dir = new File(classifiedDir);
        
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("训练语料库搜索失败！ [" + classifiedDir
                    + "]");
        }

        String[] classifications = dir.list();
        for (String c : classifications) {
            String[] filesPath = IntermediateData.getFilesPath(classifiedDir, c);
            for (String file : filesPath) {
                total++;
                String text = null;
                try {
                    text = IntermediateData.getText(file, encoding);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String classification = classify(text);
                if(classification.equals(c)) { // 计算出的类别，和原始的类别是否相同
                    correct++;
                }
            }
        }
        return ((double)correct/(double)total) * 100;
    }
    
    /* ===========================================================================*/
    /* ============ Methods that will be defined by inheriting classes ==============*/
    /** 计算先验概率P(c). */
    protected void calculatePc() {
    }
    
    /** 计算类条件概率P(x|c). */
    protected void calculatePxc() {
    }
    
    
    /**
     * 计算文本属性向量X在类Cj下的后验概率P(Cj|X).
     * 
     * @param x
     *            文本属性向量
     * @param cj
     *            给定的类别
     * @return 后验概率
     */
    protected double calcProd(final String[] x, final int cj) {
        return 0;
    }
    
    /* ===========================================================================*/
    /* ======================== Top method: test  ================================*/
    public static void test(NaiveBayesClassifier classifier, String[] args) {
    	long startTime = System.currentTimeMillis(); // 获取开始时间
        if (args.length < 3) {
            usage();
            return;
        }

        if (args[0].equals("-t")) { // 训练
            classifier.train(args[1], args[2]);
            System.out.println("训练完毕");
        } 
        
        else if(args[0].equals("-r")) { // 获取正确率
            double ret = classifier.getCorrectRate(args[1], args[2], args[3]);
            System.out.println("正确率为：" + ret);
        } 
        
        else { // 分类
            classifier.loadModel(args[0]);

            String text = null;
            try {
                text = IntermediateData.getText(args[1], args[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            
            String result = classifier.classify(text); // 进行分类

            System.out.println("此属于[" + result + "]");
        }

        long endTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
    }
}
