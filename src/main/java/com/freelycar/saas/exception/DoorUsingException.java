package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-03-20
 * @email toby911115@gmail.com
 */
public class DoorUsingException extends Exception {
    public DoorUsingException() {
    }

    public DoorUsingException(String message) {
        super(message);
    }

    public DoorUsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoorUsingException(Throwable cause) {
        super(cause);
    }

    public DoorUsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
