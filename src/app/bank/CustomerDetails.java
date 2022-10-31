package app.bank;

import app.bank.auth.LoginDetails;

public class CustomerDetails {

    private final LoginDetails loginDetails;

    public CustomerDetails(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }

    public LoginDetails getLoginDetails() {
        return loginDetails;
    }
}
