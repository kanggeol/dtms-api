package com.dailystudy.dtmsapi.exception;

public class DuplicatedmemberNameException extends RuntimeException{
    public DuplicatedmemberNameException() {
        super();
    }

    public DuplicatedmemberNameException(String message) {
        super(message);
    }

    public DuplicatedmemberNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedmemberNameException(Throwable cause) {
        super(cause);
    }
}