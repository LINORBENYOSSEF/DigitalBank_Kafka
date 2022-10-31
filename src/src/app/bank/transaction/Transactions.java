package app.bank.transaction;

import app.bank.BankException;
import app.bank.model.Account;
import app.bank.model.AccountAction;
import app.bank.model.TransactionRecord;
import app.bank.storage.Storage;
import app.bank.storage.StorageException;
import app.bank.transaction.remote.RemoteAccount;
import app.bank.transaction.remote.TransactionSession;

import java.util.Date;
import java.util.Optional;

public class Transactions {

    private final Storage storage;
    private final TransactionSession transactionSession;

    public Transactions(Storage storage, TransactionSession transactionSession) {
        this.storage = storage;
        this.transactionSession = transactionSession;
    }

    public void sendMoney(TransactionRecord transactionRecord) throws BankException {
        updateStorageOnTransfer(transactionRecord);

        TransactionDetails transactionDetails = new TransactionDetails(transactionRecord);
        transactionSession.executeTransaction(transactionDetails);
    }

    public void receiveMoney(TransactionDetails transactionDetails) throws BankException {
        TransactionRecord localDetails = toTransactionRecord(transactionDetails);
        storage.addTransactionRecord(localDetails);

        localDetails.getDestination().depositAmount(localDetails.getAmount());

        storage.save();
    }

    private void updateStorageOnTransfer(TransactionRecord transactionRecord)
            throws StorageException {
        storage.addTransactionRecord(transactionRecord);

        transactionRecord.getSource().withdrawAmount(transactionRecord.getAmount());
        updateTransferActionForAccount(transactionRecord.getSource(),
                AccountAction.Type.SEND_MONEY, transactionRecord);

        if (!transactionRecord.getDestination().isRemote()) {
            transactionRecord.getDestination().depositAmount(transactionRecord.getAmount());
            updateTransferActionForAccount(transactionRecord.getDestination(),
                    AccountAction.Type.RECEIVE_MONEY, transactionRecord);
        }

        storage.save();
    }

    private void updateTransferActionForAccount(Account account, AccountAction.Type type,
                                                TransactionRecord transactionRecord) {
        AccountAction action = new AccountAction();
        action.setType(type);
        action.setAccount(account);
        action.setTimestamp(new Date());
        action.setLinkedData(transactionRecord);

        storage.addAccountAction(action);
    }

    private TransactionRecord toTransactionRecord(TransactionDetails transactionDetails) throws BankException {
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setSource(reflectRemoteAccount(transactionDetails.getSource()));
        transactionRecord.setDestination(getDestinationAccount(transactionDetails.getDestination()));
        transactionRecord.setAmount(transactionDetails.getAmount());
        transactionRecord.setComment(transactionDetails.getComment());
        transactionRecord.setTimestamp(transactionDetails.getTimestamp());

        return transactionRecord;
    }

    private Account reflectRemoteAccount(RemoteAccount remoteAccount) {
        Account account = new Account();
        account.setBankId(remoteAccount.getBankId());
        account.setBranch(remoteAccount.getBranch());
        account.setAccountNumber(remoteAccount.getAccountNumber());
        account.setBalance(Account.UNLIMITED_BALANCE);
        account.setRemote(true);
        return account;
    }

    private Account getDestinationAccount(RemoteAccount remoteAccount) throws BankException {
        Optional<Account> destination = storage.findAccount((account)->
                account.getAccountNumber().equals(remoteAccount.getAccountNumber()) &&
                        account.getBankId().equals(remoteAccount.getBankId()) &&
                        account.getBranch().equals(remoteAccount.getBranch()));

        return destination.orElseThrow(()-> new BankException("missing destination account"));
    }
}
