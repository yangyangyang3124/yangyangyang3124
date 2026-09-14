package plagiarism;

/**
 * 写入答案文件失败（路径非法、目录不可写等）时抛出。
 */
public class FileWriteException extends PlagiarismException {

    private static final long serialVersionUID = 1L;

    public FileWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
