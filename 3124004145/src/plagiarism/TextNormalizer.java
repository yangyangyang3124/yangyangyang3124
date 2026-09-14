package plagiarism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文本预处理工具：归一化与 n-gram 切分。
 */
public final class TextNormalizer {

    private TextNormalizer() {
    }

    /**
     * 归一化文本：去除标点、空白符，仅保留中文汉字、英文字母与数字，英文统一转小写。
     *
     * @param text 原始文本，可为 null
     * @return 归一化后的文本（不含标点与空白）
     */
    public static String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (isCjk(ch) || isAsciiLetterOrDigit(ch)) {
                builder.append(isAsciiLetter(ch) ? Character.toLowerCase(ch) : ch);
            }
        }
        return builder.toString();
    }

    /**
     * 将已归一化的文本切分为指定长度的 n-gram。
     * 若文本长度不足 n，则退化为单个 unigram（整个文本作为一个 token），
     * 以保证极短文本（如单个字符）也能被正确比较。
     *
     * @param normalizedText 已归一化文本
     * @param n              gram 长度
     * @return n-gram 列表
     */
    public static List<String> ngrams(String normalizedText, int n) {
        List<String> result = new ArrayList<>();
        if (normalizedText == null || normalizedText.isEmpty()) {
            return result;
        }
        if (normalizedText.length() < n) {
            result.add(normalizedText);
            return result;
        }
        for (int i = 0; i + n <= normalizedText.length(); i++) {
            result.add(normalizedText.substring(i, i + n));
        }
        return result;
    }

    /**
     * 统计 n-gram 词频。
     *
     * @param normalizedText 已归一化文本
     * @param n              gram 长度
     * @return gram -> 出现次数 的映射
     */
    public static Map<String, Integer> termFrequency(String normalizedText, int n) {
        Map<String, Integer> frequency = new HashMap<>();
        if (normalizedText == null || normalizedText.isEmpty()) {
            return frequency;
        }
        // 滑动窗口单遍扫描：边切分边统计，避免先物化出全部 n-gram 的中间列表，
        // 从而省去一次遍历与 O(n) 的中间 List 内存开销。
        int length = normalizedText.length();
        if (length < n) {
            frequency.put(normalizedText, 1);
            return frequency;
        }
        for (int i = 0; i + n <= length; i++) {
            frequency.merge(normalizedText.substring(i, i + n), 1, Integer::sum);
        }
        return frequency;
    }

    /** 判断是否为中日韩统一表意文字（常用汉字范围）。 */
    private static boolean isCjk(char ch) {
        return ch >= '一' && ch <= '龥';
    }

    private static boolean isAsciiLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    private static boolean isAsciiLetterOrDigit(char ch) {
        return isAsciiLetter(ch) || (ch >= '0' && ch <= '9');
    }
}
