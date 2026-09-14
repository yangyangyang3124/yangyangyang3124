package plagiarism;

/**
 * 读取原文或抄袭版文件失败（文件不存在、无权限等）时抛出。
 */
public class FileReadException extends PlagiarismException {

    private static final long serialVersionUID = 1L;

    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
