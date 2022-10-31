package app.bank.model;

import app.bank.storage.Storage;
import app.bank.storage.StorageException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LoanRequest implements ModelWithId {

    private String id;
    private ForeignKey<Account> requestingAccount = new ForeignKey<>();
    private double amount;
    private String reason;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return requestingAccount.get();
    }

    public void setAccount(Account account) {
        requestingAccount.set(account);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(id);
        requestingAccount.writeInto(dataOutput);
        dataOutput.writeDouble(amount);
        dataOutput.writeUTF(reason);
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        id = dataInput.readUTF();
        requestingAccount.readFrom(dataInput);
        amount = dataInput.readDouble();
        reason = dataInput.readUTF();
    }

    @Override
    public void postLoad(Storage storage) throws StorageException {
        requestingAccount.postLoad(storage::findAccountById);
    }
}
