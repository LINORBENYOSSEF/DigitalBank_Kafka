package app.bank;

import app.bank.auth.AuthManager;
import app.bank.auth.LoginDetails;
import app.bank.model.Account;
import app.bank.model.AccountAction;
import app.bank.model.BankInfo;
import app.bank.model.Customer;
import app.bank.model.Employee;
import app.bank.model.Loan;
import app.bank.model.LoanRequest;
import app.bank.model.TransactionRecord;
import app.bank.storage.Storage;
import app.bank.transaction.TransactionBuilder;
import app.bank.transaction.Transactions;
import app.bank.transaction.TransactionDetails;
import app.bank.transaction.remote.TransactionSession;

import java.util.Date;
import java.util.List;

public class BankControlImpl implements BankControl {

    private final Storage storage;
    private final AuthManager authManager;
    private final TransactionSession transactionSession;
    private Transactions transactions;

    public BankControlImpl(Storage storage, AuthManager authManager) {
        this.storage = storage;
        this.authManager = authManager;
        this.transactionSession = new TransactionSession(
                storage.getBankInfo(),
                this::broadcastTransaction
        );
        transactions = new Transactions(storage, transactionSession);
    }

    @Override
    public void openCustomerAccount(CustomerDetails customerDetails) throws BankException {
        Customer customer = new Customer();
        customer.setUsername(customerDetails.getLoginDetails().getUsername());
        customer.setPassword(customerDetails.getLoginDetails().getPassword());
        storage.addCustomer(customer);

        storage.save();
    }

    @Override
    public void openAccount(AccountDetails accountDetails) throws BankException {
        BankInfo currentBank = storage.getBankInfo();

        Account account = new Account();
        account.setBankId(currentBank.getBankId());
        account.setBranch(accountDetails.getBranch());
        account.setAccountNumber(currentBank.generateAccountNumber());
        account.setBalance(0);
        account.setRemote(false);
        storage.addAccount(account);

        Customer customer = authManager.getLoggedCustomer();
        customer.addAccount(account);

        storage.save();

        performInitialAccountTransfer(account);
    }

    @Override
    public void editCustomerAccount(Customer customer) throws BankException {
        storage.save();
    }

    @Override
    public void closeCustomerAccount() throws BankException {
        Customer customer = authManager.getLoggedCustomer();
        Account account = authManager.getAccountUsed();

        storage.deleteAccount(account);
        customer.removeAccount(account);
        storage.save();

        authManager.logout();
    }

    @Override
    public Customer loginCustomer(LoginDetails credentials) throws BankException {
        return authManager.loginCustomer(credentials);
    }

    @Override
    public void useAccount(Account account) {
        authManager.useAccount(account);
    }

    @Override
    public Employee loginEmployee(LoginDetails credentials) throws BankException {
        return authManager.loginEmployee(credentials);
    }

    @Override
    public void logout() throws BankException {
        authManager.logout();
    }

    @Override
    public TransactionBuilder sendMoney() throws BankException {
        Account source = authManager.getAccountUsed();
        return new TransactionBuilder(storage, transactions, source);
    }

    @Override
    public void requestLoan(LoanRequest loanRequest) throws BankException {
        storage.addLoanRequest(loanRequest);

        AccountAction action = new AccountAction();
        action.setTimestamp(new Date());
        action.setAccount(loanRequest.getAccount());
        action.setLinkedData(loanRequest);
        action.setType(AccountAction.Type.REQUEST_LOAN);
        storage.addAccountAction(action);

        storage.save();
    }

    @Override
    public void acceptLoan(LoanRequest loanRequest) throws BankException {
        Account source = storage.getBankInfo().getLinkedAccount();
        Account destination = loanRequest.getAccount();

        Loan loan = new Loan();
        loan.setAmount(loanRequest.getAmount());
        loan.setReason(loanRequest.getReason());
        loan.setAccount(destination);
        storage.addLoan(loan);

        AccountAction action = new AccountAction();
        action.setType(AccountAction.Type.RECEIVE_LOAN);
        action.setAccount(destination);
        action.setLinkedData(loan);
        action.setTimestamp(new Date());
        storage.addAccountAction(action);

        storage.deleteLoanRequest(loanRequest);

        performTransfer(source, destination, loanRequest.getAmount(),
                "Loan: " + loanRequest.getReason());
    }

    @Override
    public void rejectLoan(LoanRequest loanRequest) throws BankException {
        storage.deleteLoanRequest(loanRequest);
    }

    @Override
    public List<Customer> getCustomers() {
        return storage.getCustomers();
    }

    @Override
    public List<AccountAction> getAccountActions(Account account) {
        return storage.findAccountActions(account);
    }

    @Override
    public List<LoanRequest> getLoanRequests() {
        return storage.getLoanRequests();
    }

    @Override
    public List<TransactionRecord> findTransactionRecords() {
        return storage.findTransactionRecordsForAccount(authManager.getAccountUsed());
    }

    @Override
    public void close() throws Exception {
        transactionSession.close();
    }

    private void broadcastTransaction(TransactionDetails details) {
        if (checkBankId(details.getDestination().getBankId())) {
            // transfer to this bank
            try {
                transactions.receiveMoney(details);
            } catch (BankException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkBankId(String bankId) {
        return storage.getBankInfo().getBankId().equals(bankId);
    }

    private void performTransfer(Account source, Account destination, double amount, String comment)
            throws BankException {
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setSource(source);
        transactionRecord.setDestination(destination);
        transactionRecord.setAmount(amount);
        transactionRecord.setComment(comment);
        transactionRecord.setTimestamp(new Date());

        transactions.sendMoney(transactionRecord);
    }

    private void performInitialAccountTransfer(Account destination) throws BankException {
        Account source = storage.getBankInfo().getLinkedAccount();
        performTransfer(source, destination, 1000, "Initial Transfer");
    }
}
