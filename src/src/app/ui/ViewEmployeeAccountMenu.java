package app.ui;

import app.bank.BankControl;
import app.bank.model.Employee;

import java.io.PrintStream;
import java.util.Optional;

public class ViewEmployeeAccountMenu implements Menu {

    private final Employee employee;

    public ViewEmployeeAccountMenu(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        out.printf("Username: %s\n", employee.getUsername());
        out.printf("Password: %s\n", employee.getPassword());

        return Optional.of(new EmployeeMainMenu(employee));
    }
}
