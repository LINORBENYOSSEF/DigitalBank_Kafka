package app.ui;

import app.bank.AccountDetails;
import app.bank.BankControl;
import app.bank.BankException;
import app.bank.model.Account;
import app.bank.model.Customer;

import java.io.PrintStream;
import java.util.Optional;

public class AccountSelectionMenu extends OptionsMenu {

    private final Customer customer;

    public AccountSelectionMenu(Customer customer) {
        super("Select Account");
        this.customer = customer;

        addOption("Open Account", this::openAccount);
        for (Account account : customer.getAccounts()) {
            addOption("Use " + account.getAccountNumber(), (out, in, bankControl)-> {
                bankControl.useAccount(account);
                return Optional.of(new CustomerMainMenu(customer, account));
            });
        }
        addOption("Logout", new AuthenticationMenu());
        addOption("Exit");
    }

    private Optional<? extends Menu> openAccount(PrintStream out, InputReader in, BankControl bankControl) {
        do {
            String branch = in.read("Enter branch:");

            try {
                bankControl.openAccount(new AccountDetails(branch));
                return Optional.of(new AccountSelectionMenu(customer));
            } catch (BankException e) {
                out.printf("Error: %s\n", e.getMessage());
            }
        } while (true);
    }
}
