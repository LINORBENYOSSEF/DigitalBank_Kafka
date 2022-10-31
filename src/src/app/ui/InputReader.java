package app.ui;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Predicate;

public class InputReader {

    private static final String[] YES_OPTIONS = {
            "y", "yes"
    };
    private static final String[] NO_OPTIONS = {
            "n", "no"
    };

    private final PrintStream out;
    private final Scanner in;

    public InputReader(PrintStream out, Scanner in) {
        this.out = out;
        this.in = in;
    }

    public String read(String prompt, Object... args) {
        showPrompt(prompt, args);
        return in.nextLine();
    }

    public int readInt(String prompt, Object... args) {
        return Integer.parseInt(read(prompt, args));
    }

    public double readDouble(String prompt, Object... args) {
        return Double.parseDouble(read(prompt, args));
    }

    public double readDouble(Predicate<Double> validator, String prompt, Object... args) {
        do {
            try {
                double value = Double.parseDouble(read(prompt, args));
                if (validator.test(value)) {
                    return value;
                } else {
                    out.println("Bad number");
                }
            } catch (NumberFormatException e) {
                out.println("Expected number");
            }
        } while (true);
    }

    public boolean readBoolean(String prompt, Object... args) {
        do {
            String input = read(prompt, args);
            if (isInOptions(input, YES_OPTIONS)) {
                return true;
            } else if (isInOptions(input, NO_OPTIONS)) {
                return false;
            }

            out.printf("Unknown option %s, Try again.\n", input);
        } while (true);
    }

    private void showPrompt(String prompt, Object... args) {
        out.printf(prompt, args);
        out.print(" ");
    }

    private boolean isInOptions(String input, String[] options) {
        for (String option : options) {
            if (input.equalsIgnoreCase(option)) {
                return true;
            }
        }

        return false;
    }
}
