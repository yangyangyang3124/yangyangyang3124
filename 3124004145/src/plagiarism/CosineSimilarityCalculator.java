package plagiarism;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 基于字符 n-gram 词频向量的余弦相似度查重算法。
 *
 * <p>思路：</p>
 * <ol>
 *   <li>对文本做归一化（去除标点、空白，仅保留中英文与数字，英文转小写）；</li>
 *   <li>将文本切分为字符 bigram（长度不足 2 的文本退化为 unigram）；</li>
 *   <li>统计每个 gram 的词频，构造词频向量；</li>
 *   <li>计算两个向量的余弦相似度作为重复率。</li>
 * </ol>
 *
 * <p>余弦相似度对文本整体的增删改较为鲁棒，且结果天然落在 [0, 1] 区间内，适合直接作为重复率输出。</p>
 */
public final class CosineSimilarityCalculator implements SimilarityCalculator {

    /** 默认使用 2-gram（字符二元组）。 */
    private static final int NGRAM = 2;

    @Override
    public double compute(String original, String plagiarized) {
        String normalizedOriginal = TextNormalizer.normalize(original);
        String normalizedPlagiarized = TextNormalizer.normalize(plagiarized);

        // 两个空文本视为完全相同；一个空一个非空则完全不同。
        if (normalizedOriginal.isEmpty() && normalizedPlagiarized.isEmpty()) {
            return 1.0;
        }
        if (normalizedOriginal.isEmpty() || normalizedPlagiarized.isEmpty()) {
            return 0.0;
        }

        Map<String, Integer> freqOriginal = TextNormalizer.termFrequency(normalizedOriginal, NGRAM);
        Map<String, Integer> freqPlagiarized = TextNormalizer.termFrequency(normalizedPlagiarized, NGRAM);

        double dotProduct = 0.0;
        Set<String> keys = new HashSet<>(freqOriginal.keySet());
        keys.addAll(freqPlagiarized.keySet());
        for (String key : keys) {
            dotProduct += (double) freqOriginal.getOrDefault(key, 0) * freqPlagiarized.getOrDefault(key, 0);
        }

        double denominator = norm(freqOriginal) * norm(freqPlagiarized);
        if (denominator == 0.0) {
            return 0.0;
        }
        return dotProduct / denominator;
    }

    /** 计算词频向量的欧氏范数。 */
    private double norm(Map<String, Integer> frequencies) {
        double sumOfSquares = 0.0;
        for (int count : frequencies.values()) {
            sumOfSquares += (double) count * count;
        }
        return Math.sqrt(sumOfSquares);
    }
}
