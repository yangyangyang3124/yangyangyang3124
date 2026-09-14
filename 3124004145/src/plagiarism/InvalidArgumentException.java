package plagiarism;

/**
 * 命令行参数非法（参数个数不对、路径为空）时抛出。
 */
public class InvalidArgumentException extends PlagiarismException {

    private static final long serialVersionUID = 1L;

    public InvalidArgumentException(String message) {
        super(message);
    }
}
