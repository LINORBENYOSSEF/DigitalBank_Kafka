package app.ui;

import app.bank.BankControl;
import app.bank.model.LoanRequest;

import java.io.PrintStream;
import java.util.Optional;

public class ApproveLoanRequestMenu implements Menu {

    private final Menu callingMenu;
    private final LoanRequest request;

    public ApproveLoanRequestMenu(Menu callingMenu, LoanRequest request) {
        this.callingMenu = callingMenu;
        this.request = request;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        out.printf("Request from %s\n", request.getAccount().getAccountNumber());
        out.printf("Reason: %s\n", request.getReason());
        out.printf("Amount: %.3f\n", request.getAmount());

        if (in.readBoolean("Approve? (Y/N)")) {
            bankControl.acceptLoan(request);
            out.println("Request approved");
        } else {
            bankControl.rejectLoan(request);
            out.println("Request rejected");
        }

        return Optional.of(callingMenu);
    }
}
