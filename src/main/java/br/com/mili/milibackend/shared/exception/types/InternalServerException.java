package br.com.mili.milibackend.shared.exception.types;

public class InternalServerException extends CustomException {
    private final int status;
    private final String code;

    public InternalServerException(String message, String code) {
        super(message);
        this.status = 500;
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
