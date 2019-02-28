package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-02-28
 * @email toby911115@gmail.com
 */
public class DataIsExistException extends Exception {
    public DataIsExistException() {
    }

    public DataIsExistException(String message) {
        super(message);
    }

    public DataIsExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataIsExistException(Throwable cause) {
        super(cause);
    }

    public DataIsExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
