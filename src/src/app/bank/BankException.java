package app.bank;

public class BankException extends Exception {

    public BankException(Throwable cause) {
        super(cause);
    }

    public BankException(String message) {
        super(message);
    }
}
