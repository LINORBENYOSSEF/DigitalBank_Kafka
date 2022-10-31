package app.bank.model;

import app.bank.storage.Storage;
import app.bank.storage.StorageException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Loan implements ModelWithId {

    private String id;
    private ForeignKey<Account> account = new ForeignKey<>();
    private String reason;
    private double amount;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccount(Account account) {
        this.account.set(account);
    }

    public Account getAccount() {
        return account.get();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(id);
        account.writeInto(dataOutput);
        dataOutput.writeUTF(reason);
        dataOutput.writeDouble(amount);
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        id = dataInput.readUTF();
        account.readFrom(dataInput);
        reason = dataInput.readUTF();
        amount = dataInput.readDouble();
    }

    @Override
    public void postLoad(Storage storage) throws StorageException {
        account.postLoad(storage::findAccountById);
    }
}
