package app.bank.storage;

import app.bank.model.Account;
import app.bank.model.AccountAction;
import app.bank.model.BankInfo;
import app.bank.model.Customer;
import app.bank.model.Employee;
import app.bank.model.Loan;
import app.bank.model.LoanRequest;
import app.bank.model.Model;
import app.bank.model.TransactionRecord;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class FileStorage implements Storage {

    private final Path path;
    private final Lock mSaveLock;
    private final ModelSerializer serializer;

    private BankInfo bankInfo;
    private final List<Customer> customers;
    private final List<Employee> employees;
    private final List<Account> accounts;
    private final List<TransactionRecord> transactionRecords;
    private final List<LoanRequest> loanRequests;
    private final List<Loan> loans;
    private final List<AccountAction> accountActions;

    public FileStorage(Path path) {
        this.path = path;
        mSaveLock = new ReentrantLock();
        serializer = new ModelSerializer();

        bankInfo = new BankInfo();
        customers = new CopyOnWriteArrayList<>();
        employees = new CopyOnWriteArrayList<>();
        accounts = new CopyOnWriteArrayList<>();
        transactionRecords = new CopyOnWriteArrayList<>();
        loanRequests = new CopyOnWriteArrayList<>();
        loans = new CopyOnWriteArrayList<>();
        accountActions = new CopyOnWriteArrayList<>();
    }

    @Override
    public List<Customer> getCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> findCustomer(Predicate<? super Customer> predicate) {
        for (Customer customer : customers) {
            if (predicate.test(customer)) {
                return Optional.of(customer);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> findCustomerByUsername(String username) {
        return findCustomer((customer)-> customer.getUsername().equals(username));
    }

    @Override
    public void addCustomer(Customer customer) {
        customer.setId(UUID.randomUUID().toString());
        customers.add(customer);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customers.remove(customer);
        for (Account account : customer.getAccounts()) {
            deleteAccount(account);
        }

        loanRequests.removeIf((loanRequest)-> customer.hasAccount(loanRequest.getAccount()));
    }

    @Override
    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public Optional<Employee> findEmployee(Predicate<? super Employee> predicate) {
        for (Employee employee : employees) {
            if (predicate.test(employee)) {
                return Optional.of(employee);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Employee> findEmployeeByUsername(String username) {
        return findEmployee((employee)-> employee.getUsername().equals(username));
    }

    @Override
    public void addEmployee(Employee employee) {
        employee.setId(UUID.randomUUID().toString());
        employees.add(employee);
    }

    @Override
    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public Optional<Account> findAccount(Predicate<? super Account> predicate) {
        for (Account account : accounts) {
            if (predicate.test(account)) {
                return Optional.of(account);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Account> findAccountById(String id) {
        return findAccount((account)-> account.getId().equals(id));
    }

    @Override
    public void addAccount(Account account) {
        account.setId(UUID.randomUUID().toString());
        accounts.add(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accounts.remove(account);

        for (TransactionRecord transactionRecord : transactionRecords) {
            if (account.equals(transactionRecord.getSource())) {
                transactionRecord.setSource(null);
            }
            if (account.equals(transactionRecord.getDestination())) {
                transactionRecord.setDestination(null);
            }
        }

        loans.removeIf((loan)-> loan.getAccount().equals(account));
    }

    @Override
    public List<LoanRequest> getLoanRequests() {
        return loanRequests;
    }

    @Override
    public Optional<LoanRequest> findLoanRequestById(String id) {
        for (LoanRequest loanRequest : loanRequests) {
            if (loanRequest.getId().equals(id)) {
                return Optional.of(loanRequest);
            }
        }

        return Optional.empty();
    }

    @Override
    public void addLoanRequest(LoanRequest loanRequest) {
        loanRequests.add(loanRequest);
    }

    @Override
    public void deleteLoanRequest(LoanRequest loanRequest) {
        loanRequests.remove(loanRequest);
    }

    @Override
    public List<Loan> getLoans() {
        return loans;
    }

    @Override
    public Optional<Loan> findLoanById(String id) {
        for (Loan loan : loans) {
            if (id.equals(loan.getId())) {
                return Optional.of(loan);
            }
        }

        return Optional.empty();
    }

    @Override
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    @Override
    public BankInfo getBankInfo() {
        return bankInfo;
    }

    @Override
    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

    @Override
    public List<TransactionRecord> getTransactionRecords() {
        return transactionRecords;
    }

    @Override
    public List<TransactionRecord> findTransactionRecords(Predicate<? super TransactionRecord> predicate) {
        List<TransactionRecord> filtered = new ArrayList<>();
        for (TransactionRecord transactionRecord : transactionRecords) {
            if (predicate.test(transactionRecord)) {
                filtered.add(transactionRecord);
            }
        }

        return filtered;
    }

    @Override
    public List<TransactionRecord> findTransactionRecordsForAccount(Account account) {
        return findTransactionRecords((transactionDetails)->
                account.equals(transactionDetails.getSource()) ||
                account.equals(transactionDetails.getDestination()));
    }

    @Override
    public Optional<TransactionRecord> findTransactionRecordById(String id) {
        List<TransactionRecord> list = findTransactionRecords((transactionDetails)->
                transactionDetails.getId().equals(id));
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public void addTransactionRecord(TransactionRecord transactionRecord) {
        transactionRecord.setId(UUID.randomUUID().toString());
        this.transactionRecords.add(transactionRecord);
    }

    @Override
    public List<AccountAction> getAccountActions() {
        return accountActions;
    }

    @Override
    public List<AccountAction> findAccountActions(Account account) {
        List<AccountAction> filtered = new ArrayList<>();
        for (AccountAction accountAction : accountActions) {
            if (accountAction.getAccount().getId().equals(account.getId())) {
                filtered.add(accountAction);
            }
        }

        return filtered;
    }

    @Override
    public void addAccountAction(AccountAction accountAction) {
        accountActions.add(accountAction);
    }

    @Override
    public boolean load() throws StorageException {
        mSaveLock.lock();
        try (InputStream inputStream = Files.newInputStream(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {

            bankInfo.readFrom(dataInputStream);
            serializer.readList(dataInputStream, customers, Customer::new);
            serializer.readList(dataInputStream, employees, Employee::new);
            serializer.readList(dataInputStream, accounts, Account::new);
            serializer.readList(dataInputStream, transactionRecords, TransactionRecord::new);
            serializer.readList(dataInputStream, loanRequests, LoanRequest::new);
            serializer.readList(dataInputStream, loans, Loan::new);
            serializer.readList(dataInputStream, accountActions, AccountAction::new);

            doPostLoad();
            return true;
        } catch (NoSuchFileException e) {
            // that's fine
            return false;
        } catch (IOException e) {
            throw new StorageException(e);
        } finally {
            mSaveLock.unlock();
        }
    }

    @Override
    public void save() throws StorageException {
        mSaveLock.lock();
        try (OutputStream outputStream = Files.newOutputStream(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {

            bankInfo.writeInto(dataOutputStream);
            serializer.writeList(dataOutputStream, customers);
            serializer.writeList(dataOutputStream, employees);
            serializer.writeList(dataOutputStream, accounts);
            serializer.writeList(dataOutputStream, transactionRecords);
            serializer.writeList(dataOutputStream, loanRequests);
            serializer.writeList(dataOutputStream, loans);
            serializer.writeList(dataOutputStream, accountActions);
        } catch (IOException e) {
            throw new StorageException(e);
        } finally {
            mSaveLock.unlock();
        }
    }

    private void doPostLoad() throws StorageException {
        bankInfo.postLoad(this);
        doPostLoad(customers);
        doPostLoad(employees);
        doPostLoad(accounts);
        doPostLoad(transactionRecords);
        doPostLoad(loanRequests);
        doPostLoad(loans);
        doPostLoad(accountActions);
    }

    private void doPostLoad(List<? extends Model> models) throws StorageException {
        for (Model model : models) {
            model.postLoad(this);
        }
    }
}
