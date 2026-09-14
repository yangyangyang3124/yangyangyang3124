package plagiarism;

import java.util.ArrayList;
import java.util.List;

/**
 * 轻量级单元测试运行器。
 *
 * <p>由于评测环境不引入外部依赖（不使用 JUnit），这里实现一个极简的测试框架：
 * 每条测试由“名称 + 可执行代码块”描述，运行后统计通过/失败数量并以退出码反馈，
 * 便于接入 CI 与覆盖率统计。</p>
 */
public final class TestRunner {

    /** 单条测试的执行体，允许抛出任意异常。 */
    @FunctionalInterface
    public interface TestBody {
        void run() throws Exception;
    }

    private record NamedTest(String name, TestBody body) {
    }

    private final List<NamedTest> tests = new ArrayList<>();

    /** 注册一条测试用例。 */
    public void test(String name, TestBody body) {
        tests.add(new NamedTest(name, body));
    }

    /** 运行全部已注册的测试，返回失败用例数量。 */
    public int runAll() {
        int passed = 0;
        int failed = 0;
        for (NamedTest test : tests) {
            try {
                test.body().run();
                passed++;
                System.out.println("[PASS] " + test.name());
            } catch (Throwable t) {
                failed++;
                System.out.println("[FAIL] " + test.name() + " -> " + t.getMessage());
            }
        }
        System.out.println("---------------------------------");
        System.out.println("总计: " + tests.size() + "  通过: " + passed + "  失败: " + failed);
        return failed;
    }

    // ---- 断言工具 ----

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(double expected, double actual, double epsilon, String message) {
        if (Double.compare(expected, actual) != 0 && Math.abs(expected - actual) > epsilon) {
            throw new AssertionError(message + "（期望=" + expected + "，实际=" + actual + "）");
        }
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + "（期望=" + expected + "，实际=" + actual + "）");
        }
    }

    public static void assertThrows(Class<? extends Throwable> type, TestBody body, String message) {
        try {
            body.run();
        } catch (Throwable thrown) {
            if (type.isInstance(thrown)) {
                return;
            }
            throw new AssertionError(message + "（期望抛出 " + type.getSimpleName()
                    + "，实际抛出 " + thrown + "）");
        }
        throw new AssertionError(message + "（期望抛出 " + type.getSimpleName() + "，但未抛出任何异常）");
    }

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        new TextNormalizerTest().register(runner);
        new SimilarityCalculatorTest().register(runner);
        new PlagiarismCheckerTest().register(runner);
        int failed = runner.runAll();
        System.exit(failed == 0 ? 0 : 1);
    }
}
