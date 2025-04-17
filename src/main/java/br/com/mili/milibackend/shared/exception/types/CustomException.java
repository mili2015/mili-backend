package br.com.mili.milibackend.shared.exception.types;


import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    private final int status;
    private final String code;

    protected CustomException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = "INTERNAL_SERVER_ERROR";
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
