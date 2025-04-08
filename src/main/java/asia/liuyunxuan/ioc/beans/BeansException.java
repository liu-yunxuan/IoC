package asia.liuyunxuan.ioc.beans;

/**
 * IoC容器异常类，用于封装Bean操作过程中的异常
 * <p>
 * 继承自RuntimeException，作为IoC容器中所有Bean相关操作异常的基类
 */
public class BeansException extends RuntimeException {
    /**
     * 构造一个新的BeansException实例
     *
     * @param message 异常信息
     */
    public BeansException(String message) {
        super(message);
    }

    /**
     * 构造一个新的BeansException实例
     *
     * @param message 异常信息
     * @param cause   导致此异常的原因
     */
    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }
}
