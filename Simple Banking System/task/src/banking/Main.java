package banking;

import banking.UserInterface.UserInterface;
import banking.logic.Bank;
import banking.logic.SQLHandling;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank(new SQLHandling(args[1]));
        new UserInterface(scanner, bank).start();
    }
}