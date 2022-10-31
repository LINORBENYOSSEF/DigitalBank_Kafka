package app.bank.transaction;

import app.bank.BankException;
import app.bank.model.Account;
import app.bank.model.BankInfo;
import app.bank.model.TransactionRecord;
import app.bank.storage.Storage;

import java.util.Date;
import java.util.Optional;

public class TransactionBuilder {

    private final Storage storage;
    private final Transactions transactions;
    private final Account source;

    private Account destination;
    private double amount;
    private String comment;

    public TransactionBuilder(Storage storage, Transactions transactions, Account source) {
        this.storage = storage;
        this.transactions = transactions;
        this.source = source;
    }

    public void setDestination(String bankId, String branch, String accountNumber) throws BankException {
        BankInfo bankInfo = storage.getBankInfo();
        if (bankId.equals(bankInfo.getBankId()) || bankId.isEmpty()) {
            // local
            Optional<Account> optional = storage.findAccount((account)->
                    account.getAccountNumber().equals(accountNumber) &&
                            account.getBankId().equals(bankInfo.getBankId()) &&
                            account.getBranch().equals(branch));

            destination = optional.orElseThrow(()-> new BankException("Destination Unknown"));
        } else {
            // remote
            destination = new Account();
            destination.setBankId(bankId);
            destination.setBranch(branch);
            destination.setAccountNumber(accountNumber);
        }
    }

    public void setAmount(double amount) throws BankException {
        verifyAmount(amount);
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void send() throws BankException {
        transactions.sendMoney(build());
    }

    private void verifyAmount(double amount) throws BankException {
        if (source.getBalance() < amount) {
            throw new BankException("Insufficient balance");
        }

        if (amount <= 0) {
            throw new BankException("Bad amount");
        }
    }

    private TransactionRecord build() {
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setSource(source);
        transactionRecord.setDestination(destination);
        transactionRecord.setAmount(amount);
        transactionRecord.setComment(comment);
        transactionRecord.setTimestamp(new Date());

        return transactionRecord;
    }
}
