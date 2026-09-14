package plagiarism;

import java.util.List;

/**
 * {@link TextNormalizer} 的单元测试，覆盖文本归一化与 n-gram 切分。
 */
public final class TextNormalizerTest {

    public void register(TestRunner runner) {
        runner.test("normalize 去除标点与空白", () ->
                TestRunner.assertEquals("今天天气很好", TextNormalizer.normalize("今天，天气 很好。\n"),
                        "标点与空白应被去除"));

        runner.test("normalize 英文转小写", () ->
                TestRunner.assertEquals("hello123", TextNormalizer.normalize("Hello 123"),
                        "英文应转小写、数字应保留"));

        runner.test("normalize null 返回空串", () ->
                TestRunner.assertEquals("", TextNormalizer.normalize(null), "null 应返回空串"));

        runner.test("ngrams 切分二元组", () -> {
            List<String> grams = TextNormalizer.ngrams("今天天气", 2);
            TestRunner.assertEquals(3, grams.size(), "长度为 4 的文本应有 3 个二元组");
            TestRunner.assertEquals("今天", grams.get(0), "第一个二元组应为'今天'");
            TestRunner.assertEquals("天天", grams.get(1), "第二个二元组应为'天天'");
            TestRunner.assertEquals("天气", grams.get(2), "最后一个二元组应为'天气'");
        });

        runner.test("ngrams 短文本退化为 unigram", () -> {
            List<String> grams = TextNormalizer.ngrams("好", 2);
            TestRunner.assertEquals(1, grams.size(), "短文本应退化为单个 unigram");
            TestRunner.assertEquals("好", grams.get(0), "unigram 应为原文本");
        });
    }
}
