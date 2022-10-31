package app.bank.model;

import app.bank.storage.Storage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Account implements ModelWithId {

    public static final double UNLIMITED_BALANCE = Double.MIN_VALUE;

    private String id;
    private String accountNumber;
    private String bankId;
    private String branch;
    private double balance;
    private boolean isRemote;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void updateBalance(double delta) {
        if (balance != UNLIMITED_BALANCE) {
            balance += delta;
        }
    }

    public void withdrawAmount(double amount) {
        updateBalance(-amount);
    }

    public void depositAmount(double amount) {
        updateBalance(amount);
    }

    public void setRemote(boolean remote) {
        isRemote = remote;
    }

    public boolean isRemote() {
        return isRemote;
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(id);
        dataOutput.writeUTF(accountNumber);
        dataOutput.writeUTF(bankId);
        dataOutput.writeUTF(branch);
        dataOutput.writeDouble(balance);
        dataOutput.writeBoolean(isRemote);
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        id = dataInput.readUTF();
        accountNumber = dataInput.readUTF();
        bankId = dataInput.readUTF();
        branch = dataInput.readUTF();
        balance = dataInput.readDouble();
        isRemote = dataInput.readBoolean();
    }

    @Override
    public void postLoad(Storage storage) {

    }

    @Override
    public String toString() {
        if (isRemote()) {
            return String.format("[REMOTE] %s:%s:%s", bankId, branch, accountNumber);
        } else {
            return String.format("%s:%s", branch, accountNumber);
        }
    }
}
