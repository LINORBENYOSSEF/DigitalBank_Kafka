package app.bank.auth;

import app.bank.BankException;

public class AuthException extends BankException {

    public AuthException(String message) {
        super(message);
    }
}
