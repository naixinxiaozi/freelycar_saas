package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-02-28
 * @email toby911115@gmail.com
 */
public class OpenArkDoorFailedException extends Exception {
    public OpenArkDoorFailedException() {
    }

    public OpenArkDoorFailedException(String message) {
        super(message);
    }

    public OpenArkDoorFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenArkDoorFailedException(Throwable cause) {
        super(cause);
    }

    public OpenArkDoorFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
