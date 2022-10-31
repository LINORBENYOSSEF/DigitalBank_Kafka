package app.ui;

import app.bank.model.Account;
import app.bank.model.Customer;

public class CustomerMainMenu extends OptionsMenu {

    public CustomerMainMenu(Customer customer, Account account) {
        super("Main Menu");
        addOption("View Account", new ViewCustomerAccountMenu(customer, account, this));
        addOption("Edit Account", new EditAccountMenu(customer, account));
        addOption("Send Money", new TransferMenu(customer, account));
        addOption("Request Loan", new RequestLoansMenu(customer, account));
        addOption("View Transfer History", new ViewTransferHistoryMenu(customer, account));
        addOption("View Actions History", new ViewAccountActions(customer, account, this));
        addOption("Close Account", new CloseAccountMenu(customer, account));
        addOption("Logout", new LogoutMenu());
        addOption("Exit");
    }
}
