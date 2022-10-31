package app.bank;

import app.bank.auth.LoginDetails;
import app.bank.model.Account;
import app.bank.model.AccountAction;
import app.bank.model.Customer;
import app.bank.model.Employee;
import app.bank.model.LoanRequest;
import app.bank.model.TransactionRecord;
import app.bank.transaction.TransactionBuilder;

import java.util.List;

public interface BankControl extends AutoCloseable {

    void openCustomerAccount(CustomerDetails customerDetails) throws BankException;
    void openAccount(AccountDetails accountDetails) throws BankException;
    void editCustomerAccount(Customer customer) throws BankException;
    void closeCustomerAccount() throws BankException;

    Customer loginCustomer(LoginDetails credentials) throws BankException;
    void useAccount(Account account);
    Employee loginEmployee(LoginDetails credentials) throws BankException;
    void logout() throws BankException;

    TransactionBuilder sendMoney() throws BankException;

    void requestLoan(LoanRequest loanRequest) throws BankException;
    void acceptLoan(LoanRequest loanRequest) throws BankException;
    void rejectLoan(LoanRequest loanRequest) throws BankException;

    List<Customer> getCustomers();
    List<AccountAction> getAccountActions(Account account);
    List<LoanRequest> getLoanRequests();
    List<TransactionRecord> findTransactionRecords();
}
