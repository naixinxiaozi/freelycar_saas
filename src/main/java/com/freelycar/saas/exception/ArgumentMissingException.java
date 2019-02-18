package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
public class ArgumentMissingException extends Exception {
    public ArgumentMissingException() {
    }

    public ArgumentMissingException(String message) {
        super(message);
    }

    public ArgumentMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentMissingException(Throwable cause) {
        super(cause);
    }

    public ArgumentMissingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
