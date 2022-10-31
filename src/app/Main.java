package app;

import app.bank.BankControl;
import app.bank.BankControlImpl;
import app.bank.auth.AuthManager;
import app.bank.model.Account;
import app.bank.model.BankInfo;
import app.bank.model.Employee;
import app.bank.storage.FileStorage;
import app.bank.storage.Storage;
import app.ui.AuthenticationMenu;
import app.ui.InputReader;
import app.ui.Menu;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final String BANK_ID = "afeka03";

    public static void main(String[] args) throws Exception {
        Storage storage = new FileStorage(Paths.get("storage.bin"));
        if (!storage.load()) {
            onFirstLoad(storage);
            storage.save();
        }

        AuthManager authManager = new AuthManager(storage);
        try (Scanner rawInt = new Scanner(System.in);
             BankControl bankControl = new BankControlImpl(storage, authManager)){
            InputReader in = new InputReader(System.out, rawInt);
            Menu menu = new AuthenticationMenu();
            do {
                try {
                    Optional<? extends Menu> optional = menu.showMenu(System.out, in, bankControl);
                    if (optional.isPresent()) {
                        menu = optional.get();
                    } else {
                        break;
                    }
                } catch (Throwable t) {
                    System.out.printf("Error: %s\n", t.getMessage());
                    t.printStackTrace();
                }
            } while (true);
        }
    }

    private static void onFirstLoad(Storage storage) {
        Account account = new Account();
        account.setAccountNumber("BANK");
        account.setBankId(BANK_ID);
        account.setBranch("BANK");
        account.setBalance(Account.UNLIMITED_BALANCE);
        storage.addAccount(account);

        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankId(BANK_ID);
        bankInfo.setLinkedAccount(account);
        storage.setBankInfo(bankInfo);

        Employee admin = new Employee();
        admin.setUsername("admin");
        admin.setPassword("admin");
        storage.addEmployee(admin);
    }
}
