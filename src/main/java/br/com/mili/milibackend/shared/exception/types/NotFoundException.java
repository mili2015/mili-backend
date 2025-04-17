package br.com.mili.milibackend.shared.exception.types;

public class NotFoundException extends CustomException {
    private final int status;
    private final String code;

    public NotFoundException(String message, String code) {
        super(message);
        this.status = 404;
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
