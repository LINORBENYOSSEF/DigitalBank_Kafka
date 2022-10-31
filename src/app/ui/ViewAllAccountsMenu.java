package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.Customer;
import app.bank.model.Employee;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

public class ViewAllAccountsMenu implements Menu {

    private final Employee employee;

    public ViewAllAccountsMenu(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        List<Customer> customers = bankControl.getCustomers();

        OptionsMenu menu = new OptionsMenu("Select Account");
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                menu.addOption(account.getAccountNumber(), new ViewCustomerAccountMenu(customer, account, this));
            }
        }
        menu.addOption("Back", new EmployeeMainMenu(employee));

        return Optional.of(menu);
    }
}
