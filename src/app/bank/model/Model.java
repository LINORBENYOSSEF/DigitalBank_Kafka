package app.bank.model;

import app.bank.storage.Storage;
import app.bank.storage.StorageException;

public interface Model extends Field {

    void postLoad(Storage storage) throws StorageException;
}
