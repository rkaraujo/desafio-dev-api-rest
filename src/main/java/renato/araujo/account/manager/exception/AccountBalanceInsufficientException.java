package renato.araujo.account.manager.exception;

import org.springframework.http.HttpStatus;

public class AccountBalanceInsufficientException extends AbstractBusinessException {

    public AccountBalanceInsufficientException() {
        super("Saldo insuficiente", HttpStatus.BAD_REQUEST);
    }
}
