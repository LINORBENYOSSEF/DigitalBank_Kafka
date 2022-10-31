package app.ui;

import app.bank.model.Employee;

public class EmployeeMainMenu extends OptionsMenu {

    public EmployeeMainMenu(Employee employee) {
        super("Main Menu");
        addOption("View Account", new ViewEmployeeAccountMenu(employee));
        addOption("View All Accounts", new ViewAllAccountsMenu(employee));
        addOption("Approve Loans", new ViewLoanRequestsFromAllMenu(employee));
        addOption("Logout", new LogoutMenu());
        addOption("Exit");
    }
}
