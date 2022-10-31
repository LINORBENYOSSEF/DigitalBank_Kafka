package app.bank.model;

import app.bank.storage.Storage;
import app.bank.storage.StorageException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;

public class AccountAction implements Model {

    public enum Type {
        SEND_MONEY {
            @Override
            void postLoad(Storage storage, AccountAction accountAction) throws StorageException {
                accountAction.linkedData.postLoad(storage::findTransactionRecordById);
            }
        },
        RECEIVE_MONEY {
            @Override
            void postLoad(Storage storage, AccountAction accountAction) throws StorageException {
                accountAction.linkedData.postLoad(storage::findTransactionRecordById);
            }
        },
        REQUEST_LOAN {
            @Override
            void postLoad(Storage storage, AccountAction accountAction) throws StorageException {
                accountAction.linkedData.postLoad(storage::findLoanRequestById);
            }
        },
        RECEIVE_LOAN {
            @Override
            void postLoad(Storage storage, AccountAction accountAction) throws StorageException {
                accountAction.linkedData.postLoad(storage::findLoanById);
            }
        }
        ;

        abstract void postLoad(Storage storage, AccountAction accountAction) throws StorageException;
    }

    private EnumField<Type> type = new EnumField<>(Type.class);
    private Date timestamp;
    private ForeignKey<ModelWithId> linkedData = new ForeignKey<>();
    private ForeignKey<Account> account = new ForeignKey<>();

    public Type getType() {
        return type.get();
    }

    public void setType(Type type) {
        this.type.set(type);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ModelWithId getLinkedData() {
        return linkedData.get();
    }

    public void setLinkedData(ModelWithId model) {
        this.linkedData.set(model);
    }

    public Account getAccount() {
        return account.get();
    }

    public void setAccount(Account account) {
        this.account.set(account);
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        type.writeInto(dataOutput);
        dataOutput.writeLong(timestamp.getTime());
        linkedData.writeInto(dataOutput);
        account.writeInto(dataOutput);
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        type.readFrom(dataInput);
        timestamp = new Date(dataInput.readLong());
        linkedData.readFrom(dataInput);
        account.readFrom(dataInput);
    }

    @Override
    public void postLoad(Storage storage) throws StorageException {
        type.get().postLoad(storage, this);
        account.postLoad(storage::findAccountById);
    }
}
