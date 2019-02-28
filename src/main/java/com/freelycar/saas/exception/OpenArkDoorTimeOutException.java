package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-02-28
 * @email toby911115@gmail.com
 */
public class OpenArkDoorTimeOutException extends Exception {
    public OpenArkDoorTimeOutException() {
    }

    public OpenArkDoorTimeOutException(String message) {
        super(message);
    }

    public OpenArkDoorTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenArkDoorTimeOutException(Throwable cause) {
        super(cause);
    }

    public OpenArkDoorTimeOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
