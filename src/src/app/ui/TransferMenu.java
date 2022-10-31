package app.ui;

import app.bank.BankControl;
import app.bank.BankException;
import app.bank.model.Account;
import app.bank.model.Customer;
import app.bank.transaction.TransactionBuilder;

import java.io.PrintStream;
import java.util.Optional;

public class TransferMenu implements Menu {

    private final Customer customer;
    private final Account account;

    public TransferMenu(Customer customer, Account account) {
        this.customer = customer;
        this.account = account;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        try {
            TransferContext context = new TransferContext(in, bankControl);
            context.askForDestinationAccountDetails();
            context.askForAmountDetails();
            context.send();

            return Optional.of(new CustomerMainMenu(customer, account));
        } catch (BankException e) {
            out.printf("Error: %s\n", e.getMessage());
            return Optional.of(new CustomerMainMenu(customer, account));
        }
    }

    private static class TransferContext {

        private final InputReader in;
        private final TransactionBuilder builder;

        private TransferContext(InputReader in, BankControl bankControl) throws BankException {
            this.in = in;
            this.builder = bankControl.sendMoney();
        }

        public void askForDestinationAccountDetails() throws BankException {
            String bankId = in.read("Destination Bank ID (empty for local):");
            String branch = in.read("Destination branch:");
            String accountNumber = in.read("Destination account number:");

            builder.setDestination(bankId, branch, accountNumber);
        }

        public void askForAmountDetails() throws BankException {
            double amount = in.readDouble("Transfer amount:");
            String comment = in.read("Comment:");

            builder.setAmount(amount);
            builder.setComment(comment);
        }

        public void send() throws BankException {
            builder.send();
        }
    }
}
