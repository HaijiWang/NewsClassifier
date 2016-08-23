package solr_classification.com;

/**
 * ������.
 */
public class ClassifyResult {
    /** ����ĸ���. */
    public double probility;
    /** �����. */
    public String classification;

    /** ���캯��. */
    public ClassifyResult() {
        this.probility = 0;
        this.classification = null;
    }
}
