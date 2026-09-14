package plagiarism;

/**
 * 相似度计算接口，定义查重算法的抽象。
 * 实现类负责给出两段文本的重复率，取值范围 [0.0, 1.0]。
 */
public interface SimilarityCalculator {

    /**
     * 计算两段文本的重复率。
     *
     * @param original     论文原文内容
     * @param plagiarized  抄袭版论文内容
     * @return 重复率，范围 [0.0, 1.0]，1.0 表示完全相同，0.0 表示完全不同
     */
    double compute(String original, String plagiarized);
}
