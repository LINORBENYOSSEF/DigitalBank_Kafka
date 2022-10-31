package app.ui;

import app.bank.BankControl;
import app.bank.model.AccountAction;
import app.bank.model.Customer;
import app.bank.model.Loan;
import app.bank.model.LoanRequest;
import app.bank.model.TransactionRecord;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

public class ViewAccountAction implements Menu {

    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");

    private final Customer customer;
    private final AccountAction accountAction;
    private final Menu backMenu;

    public ViewAccountAction(Customer customer, AccountAction accountAction, Menu backMenu) {
        this.customer = customer;
        this.accountAction = accountAction;
        this.backMenu = backMenu;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        out.printf("Action: %s\n", accountAction.getType());
        out.printf("Done at: %s\n", dateFormat.format(accountAction.getTimestamp()));

        switch (accountAction.getType()) {
            case SEND_MONEY:
            case RECEIVE_MONEY:
                TransactionRecord details = (TransactionRecord) accountAction.getLinkedData();
                out.printf("Transfer at %s\n", dateFormat.format(details.getTimestamp()));
                if (Objects.equals(details.getSource(), accountAction.getAccount())) {
                    out.printf("From this account to %s\n", details.getDestination());
                } else {
                    out.printf("From %s account to this account\n", details.getSource());
                }
                out.printf("%.3f$ transferred\n", details.getAmount());
                if (!details.getComment().isEmpty()) {
                    out.printf("Comment: %s\n", details.getComment());
                }
                break;
            case REQUEST_LOAN:
                LoanRequest request = (LoanRequest) accountAction.getLinkedData();
                out.println("Loan requested");
                out.printf("%s: %.3f$\n", request.getReason(), request.getAmount());
                break;
            case RECEIVE_LOAN:
                Loan loan = (Loan) accountAction.getLinkedData();
                out.println("Loan received");
                out.printf("%s: %.3f$\n", loan.getReason(), loan.getAmount());
                break;
        }

        return Optional.of(new ViewAccountActions(customer, accountAction.getAccount(), backMenu));
    }
}
