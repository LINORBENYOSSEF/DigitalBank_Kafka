package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.Customer;
import app.bank.model.TransactionRecord;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class ViewTransferHistoryMenu implements Menu {

    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
    private final Customer customer;
    private final Account account;

    public ViewTransferHistoryMenu(Customer customer, Account account) {
        this.customer = customer;
        this.account = account;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        List<TransactionRecord> transactionDetails = bankControl
                .findTransactionRecords();

        int option;
        do {
            for (int i = 0; i < transactionDetails.size(); i++) {
                TransactionRecord details = transactionDetails.get(i);
                String source = details.getSource() != null ?
                        details.getSource().getAccountNumber() :
                        "DELETED ACCOUNT";
                String destination = details.getDestination() != null ?
                        details.getDestination().getAccountNumber() :
                        "DELETED ACCOUNT";

                if (details.isRemote()) {
                    out.printf("%d. [REMOTE] %s: %s -> %s\n", i+1,
                            source,
                            destination,
                            dateFormat.format(details.getTimestamp()));
                } else {
                    out.printf("%d. %s: %s -> %s\n", i+1,
                            source,
                            destination,
                            dateFormat.format(details.getTimestamp()));
                }
            }
            out.printf("%d. Back\n", transactionDetails.size()+1);

            option = in.readInt("Select transfer:");

            if (option >= 1 && option <= transactionDetails.size()) {
                TransactionRecord details = transactionDetails.get(option-1);
                out.printf("\nTransfer at %s\n", dateFormat.format(details.getTimestamp()));
                if (details.getSource().equals(customer.getAccounts())) {
                    out.printf("From this account to %s\n",
                            details.getDestination() != null ?
                            details.getDestination() :
                            "DELETED ACCOUNT");
                } else {
                    out.printf("From %s account to this account\n",
                            details.getSource() != null ?
                            details.getSource() :
                            "DELETED ACCOUNT");
                }
                out.printf("%.3f$ transferred\n", details.getAmount());
                if (!details.getComment().isEmpty()) {
                    out.printf("Comment: %s\n", details.getComment());
                }
            } else if (option == transactionDetails.size() + 1) {
                return Optional.of(new CustomerMainMenu(customer, account));
            }
        } while (true);
    }
}
