package solr_classification.com;

import java.util.HashMap;
import java.util.Iterator;

public class MultiNomialNB extends NaiveBayesClassifier {
	protected void calculatePc() {
        for (int i = 0; i < db.classifications.length; i++) {
            model.setPc(i, (double)db.tokensOfC[i] / (double)db.tokens);
        }
    }
    

	protected void calculatePxc() {
		for (int i = 0; i < db.classifications.length; i++) {
			HashMap<String, Integer> source =  db.tokensOfXC[i];
			//HashMap<String, Double> target = model.pXC[i];
			
			for(Iterator<String> iter = db.vocabulary.iterator(); iter.hasNext();) {
				String t = iter.next();
				
				Integer value = source.get(t);
				if(value == null) { // ������²���������t
					value = 0;
				}
				model.setPxc(t, i, (double)(value + 1)/(double)(db.tokensOfC[i] + db.vocabulary.size()));
			}
		}
    }
    
	
	
    protected double calcProd(final String[] x, final int cj) {
        double ret = 0.0;
        // ��������������
        for (int i = 0; i < x.length; i++) {
            // ��Ϊ�����С�����������֮ǰ�Ŵ�10����������ս������Ӱ�죬��Ϊ����ֻ�ǱȽϸ��ʴ�С����
            ret += Math.log(model.getPxc(x[i], cj));
        }
        // �ٳ����������
        ret += Math.log(model.getPc(cj));
        return ret;
    }
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MultiNomialNB nb = new MultiNomialNB();
		MultiNomialNB.test(nb, args);
	}

}
