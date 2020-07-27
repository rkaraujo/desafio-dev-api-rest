package renato.araujo.account.manager.exception;

import org.springframework.http.HttpStatus;

public class AbstractBusinessException extends RuntimeException {

    private final HttpStatus code;

    public AbstractBusinessException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }
}
