package br.com.mili.milibackend.shared.exception;

public class MyBadRequestException extends IllegalArgumentException {

    public MyBadRequestException() {
    }

    public MyBadRequestException(String s) {
        super(s);
    }

    public MyBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyBadRequestException(Throwable cause) {
        super(cause);
    }
}
