package br.com.mili.milibackend.shared.exception;

import br.com.mili.milibackend.shared.MyResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
@Order(2)
public class MyHandlerException {

    protected Logger logger;

    public MyHandlerException() {
        this.logger = LoggerFactory.getLogger(MyHandlerException.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logger.error("handleMethodArgumentNotValidException", exception);

        var errors = exception.getFieldErrors();
        var fields = new HashMap<String, String>();

        errors.forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));

 /*       List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());*/

        MyResponse myResponse = new MyResponse(HttpStatus.BAD_REQUEST.value());
        fields.forEach((field, message) -> myResponse.addMessage(field + ": " + message));
        return myResponse;
    }

    @ExceptionHandler(org.springframework.dao.InvalidDataAccessResourceUsageException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyResponse handleInvalidDataAccessException(org.springframework.dao.InvalidDataAccessResourceUsageException exception) {
        logger.error("handleInvalidDataAccessException", exception);

        return new MyResponse(MyResponse.ERROR_500, "Ocorreu um erro interno, aguarde alguns minutos e tente novamente. Se o erro persistir enviar e-mail para felipe.alves@mili.com.br");
    }

    @ExceptionHandler(JpaSystemException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyResponse handleJpaSystemException(JpaSystemException exception) {
        logger.error("handleJpaSystemException", exception);

        String mensagem = handleDtEntregaOra20570(exception);

        if (mensagem == null)
            mensagem = handleDtEntregaOra20571(exception);

        if (mensagem == null)
            mensagem = "Ocorreu um erro interno, aguarde alguns minutos e tente novamente. Se o erro persistir enviar e-mail para felipe.alves@mili.com.br";

        return new MyResponse(MyResponse.ERROR_500, mensagem );
    }

    @ExceptionHandler(MyIllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    public MyResponse handleIllegalArgumentException(MyIllegalArgumentException exception) {
        logger.error("handleIllegalArgumentException", exception);
        return new MyResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyResponse handleIllegalArgumentException(IllegalArgumentException exception) {
        logger.error("handleIllegalArgumentException", exception);
        return new MyResponse(MyResponse.ERROR_500, exception.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyResponse handleDateTimeParseException(DateTimeParseException exception) {
        logger.error("handleDateTimeParseException", exception);
        return new MyResponse(MyResponse.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyResponse handleConstraintViolationException(ConstraintViolationException exception) {
        logger.error("handleConstraintViolationException", exception);
        return new MyResponse(MyResponse.BAD_REQUEST, exception.getMessage());
    }



    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ResponseBody
    public MyResponse handleAccessDeniedException(AccessDeniedException exception) {
        logger.error("AccessDeniedException", exception);
        return new MyResponse(HttpStatus.FORBIDDEN.value(), "Acesso negado");
    }
    
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyResponse handleRuntimeException(RuntimeException exception) {
        logger.error("handleRuntimeException", exception);
        return new MyResponse(MyResponse.ERROR_500, "Ocorreu um erro interno, aguarde alguns minutos e tente novamente. Se o erro persistir enviar e-mail para felipe.alves@mili.com.br");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyResponse handleException(Exception exception) {
        logger.error("handleException", exception);
        return new MyResponse(MyResponse.ERROR_500, "Ocorreu um erro interno, aguarde alguns minutos e tente novamente. Se o erro persistir enviar e-mail para felipe.alves@mili.com.br");
    }
    private String handleDtEntregaOra20570(JpaSystemException exception) {
        String endIndex = "#";
        String ORA_205670 = "ORA-20570:";
        return handleORA(exception, ORA_205670, endIndex);
    }

    private String handleDtEntregaOra20571(JpaSystemException exception) {
        String endIndex = "#";
        String ORA_205671 = "ORA-20571:";
        return handleORA(exception, ORA_205671, endIndex);
    }

    private String handleORA(JpaSystemException exception,String beginIndex, String endIndex) {
        Throwable cause = getCause(exception, beginIndex);
        if (cause == null)
            return null;

        String msg = cause.getMessage();
        if (msg == null)
            return null;

        return msg.substring(msg.indexOf(beginIndex) + beginIndex.length(), msg.indexOf(endIndex));
    }


    private Throwable getCause(Throwable cause, String key)
    {
        if(cause == null )
            return null;

        String msg =  cause.getMessage();

        if(msg.contains(key))
            return cause;

       return getCause(cause.getCause(), key);
    }
}
