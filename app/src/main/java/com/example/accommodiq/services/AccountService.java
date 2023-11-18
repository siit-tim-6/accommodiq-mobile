package com.example.accommodiq.services;

import com.example.accommodiq.models.Account;

import java.util.ArrayList;

public class AccountService {
    private static AccountService instance = null;

    public static AccountService getInstance() {
        if (instance == null) instance = new AccountService();
        return instance;
    }

    private final ArrayList<Account> accounts;
    private Account loggedInAccount;
    private AccountService() {
        Account guestAccount = new Account("guest@example.com", "guestPassword", "Guest", "User", "Guest Address", "1234567890", Account.AccountType.GUEST);
        Account ownerAccount = new Account("owner@example.com", "ownerPassword", "Owner", "User", "Owner Address", "9876543210", Account.AccountType.OWNER);
        Account adminAccount = new Account("admin@example.com", "adminPassword", "Admin", "User", "Admin Address", "5555555555", Account.AccountType.ADMIN);

        accounts = new ArrayList<>();
        accounts.add(guestAccount);
        accounts.add(ownerAccount);
        accounts.add(adminAccount);
    }

    public boolean logIn(String email, String password) {
        for (Account account : accounts) {
            if (account.getEmail().equals(email) && account.getPassword().equals(password)) {
                loggedInAccount = account;
                return true;
            }
        }
        return false;
    }

    public boolean register(Account account) {
        if (accounts.contains(account)) return false;
        accounts.add(account);
        loggedInAccount = account;
        return true;
    }

    public Account getLoggedInAccount() {
        return loggedInAccount;
    }

}
