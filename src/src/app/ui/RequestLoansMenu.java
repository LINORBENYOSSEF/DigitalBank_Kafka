package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.Customer;
import app.bank.model.LoanRequest;

import java.io.PrintStream;
import java.util.Optional;

public class RequestLoansMenu implements Menu {

    private final Customer customer;
    private final Account account;

    public RequestLoansMenu(Customer customer, Account account) {
        this.customer = customer;
        this.account = account;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        double amount = in.readDouble("Loan amount:");
        String reason = in.read("Reason:");

        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setAccount(account);
        loanRequest.setAmount(amount);
        loanRequest.setReason(reason);

        bankControl.requestLoan(loanRequest);

        return Optional.of(new CustomerMainMenu(customer, account));
    }
}
