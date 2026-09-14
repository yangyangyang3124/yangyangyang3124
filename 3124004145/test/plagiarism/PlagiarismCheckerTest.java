package plagiarism;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * {@link PlagiarismChecker} 的单元测试，覆盖完整文件读写流程与异常处理。
 */
public final class PlagiarismCheckerTest {

    private final PlagiarismChecker checker = new PlagiarismChecker(new CosineSimilarityCalculator());

    public void register(TestRunner runner) {
        final Path tempDir;
        final Path original;
        final Path plagiarized;
        final Path answer;
        try {
            tempDir = Files.createTempDirectory("plagiarism-test-");
            original = tempDir.resolve("orig.txt");
            plagiarized = tempDir.resolve("copy.txt");
            answer = tempDir.resolve("ans.txt");
            Files.writeString(original, "今天天气很好");
            Files.writeString(plagiarized, "今天天气很好");
        } catch (IOException e) {
            throw new RuntimeException("创建临时测试文件失败", e);
        }

        runner.test("完整流程：完全相同输出 1.00", () -> {
            checker.check(original.toString(), plagiarized.toString(), answer.toString());
            String content = Files.readString(answer).trim();
            TestRunner.assertEquals("1.00", content, "完全相同应输出 1.00");
        });

        runner.test("答案格式为两位小数", () -> {
            checker.check(original.toString(), plagiarized.toString(), answer.toString());
            String content = Files.readString(answer).trim();
            TestRunner.assertTrue(content.matches("\\d\\.\\d{2}"), "答案应为两位小数，实际=" + content);
        });

        runner.test("读取不存在的原文抛出 FileReadException", () ->
                TestRunner.assertThrows(FileReadException.class,
                        () -> checker.check(tempDir.resolve("none.txt").toString(),
                                plagiarized.toString(), answer.toString()),
                        "原文不存在应抛出 FileReadException"));

        runner.test("答案路径为目录时抛出 FileWriteException", () ->
                TestRunner.assertThrows(FileWriteException.class,
                        () -> checker.check(original.toString(), plagiarized.toString(), tempDir.toString()),
                        "答案路径为目录应抛出 FileWriteException"));

        runner.test("参数个数错误抛出 InvalidArgumentException", () ->
                TestRunner.assertThrows(InvalidArgumentException.class,
                        () -> CliArguments.parse(new String[]{"a", "b"}),
                        "参数个数不为 3 应抛出 InvalidArgumentException"));

        runner.test("空路径参数抛出 InvalidArgumentException", () ->
                TestRunner.assertThrows(InvalidArgumentException.class,
                        () -> CliArguments.parse(new String[]{"a", "b", "  "}),
                        "空路径应抛出 InvalidArgumentException"));
    }
}
