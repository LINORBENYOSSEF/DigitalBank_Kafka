package app.ui;

import app.bank.BankControl;
import app.bank.BankException;
import app.bank.CustomerDetails;
import app.bank.auth.LoginDetails;
import app.bank.model.Customer;
import app.bank.model.Employee;

import java.io.PrintStream;
import java.util.Optional;

public class AuthenticationMenu extends OptionsMenu {

    public AuthenticationMenu() {
        super("Authentication");
        addOption("Log Into Account", this::loginCustomer);
        addOption("Open Account", this::openAccount);
        addOption("Log As Employee", this::loginEmployee);
        addOption("Exit");
    }

    private Optional<? extends Menu> loginCustomer(PrintStream out, InputReader in, BankControl bankControl) {
        do {
            String username = in.read("Enter Username:");
            String password = in.read("Enter Password:");

            try {
                LoginDetails loginDetails = new LoginDetails(username, password);

                Customer customer = bankControl.loginCustomer(loginDetails);
                return Optional.of(new AccountSelectionMenu(customer));
            } catch (BankException e) {
                out.printf("Error: %s\n", e.getMessage());
            }
        } while (true);
    }

    private Optional<? extends Menu> openAccount(PrintStream out, InputReader in, BankControl bankControl) {
        do {
            String username = in.read("Enter Username:");
            String password = in.read("Enter Password:");

            try {
                LoginDetails loginDetails = new LoginDetails(username, password);
                bankControl.openCustomerAccount(new CustomerDetails(
                        loginDetails
                ));

                Customer customer = bankControl.loginCustomer(loginDetails);
                return Optional.of(new AccountSelectionMenu(customer));
            } catch (BankException e) {
                out.printf("Error: %s\n", e.getMessage());
            }
        } while (true);
    }

    private Optional<? extends Menu> loginEmployee(PrintStream out, InputReader in, BankControl bankControl) {
        do {
            String username = in.read("Enter Username:");
            String password = in.read("Enter Password:");

            try {
                LoginDetails loginDetails = new LoginDetails(username, password);

                Employee employee = bankControl.loginEmployee(loginDetails);
                return Optional.of(new EmployeeMainMenu(employee));
            } catch (BankException e) {
                out.printf("Error: %s\n", e.getMessage());
            }
        } while (true);
    }
}
