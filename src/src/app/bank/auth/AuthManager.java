package app.bank.auth;

import app.bank.model.Account;
import app.bank.model.Customer;
import app.bank.model.Employee;
import app.bank.storage.Storage;

import java.util.Optional;

public class AuthManager {

    private final Storage storage;
    private Customer loggedCustomer;
    private Account accountUsed;
    private Employee loggedEmployee;

    public AuthManager(Storage storage) {
        this.storage = storage;
        loggedCustomer = null;
        accountUsed = null;
        loggedEmployee = null;
    }

    public Customer loginCustomer(LoginDetails credentials) throws AuthException {
        Optional<Customer> optional = storage.findCustomerByUsername(credentials.getUsername());
        if (!optional.isPresent()) {
            throw new AuthException("Bad credentials");
        }

        Customer customer = optional.get();
        if (!customer.getPassword().equals(credentials.getPassword())) {
            throw new AuthException("Bad credentials");
        }

        loggedCustomer = customer;
        return customer;
    }

    public void useAccount(Account account) {
        accountUsed = account;
    }

    public Employee loginEmployee(LoginDetails credentials) throws AuthException {
        Optional<Employee> optional = storage.findEmployeeByUsername(credentials.getUsername());
        if (!optional.isPresent()) {
            throw new AuthException("Bad credentials");
        }

        Employee employee = optional.get();
        if (!employee.getPassword().equals(credentials.getPassword())) {
            throw new AuthException("Bad credentials");
        }

        loggedEmployee = employee;
        return employee;
    }

    public void logout() {
        loggedCustomer = null;
        accountUsed = null;
        loggedEmployee = null;
    }

    public Customer getLoggedCustomer() {
        return loggedCustomer;
    }

    public Account getAccountUsed() {
        return accountUsed;
    }

    public Employee getLoggedEmployee() {
        return loggedEmployee;
    }
}
