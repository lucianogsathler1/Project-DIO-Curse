package exerc_DIO.demo.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura cliente não encontrado -> Devolve 404 not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Captura CEP inválido ou erro na chamada do Feign -> Devolve 400 Bad Request
    @ExceptionHandler({IllegalArgumentException.class, FeignException.class})
    public ResponseEntity<Object> handleBadRequest(Exception ex) {
        String mensagem = (ex instanceof FeignException)
                ? "Formato de CEP inválido. Informe 8 dígitos numéricos."
                : ex.getMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, mensagem);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String message)  {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
