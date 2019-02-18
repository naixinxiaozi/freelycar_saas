package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
public class NoEmptyArkException extends Exception {
    public NoEmptyArkException() {
    }

    public NoEmptyArkException(String message) {
        super(message);
    }

    public NoEmptyArkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEmptyArkException(Throwable cause) {
        super(cause);
    }

    public NoEmptyArkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
