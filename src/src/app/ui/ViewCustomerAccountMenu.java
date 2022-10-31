package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.Customer;

import java.io.PrintStream;
import java.util.Optional;

public class ViewCustomerAccountMenu implements Menu {

    private final Customer customer;
    private final Account account;
    private final Menu backMenu;

    public ViewCustomerAccountMenu(Customer customer, Account account, Menu backMenu) {
        this.customer = customer;
        this.account = account;
        this.backMenu = backMenu;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        out.printf("Username: %s\n", customer.getUsername());
        out.printf("Password: %s\n", customer.getPassword());

        if (account != null) {
            out.printf("Account Bank ID: %s\n", account.getBankId());
            out.printf("Account branch: %s\n", account.getBranch());
            out.printf("Account number: %s\n", account.getAccountNumber());
            out.printf("Balance: %.3f\n", account.getBalance());

            OptionsMenu menu = new OptionsMenu("Next");
            menu.addOption("View Actions History", new ViewAccountActions(customer, account, backMenu));
            menu.addOption("Main Menu", backMenu);

            return Optional.of(menu);
        } else {
            return Optional.of(backMenu);
        }
    }
}
