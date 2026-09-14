package plagiarism;

/**
 * {@link CosineSimilarityCalculator} 的单元测试。
 * 覆盖：完全一致、完全无关、作业示例、空文本、标点/空白、英文大小写、单字符、部分重复、null、对称性。
 */
public final class SimilarityCalculatorTest {

    private final SimilarityCalculator calculator = new CosineSimilarityCalculator();

    public void register(TestRunner runner) {
        runner.test("相同文本返回 1.0", () ->
                TestRunner.assertEquals(1.0, calculator.compute("今天天气很好", "今天天气很好"),
                        1e-9, "完全相同的文本应返回 1.0"));

        runner.test("完全无关文本返回 0.0", () ->
                TestRunner.assertEquals(0.0, calculator.compute("人工智能", "北京天气"),
                        1e-9, "无共同 bigram 的文本应返回 0.0"));

        runner.test("作业示例相似度落在 (0,1) 区间", () -> {
            double sim = calculator.compute(
                    "今天是星期天，天气晴，今天晚上我要去看电影。",
                    "今天是周天，天气晴朗，我晚上要去看电影。");
            TestRunner.assertTrue(sim > 0.0 && sim < 1.0,
                    "作业示例相似度应严格介于 0 与 1 之间，实际=" + sim);
        });

        runner.test("空文本对空文本返回 1.0", () ->
                TestRunner.assertEquals(1.0, calculator.compute("", ""), 1e-9, "两个空文本视为相同"));

        runner.test("空文本对非空文本返回 0.0", () ->
                TestRunner.assertEquals(0.0, calculator.compute("", "你好"), 1e-9, "空文本与非空文本相似度为 0"));

        runner.test("标点与空白不影响结果", () ->
                TestRunner.assertEquals(
                        calculator.compute("今天天气很好", "今天天气很好"),
                        calculator.compute("今天，天气 很好。\n", "今天天气很好"),
                        1e-9, "标点与空白应被归一化掉"));

        runner.test("英文大小写不敏感", () ->
                TestRunner.assertEquals(1.0, calculator.compute("Hello World", "hello world"),
                        1e-9, "英文应大小写不敏感"));

        runner.test("单字符文本可正确处理", () ->
                TestRunner.assertEquals(1.0, calculator.compute("好", "好"), 1e-9, "单字符相同应返回 1.0"));

        runner.test("部分重复返回中间值", () -> {
            double sim = calculator.compute("我爱学习编程", "我爱学习");
            TestRunner.assertTrue(sim > 0.0 && sim < 1.0, "部分重复应返回中间值，实际=" + sim);
        });

        runner.test("null 输入按空文本处理", () -> {
            TestRunner.assertEquals(1.0, calculator.compute(null, null), 1e-9, "null 与 null 视为相同");
            TestRunner.assertEquals(0.0, calculator.compute(null, "内容"), 1e-9, "null 与非空文本相似度为 0");
        });

        runner.test("相似度具有对称性", () ->
                TestRunner.assertEquals(
                        calculator.compute("今天天气很好", "今天天气不错"),
                        calculator.compute("今天天气不错", "今天天气很好"),
                        1e-9, "相似度应对称"));
    }
}
