package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.Customer;

import java.io.PrintStream;
import java.util.Optional;

public class EditAccountMenu implements Menu {

    private final Customer customer;
    private final Account account;

    public EditAccountMenu(Customer customer, Account account) {
        this.customer = customer;
        this.account = account;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        boolean changed = false;
        if (in.readBoolean("Current username: %s. Edit (Y/N)?", customer.getUsername())) {
            String username = in.read("Enter new username:");
            customer.setUsername(username);
            changed = true;
        }
        if (in.readBoolean("Current password: %s. Edit (Y/N)?", customer.getPassword())) {
            String password = in.read("Enter new password:");
            customer.setPassword(password);
            changed = true;
        }

        if (changed) {
            bankControl.editCustomerAccount(customer);
        }

        return Optional.of(new CustomerMainMenu(customer, account));
    }
}
