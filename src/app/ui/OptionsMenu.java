package app.ui;

import app.bank.BankControl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionsMenu implements Menu {

    private final String title;
    private final List<Option> options;

    public OptionsMenu(String title) {
        this.title = title;
        this.options = new ArrayList<>();
    }

    public void addOption(String name, Menu menu) {
        options.add(new Option(name, menu));
    }

    public void addOption(String name) {
        options.add(new Option(name, null));
    }

    @Override
    public Optional<? extends Menu> showMenu(PrintStream out, InputReader in, BankControl bankControl) {
        out.printf("%s:\n", title);
        for (int i = 0; i < options.size(); i++) {
            out.printf("%d. %s\n", i + 1, options.get(i).name);
        }

        return Optional.ofNullable(getChoice(out, in).menu);
    }

    private Option getChoice(PrintStream out, InputReader in) {
        int option;
        do {
            option = in.readInt("Select option:");

            if (option >= 1 && option <= options.size()) {
                return options.get(option-1);
            }

            out.println("Bad option");
        } while (option < 1 || option > options.size());

        throw new AssertionError("should not reach");
    }

    private static class Option {
        private String name;
        private Menu menu;

        private Option(String name, Menu menu) {
            this.name = name;
            this.menu = menu;
        }
    }
}
