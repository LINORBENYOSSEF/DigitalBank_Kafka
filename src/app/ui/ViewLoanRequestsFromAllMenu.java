package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.Employee;
import app.bank.model.LoanRequest;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViewLoanRequestsFromAllMenu implements Menu {

    private final Employee employee;

    public ViewLoanRequestsFromAllMenu(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        List<LoanRequest> requests = bankControl.getLoanRequests();

        Map<Account, List<LoanRequest>> requestsPerAccount = requests.stream()
                .collect(Collectors.groupingBy(LoanRequest::getAccount));

        OptionsMenu optionsMenu = new OptionsMenu("Select Request");
        for (Account account : requestsPerAccount.keySet()) {
            optionsMenu.addOption(String.format("Request from %s",
                    account.getAccountNumber()),
                    new ViewLoanRequestsFromAccountMenu(employee, account, requestsPerAccount.get(account)));
        }
        optionsMenu.addOption("Back", new EmployeeMainMenu(employee));

        return Optional.of(optionsMenu);
    }
}
