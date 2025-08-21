package br.com.mili.milibackend.shared.exception.types;

public class ConflictException extends CustomException {
    private final int status;
    private final String code;

    public ConflictException(String message, String code) {
        super(message);
        this.status = 409;
        this.code = code;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }
}
