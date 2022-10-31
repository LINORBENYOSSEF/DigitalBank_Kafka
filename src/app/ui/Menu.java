package app.ui;

import app.bank.BankControl;

import java.io.PrintStream;
import java.util.Optional;

@FunctionalInterface
public interface Menu {

    Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) throws Exception;
}
