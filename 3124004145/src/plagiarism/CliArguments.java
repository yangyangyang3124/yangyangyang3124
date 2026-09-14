package plagiarism;

/**
 * 命令行参数的封装与校验。
 * 三个参数依次为：论文原文文件绝对路径、抄袭版文件绝对路径、答案文件绝对路径。
 */
public final class CliArguments {

    private final String originalPath;
    private final String plagiarizedPath;
    private final String answerPath;

    private CliArguments(String originalPath, String plagiarizedPath, String answerPath) {
        this.originalPath = originalPath;
        this.plagiarizedPath = plagiarizedPath;
        this.answerPath = answerPath;
    }

    /**
     * 解析并校验命令行参数。
     *
     * @param args main 方法收到的原始参数
     * @return 校验通过后的参数封装
     * @throws InvalidArgumentException 参数个数不为 3 或存在空路径时
     */
    public static CliArguments parse(String[] args) {
        if (args == null || args.length != 3) {
            throw new InvalidArgumentException(
                    "需要且仅需要 3 个参数（原文路径、抄袭版路径、答案路径），实际收到 "
                            + (args == null ? 0 : args.length) + " 个");
        }
        if (isBlank(args[0]) || isBlank(args[1]) || isBlank(args[2])) {
            throw new InvalidArgumentException("文件路径不能为空");
        }
        return new CliArguments(args[0], args[1], args[2]);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public String originalPath() {
        return originalPath;
    }

    public String plagiarizedPath() {
        return plagiarizedPath;
    }

    public String answerPath() {
        return answerPath;
    }
}
