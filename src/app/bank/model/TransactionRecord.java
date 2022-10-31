package app.bank.model;

import app.bank.storage.Storage;
import app.bank.storage.StorageException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

public class TransactionRecord implements ModelWithId {

    private String id;
    private AccountField source = new AccountField();
    private AccountField destination = new AccountField();
    private double amount;
    private String comment;
    private Date timestamp;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getSource() {
        return source.get();
    }

    public void setSource(Account source) {
        this.source.set(source);
    }

    public Account getDestination() {
        return destination.get();
    }

    public void setDestination(Account destination) {
        this.destination.set(destination);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRemote() {
        return source.get().isRemote() || destination.get().isRemote();
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(id);
        source.writeInto(dataOutput);
        destination.writeInto(dataOutput);
        dataOutput.writeDouble(amount);
        dataOutput.writeUTF(comment);
        dataOutput.writeLong(timestamp.getTime());
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        id = dataInput.readUTF();
        source.readFrom(dataInput);
        destination.readFrom(dataInput);
        amount = dataInput.readDouble();
        comment = dataInput.readUTF();
        timestamp = new Date(dataInput.readLong());
    }

    @Override
    public void postLoad(Storage storage) throws StorageException {
        source.postLoad(storage::findAccountById);
        destination.postLoad(storage::findAccountById);
    }

    private static class AccountField extends ForeignKey<Account> {

        private boolean loadRemote = false;

        @Override
        public void writeInto(DataOutput dataOutput) throws IOException {
            Account account = get();
            if (account != null && account.isRemote()) {
                dataOutput.writeBoolean(true);
                account.writeInto(dataOutput);
            } else {
                dataOutput.writeBoolean(false);
                super.writeInto(dataOutput);
            }
        }

        @Override
        public void readFrom(DataInput dataInput) throws IOException {
            loadRemote = dataInput.readBoolean();
            if (loadRemote) {
                Account account = new Account();
                account.readFrom(dataInput);
                set(account);
            } else {
                super.readFrom(dataInput);
            }
        }

        @Override
        public void postLoad(Function<String, Optional<? extends Account>> valueFinder) throws StorageException {
            if (!loadRemote) {
                super.postLoad(valueFinder);
            }
        }
    }
}
