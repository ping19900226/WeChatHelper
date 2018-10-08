package com.yh.exception;

public class YHRTException extends RuntimeException {
    public YHRTException() {
    }

    public YHRTException(String message) {
        super(message);
    }

    public YHRTException(String message, Throwable cause) {
        super(message, cause);
    }

    public YHRTException(Throwable cause) {
        super(cause);
    }

    public YHRTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
