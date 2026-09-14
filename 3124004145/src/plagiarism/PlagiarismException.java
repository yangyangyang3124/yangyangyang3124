package plagiarism;

/**
 * 论文查重程序统一的运行时异常基类。
 * 所有业务异常均继承自本类，便于上层统一捕获与兜底处理。
 */
public class PlagiarismException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PlagiarismException(String message) {
        super(message);
    }

    public PlagiarismException(String message, Throwable cause) {
        super(message, cause);
    }
}
