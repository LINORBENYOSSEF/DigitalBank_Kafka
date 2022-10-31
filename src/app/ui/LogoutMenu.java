package app.ui;

import app.bank.BankControl;

import java.io.PrintStream;
import java.util.Optional;

public class LogoutMenu implements Menu {

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception {
        bankControl.logout();
        return Optional.of(new AuthenticationMenu());
    }
}
