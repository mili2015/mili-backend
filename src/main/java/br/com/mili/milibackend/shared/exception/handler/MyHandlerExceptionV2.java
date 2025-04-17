package br.com.mili.milibackend.shared.exception.handler;

import br.com.mili.milibackend.shared.MyResponse;
import br.com.mili.milibackend.shared.exception.types.CustomException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(1)
public class MyHandlerExceptionV2 {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<MyResponse> handleCustomException(CustomException ex) {
        MyResponse errorResponse = new MyResponse(ex.getStatus(), ex.getMessage(), ex.getCode());

        return new ResponseEntity<MyResponse>(errorResponse, HttpStatus.valueOf(ex.getStatus()));
    }

    // DESATIVADO POR AGORA PARA EVITAR PROBLEMAS NO MY HANDLER EXCEPTION V1
/*    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorMessage> handleCustomException(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        var fields = new HashMap<String, String>();

        errors.forEach(e -> {
            fields.put(e.getField(), e.getDefaultMessage());
        });

        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, "Erro de validação", "BAD_REQUEST", ex, fields);

        return new ResponseEntity<RestErrorMessage>(errorResponse, HttpStatus.BAD_REQUEST);
    }*/

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> handleGeneralException(Exception ex) {
        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno.", "INTERNAL_SERVER_ERROR", ex);
        return new ResponseEntity<RestErrorMessage>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
