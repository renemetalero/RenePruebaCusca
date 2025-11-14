package backend.apiscart.advicer;

import backend.apiscart.component.exception.GenericException;
import backend.apiscart.component.util.log.LogUtil;
import backend.apiscart.component.util.log.LogUtil.TYPELOG;
import backend.apiscart.component.util.log.LogUtilImpl;
import backend.apiscart.dto.exception.ErrorDto;
import backend.apiscart.dto.exception.ResponseErrorDto;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private final LogUtil log;

    public GenericExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.log = new LogUtilImpl(GenericExceptionHandler.class);
    }

    // ---------------------------------------------------------------------
    // 1) HANDLER PARA GenericException
    // ---------------------------------------------------------------------
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<Object> handleCustomException(GenericException ex, WebRequest request) {
        return buildResponse(ex, request, ex.getBaseError());
    }


    // ---------------------------------------------------------------------
    // 2) HANDLER PARA EXCEPCIONES GENERALES (Exception.class)
    // ---------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {

        ErrorDto error = ErrorDto.builder()
                .field("Exception")
                .errors(List.of(ex.getMessage()))
                .httpHeaders(new HttpHeaders())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        log.write(TYPELOG.ERROR, "Internal Server Error", ex.getMessage());

        ResponseErrorDto response = ResponseErrorDto.builder()
                .field("Exception")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errors(List.of(ex.getMessage()))
                .message("Internal Server Error")
                .object(ex.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // ---------------------------------------------------------------------
    // 3) HANDLER PARA VALIDACIONES (@Valid)
    // ---------------------------------------------------------------------
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String message = getMessage(error.getDefaultMessage(), request.getLocale());
            errors.add(error.getField() + ": " + message);
        });

        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            String message = getMessage(error.getDefaultMessage(), request.getLocale());
            errors.add(error.getObjectName() + ": " + message);
        });

        GenericException apiError = new GenericException(
                HttpStatus.BAD_REQUEST,
                headers,
                getMessage("error.genericexception.inputvalidation", request.getLocale()),
                "error.genericexception.badrequest",
                errors
        );

        return buildResponse(apiError, request, apiError.getBaseError());
    }


    // ---------------------------------------------------------------------
    // MÉTODO UTIL PARA CONSTRUIR RESPUESTAS CONSISTENTES
    // ---------------------------------------------------------------------
    private ResponseEntity<Object> buildResponse(GenericException ex, WebRequest request, ErrorDto baseError) {

        ResponseErrorDto response = ResponseErrorDto.builder()
                .field(baseError.getField())
                .status(baseError.getHttpStatus())
                .errors(baseError.getErrors())
                .message(getMessage(ex.getMessage(), request.getLocale()))
                .object(ex.getClass().getSimpleName())
                .build();

        log.write(TYPELOG.ERROR, "Response Error", response);

        return new ResponseEntity<>(response, baseError.getHttpHeaders(), baseError.getHttpStatus());
    }


    // ---------------------------------------------------------------------
    // TRADUCCIÓN DE MENSAJES
    // ---------------------------------------------------------------------
    private String getMessage(String code, Locale locale) {
        try {
            return messageSource.getMessage(code, null, locale);
        } catch (Exception e) {
            return code;
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(
                        401,
                        "Credenciales inválidas. Usuario o contraseña incorrectos."
                )
        );
    }

    record ErrorResponse(int status, String message) {}
}

