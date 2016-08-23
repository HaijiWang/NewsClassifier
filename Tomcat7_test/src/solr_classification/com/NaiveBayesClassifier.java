package solr_classification.com;


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
 * ���ر�Ҷ˹������.
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
    /** ��ӡ�����в����Ľ�����Ϣ. */
    private static void usage() {
    	// �����м������ļ���ѵ������ģ���ļ�
        System.err.println("usage:\t -t <�м��ļ�> <ģ���ļ�>");
        // ���Ѿ�����õ��ı��⣬��ĳ��ģ���ļ������࣬������ȷ��
        System.err.println("usage:\t -r <���Ͽ�Ŀ¼> <���Ͽ��ı�����> <ģ���ļ�>");
        // ��ĳһ��ѵ���õ�ģ���ļ���������ĳ���ı��ļ�
        System.err.println("usage:\t <ģ���ļ�> <�ı��ļ�> <�ı��ļ�����>");
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
     * ��������ģ���ļ�.
     * 
     * @param modelFile
     *            ģ���ļ�·��
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
    	// �����м������ļ�
    	loadData(intermediateData_path);
    	
    	model = new TrainnedModel(db.classifications.length);
    	
    	model.classifications = db.classifications;
    	model.vocabulary = db.vocabulary;
    	System.out.println("Vocabulary lengh: " + model.vocabulary.size());
    	System.out.println("Training model...");
    	// ��ʼѵ��
    	calculatePc();
    	calculatePxc();
    	db = null;
    	
    	try {
    		// �����л�����ѵ���õ��Ľ����ŵ�ģ���ļ���
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
     * �Ը������ı����з���.
     * 
     * @param text
     *            �������ı�
     * @return ������
     */
    public final String classify(final String text) {
        String[] terms = null;
        // ���ķִʴ���(�ִʺ������ܻ�������ͣ�ôʣ�
        System.out.println("text splitting....");
        long start_time_1 = System.currentTimeMillis();
        
        
        //terms = textSpliter.split(text, " ").split(" ");
        //terms = ChineseSpliter.dropStopWords(terms); // ȥ��ͣ�ôʣ�����Ӱ�����
        
        terms = Chinese_parser.participle(text);
        System.out.println("Splitted terms: " 
        		+ terms[0] + " " 
        		+ terms[1] + " "
        		);
        
        long end_time_1 = System.currentTimeMillis();
        System.out.println("text splitting done! time: "+ (end_time_1 - start_time_1) + "ms");
        
        
        long start_time_2 = System.currentTimeMillis(); 
        double probility = 0.0;
        List<ClassifyResult> crs = new ArrayList<ClassifyResult>(); // ������
        for (int i = 0; i < model.classifications.length; i++) {
            // ����������ı���������terms�ڸ����ķ���Ci�еķ�����������
            probility = calcProd(terms, i);
            // ���������
            ClassifyResult cr = new ClassifyResult();
            cr.classification = model.classifications[i]; // ����
            cr.probility = probility; // �ؼ����ڷ������������
            System.out.println("In process....");
            System.out.println(model.classifications[i] + "��" + probility);
            crs.add(cr);
        }
        long end_time_2 = System.currentTimeMillis(); // ��ȡ����ʱ��
        System.out.println("���������ʼ���ʱ�䣺 " + (end_time_2 - start_time_2) + "ms");
        // �ҳ�����Ԫ��
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
            throw new IllegalArgumentException("ѵ�����Ͽ�����ʧ�ܣ� [" + classifiedDir
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
                if(classification.equals(c)) { // ���������𣬺�ԭʼ������Ƿ���ͬ
                    correct++;
                }
            }
        }
        return ((double)correct/(double)total) * 100;
    }
    
    /* ===========================================================================*/
    /* ============ Methods that will be defined by inheriting classes ==============*/
    /** �����������P(c). */
    protected void calculatePc() {
    }
    
    /** ��������������P(x|c). */
    protected void calculatePxc() {
    }
    
    
    /**
     * �����ı���������X����Cj�µĺ������P(Cj|X).
     * 
     * @param x
     *            �ı���������
     * @param cj
     *            ���������
     * @return �������
     */
    protected double calcProd(final String[] x, final int cj) {
        return 0;
    }
    
    /* ===========================================================================*/
    /* ======================== Top method: test  ================================*/
    public static void test(NaiveBayesClassifier classifier, String[] args) {
    	long startTime = System.currentTimeMillis(); // ��ȡ��ʼʱ��
        if (args.length < 3) {
            usage();
            return;
        }

        if (args[0].equals("-t")) { // ѵ��
            classifier.train(args[1], args[2]);
            System.out.println("ѵ�����");
        } 
        
        else if(args[0].equals("-r")) { // ��ȡ��ȷ��
            double ret = classifier.getCorrectRate(args[1], args[2], args[3]);
            System.out.println("��ȷ��Ϊ��" + ret);
        } 
        
        else { // ����
            classifier.loadModel(args[0]);

            String text = null;
            try {
                text = IntermediateData.getText(args[1], args[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            
            String result = classifier.classify(text); // ���з���

            System.out.println("������[" + result + "]");
        }

        long endTime = System.currentTimeMillis(); // ��ȡ����ʱ��
        System.out.println("��������ʱ�䣺 " + (endTime - startTime) + "ms");
    }
}
