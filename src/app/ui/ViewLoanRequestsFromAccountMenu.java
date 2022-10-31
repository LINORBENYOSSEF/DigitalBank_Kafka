package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.Employee;
import app.bank.model.LoanRequest;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

public class ViewLoanRequestsFromAccountMenu implements Menu {

    private final Employee employee;
    private final Account accountToView;
    private final List<LoanRequest> requests;

    public ViewLoanRequestsFromAccountMenu(Employee employee, Account accountToView, List<LoanRequest> requests) {
        this.employee = employee;
        this.accountToView = accountToView;
        this.requests = requests;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        if (requests.isEmpty()) {
            return Optional.of(new ViewLoanRequestsFromAllMenu(employee));
        }

        int option;
        do {
            out.printf("Requests from %s\n", accountToView.getAccountNumber());
            for (int i = 0; i < requests.size(); i++) {
                out.printf("%d. %s: %.3f$\n", i+1, requests.get(i).getReason(), requests.get(i).getAmount());
            }
            out.printf("%d. Back\n", requests.size()+1);

            option = in.readInt("Select request:");

            if (option >= 1 && option <= requests.size()) {
                LoanRequest request = requests.get(option-1);
                requests.remove(option-1);
                return Optional.of(new ApproveLoanRequestMenu(this, request));
            } else if (option == requests.size() + 1) {
                return Optional.of(new ViewLoanRequestsFromAllMenu(employee));
            }

            out.println("Bad option");
        } while (true);
    }
}
