package com.freelycar.saas.exception;

/**
 * @author tangwei - Toby
 * @date 2019-03-06
 * @email toby911115@gmail.com
 */
public class CarNumberValidationException extends Exception {
    public CarNumberValidationException() {
    }

    public CarNumberValidationException(String message) {
        super(message);
    }

    public CarNumberValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarNumberValidationException(Throwable cause) {
        super(cause);
    }

    public CarNumberValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
