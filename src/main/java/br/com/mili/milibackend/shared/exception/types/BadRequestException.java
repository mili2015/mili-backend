package br.com.mili.milibackend.shared.exception.types;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends CustomException {
    private final int status;
    private final String code;

    public BadRequestException(String message, String code) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST.value();
        this.code = code;
    }
}