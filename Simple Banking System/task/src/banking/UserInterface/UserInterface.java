package banking.UserInterface;

import banking.domain.Account;
import banking.logic.Bank;

import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final Bank bank;
    private boolean exit;

    public UserInterface(Scanner scanner, Bank bank) {
        this.scanner = scanner;
        this.bank = bank;
        exit = false;
    }

    public void start() {
        try {
            bank.getSqlConn().connectAndCheck();
            menu();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to Database");
        }
    }

    private void menu() {
        while (!exit) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    login();
                    break;
                case "0":
                    System.out.println("Bye!");
                    exit = true;
            }
        }
    }

    private void createAccount() {
        try {
            Account account = bank.createAccount();
            System.out.println("\nYour card has been created");
            System.out.println("Your card number:");
            System.out.println(account.getCreditCard());
            System.out.println("Your card PIN:");
            System.out.println(account.getPin() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Account not created");
        }
    }

    private void login() {
        System.out.println("\nEnter your card number:");
        String cardNumber = scanner.nextLine().trim();
        System.out.println("Enter your PIN:");
        String pin = (scanner.nextLine());
        try {
            accountUI(bank.getSqlConn().login(cardNumber, pin));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void accountUI(String card) {
        System.out.println("\nYou have successfully logged in!\n");
        label:
        while (true) {
            printAccountMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    getBalance(card);
                    break;
                case "2":
                    System.out.println("\nEnter income:");
                    int income = Integer.parseInt(scanner.nextLine());
                    addIncome(card, income);
                    break;
                case "3":
                    doTransfer(card);
                    break;
                case "4":
                    close(card);
                    break;
                case "5":
                    System.out.println("You have successfully logged out!");
                    break label;
                case "0":
                    System.out.println("\nBye!");
                    exit = true;
                    break label;
            }
        }
    }

    private void printAccountMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    private void getBalance(String card) {
        try {
            int balance = bank.getSqlConn().getBalance(card);
            System.out.println("\nBalance: " + balance + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void addIncome(String card, int income) {
        try {
            bank.getSqlConn().addIncome(card, income);
            System.out.println("Income was added!\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Adding income failed\n");
        }
    }

    private void doTransfer(String card) {
        System.out.println("Enter card number:");
        String targetCard = scanner.nextLine();
        if (!card.equals(targetCard)) {
            if (bank.checkCardLuhn(targetCard)) {
                if (bank.getSqlConn().cardNmbTaken(targetCard)) { //card taken == exist
                    System.out.println("Enter how much money you want to transfer:");
                    int transferAmount = Integer.parseInt(scanner.nextLine());
                    try {
                        if (transferAmount <= bank.getSqlConn().getBalance(card)) {
                            bank.getSqlConn().doTransfer(card, targetCard, transferAmount);
                            System.out.println("Success!\n");
                        } else {
                            System.out.println("Not enough money!\n");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Such a card does not exist.");
                }
            } else {
                System.out.println("Probably you made mistake in the card number. Please try again!\n");
            }
        } else {
            System.out.println("You can't transfer money to the same account!\n");
        }
    }

    private void close(String card) {
        try {
            bank.getSqlConn().closeAccount(card);
            System.out.println("The account has been closed!\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


