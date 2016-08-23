package solr_classification.com;

/**
 * ͣ�ôʴ�����.
 * 
 * @author phinecos
 */
public final class StopWordsHandler {
    /** ��ֹʵ����. */
    private StopWordsHandler() {
    }

    /** ����ͣ�ô�. */
    private static String[] stopWordsList = {
            // ���� c:\Windows\System32\NOISE.CHS
            "��", "һ", "��", "��", "��", "��", "��", "Ϊ", "��", "��", "��", "��", "��",
            "��", "֮", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "Ҳ",
            "��", "��", "��", "��", "��", "��", "��", "С", "��", "��", "��", "��", "��",
            "��", "ȥ", "��", "ֻ", "��", "��", "��", "��", "ĳ", "��", "��", "��", "��",
            "��",
            // ��������
            "Ҫ", "��", "Ӧ", "λ", "��", "��", "��", "��", "����", "�Լ�", "û��", "��", "��",
            "��", "��", "��", "" };

    /**
     * �ж�һ�����Ƿ���ֹͣ��.
     * 
     * @param word
     *            Ҫ�жϵĴ�
     * @return ��ֹͣ�ʣ�����true�����򷵻�false
     */
    public static boolean isStopWord(final String word) {
        for (int i = 0; i < stopWordsList.length; ++i) {
            if (word.equalsIgnoreCase(stopWordsList[i])) {
                return true;
            }
        }
        return false;
    }
}
