package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.Customer;

import java.io.PrintStream;
import java.util.Optional;

public class CloseAccountMenu implements Menu {

    private final Customer customer;
    private final Account account;

    public CloseAccountMenu(Customer customer, Account account) {
        this.customer = customer;
        this.account = account;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        if (in.readBoolean("Are you sure (Y/N)?")) {
            bankControl.closeCustomerAccount();
            return Optional.of(new LogoutMenu());
        } else {
            return Optional.of(new CustomerMainMenu(customer, account));
        }
    }
}
