package com.yh.exception;

public class YHException extends Exception{

    public YHException() {
    }

    public YHException(String message) {
        super(message);
    }

    public YHException(String message, Throwable cause) {
        super(message, cause);
    }

    public YHException(Throwable cause) {
        super(cause);
    }
}
