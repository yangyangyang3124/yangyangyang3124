package plagiarism;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * 论文查重主流程：读取原文与抄袭版文件 -> 计算重复率 -> 将结果写入答案文件。
 */
public final class PlagiarismChecker {

    private final SimilarityCalculator similarityCalculator;

    public PlagiarismChecker(SimilarityCalculator similarityCalculator) {
        this.similarityCalculator = similarityCalculator;
    }

    /**
     * 执行一次完整的查重。
     *
     * @param originalPath     论文原文文件的绝对路径
     * @param plagiarizedPath  抄袭版论文文件的绝对路径
     * @param answerPath       答案文件的绝对路径
     * @throws FileReadException  读取输入文件失败时
     * @throws FileWriteException 写入答案文件失败时
     */
    public void check(String originalPath, String plagiarizedPath, String answerPath) {
        String originalText = readText(originalPath);
        String plagiarizedText = readText(plagiarizedPath);

        double similarity = similarityCalculator.compute(originalText, plagiarizedText);

        // 输出浮点型，精确到小数点后两位。
        String result = String.format(Locale.ROOT, "%.2f", similarity);
        writeText(answerPath, result);
    }

    private String readText(String path) {
        try {
            return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileReadException("无法读取文件：" + path, e);
        }
    }

    private void writeText(String path, String content) {
        try {
            Files.writeString(Paths.get(path), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileWriteException("无法写入答案文件：" + path, e);
        }
    }
}
