package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
public class UpdateDataErrorException extends Exception {
    public UpdateDataErrorException() {
    }

    public UpdateDataErrorException(String message) {
        super(message);
    }

    public UpdateDataErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateDataErrorException(Throwable cause) {
        super(cause);
    }

    public UpdateDataErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
