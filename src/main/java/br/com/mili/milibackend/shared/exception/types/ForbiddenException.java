package br.com.mili.milibackend.shared.exception.types;

public class ForbiddenException extends CustomException {
    private final int status;
    private final String code;

    public ForbiddenException(String message, String code) {
        super(message);
        this.status = 403;
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
