package app.bank.model;

import app.bank.storage.Storage;
import app.bank.storage.StorageException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Customer implements ModelWithId {

    private String id;
    private String username;
    private String password;

    private ListField<ForeignKey<Account>> accounts = new ListField<>(ForeignKey::new);

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Account> getAccounts() {
        return accounts.getList().stream()
                .map(ForeignKey::get)
                .collect(Collectors.toList());
    }

    public void addAccount(Account account) {
        ForeignKey<Account> accountForeignKey = new ForeignKey<>();
        accountForeignKey.set(account);
        accounts.getList().add(accountForeignKey);
    }

    public void removeAccount(Account account) {
        ForeignKey<Account> toRemove = null;
        for (ForeignKey<Account> accountForeignKey : accounts.getList()) {
            if (accountForeignKey.get().getId().equals(account.getId())) {
                toRemove = accountForeignKey;
                break;
            }
        }

        if (toRemove != null) {
            accounts.getList().remove(toRemove);
        }
    }

    public boolean hasAccount(Account account) {
        for (ForeignKey<Account> accountForeignKey : accounts.getList()) {
            if (accountForeignKey.get().getId().equals(account.getId())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(id);
        dataOutput.writeUTF(username);
        dataOutput.writeUTF(password);
        accounts.writeInto(dataOutput);
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        id = dataInput.readUTF();
        username = dataInput.readUTF();
        password = dataInput.readUTF();
        accounts.readFrom(dataInput);
    }

    @Override
    public void postLoad(Storage storage) throws StorageException {
        for (ForeignKey<Account> accountForeignKey : accounts.getList()) {
            accountForeignKey.postLoad(storage::findAccountById);
        }
    }
}
