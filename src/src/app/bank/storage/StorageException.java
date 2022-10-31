package app.bank.storage;

import app.bank.BankException;

public class StorageException extends BankException {

    public StorageException(Throwable cause) {
        super(cause);
    }

    public StorageException(String message) {
        super(message);
    }
}
