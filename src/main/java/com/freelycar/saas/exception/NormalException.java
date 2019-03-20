package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-03-20
 * @email toby911115@gmail.com
 */
public class NormalException extends Exception {
    public NormalException() {
    }

    public NormalException(String message) {
        super(message);
    }

    public NormalException(String message, Throwable cause) {
        super(message, cause);
    }

    public NormalException(Throwable cause) {
        super(cause);
    }

    public NormalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
