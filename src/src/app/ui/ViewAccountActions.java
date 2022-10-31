package app.ui;

import app.bank.BankControl;
import app.bank.model.Account;
import app.bank.model.AccountAction;
import app.bank.model.Customer;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class ViewAccountActions implements Menu {

    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");

    private final Customer customer;
    private final Account account;
    private final Menu backMenu;

    public ViewAccountActions(Customer customer, Account account, Menu backMenu) {
        this.customer = customer;
        this.account = account;
        this.backMenu = backMenu;
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        int option;
        do {
            List<AccountAction> actions = bankControl.getAccountActions(account);
            for (int i = 0; i < actions.size(); i++) {
                out.printf("%d. %s: %s\n", i+1, dateFormat.format(actions.get(i).getTimestamp()),
                        actions.get(i).getType());
            }
            out.printf("%d. Back\n", actions.size()+1);

            option = in.readInt("Select action:");

            if (option >= 1 && option <= actions.size()) {
                return Optional.of(new ViewAccountAction(customer, actions.get(option-1), backMenu));
            } else if (option == actions.size() + 1) {
                return Optional.of(backMenu);
            }

            out.println("Bad option");
        } while (true);
    }
}
