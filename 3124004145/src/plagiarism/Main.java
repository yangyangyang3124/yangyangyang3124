package plagiarism;

/**
 * 程序入口：论文查重工具。
 *
 * <p>用法：</p>
 * <pre>java -jar main.jar &lt;原文文件绝对路径&gt; &lt;抄袭版文件绝对路径&gt; &lt;答案文件绝对路径&gt;</pre>
 *
 * <p>程序从命令行指定的原文与抄袭版文件读取内容，计算重复率，并将结果
 * （浮点型，精确到小数点后两位）写入指定的答案文件。</p>
 */
public final class Main {

    public static void main(String[] args) {
        try {
            CliArguments arguments = CliArguments.parse(args);
            PlagiarismChecker checker = new PlagiarismChecker(new CosineSimilarityCalculator());
            checker.check(arguments.originalPath(), arguments.plagiarizedPath(), arguments.answerPath());
        } catch (PlagiarismException e) {
            System.err.println("错误：" + e.getMessage());
            printUsage();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("发生未知错误：" + e.getMessage());
            System.exit(2);
        }
    }

    private static void printUsage() {
        System.err.println("用法：java -jar main.jar <原文文件绝对路径> <抄袭版文件绝对路径> <答案文件绝对路径>");
    }
}
