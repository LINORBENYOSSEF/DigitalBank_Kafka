package app.bank.storage;

import app.bank.model.Account;
import app.bank.model.AccountAction;
import app.bank.model.BankInfo;
import app.bank.model.Customer;
import app.bank.model.Employee;
import app.bank.model.Loan;
import app.bank.model.LoanRequest;
import app.bank.model.TransactionRecord;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Storage {

    List<Customer> getCustomers();
    Optional<Customer> findCustomer(Predicate<? super Customer> predicate);
    Optional<Customer> findCustomerByUsername(String username);
    void addCustomer(Customer customer);
    void deleteCustomer(Customer customer);

    List<Employee> getEmployees();
    Optional<Employee> findEmployee(Predicate<? super Employee> predicate);
    Optional<Employee> findEmployeeByUsername(String username);
    void addEmployee(Employee employee);

    List<Account> getAccounts();
    Optional<Account> findAccount(Predicate<? super Account> predicate);
    Optional<Account> findAccountById(String id);
    void addAccount(Account account);
    void deleteAccount(Account account);

    List<LoanRequest> getLoanRequests();
    Optional<LoanRequest> findLoanRequestById(String id);
    void addLoanRequest(LoanRequest loanRequest);
    void deleteLoanRequest(LoanRequest loanRequest);

    List<Loan> getLoans();
    Optional<Loan> findLoanById(String id);
    void addLoan(Loan loan);

    BankInfo getBankInfo();
    void setBankInfo(BankInfo bankInfo);

    List<TransactionRecord> getTransactionRecords();
    List<TransactionRecord> findTransactionRecords(Predicate<? super TransactionRecord> predicate);
    List<TransactionRecord> findTransactionRecordsForAccount(Account account);
    Optional<TransactionRecord> findTransactionRecordById(String id);
    void addTransactionRecord(TransactionRecord transactionRecord);

    List<AccountAction> getAccountActions();
    List<AccountAction> findAccountActions(Account account);
    void addAccountAction(AccountAction accountAction);

    boolean load() throws StorageException;
    void save() throws StorageException;
}
