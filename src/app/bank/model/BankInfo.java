package app.bank.model;

import app.bank.storage.Storage;
import app.bank.storage.StorageException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public class BankInfo implements Model {

    private String bankId;
    private ForeignKey<Account> linkedAccount = new ForeignKey<>();

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Account getLinkedAccount() {
        return linkedAccount.get();
    }

    public void setLinkedAccount(Account linkedAccount) {
        this.linkedAccount.set(linkedAccount);
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(bankId);
        linkedAccount.writeInto(dataOutput);
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        bankId = dataInput.readUTF();
        linkedAccount.readFrom(dataInput);
    }

    @Override
    public void postLoad(Storage storage) throws StorageException {
        linkedAccount.postLoad(storage::findAccountById);
    }

    public String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }
}
