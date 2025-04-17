package br.com.mili.milibackend.shared.exception;

public class MyIllegalArgumentException extends IllegalArgumentException {

    public MyIllegalArgumentException() {
    }

    public MyIllegalArgumentException(String s) {
        super(s);
    }

    public MyIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
