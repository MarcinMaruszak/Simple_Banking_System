package banking.logic;

import banking.domain.Account;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Bank {
    private SQLHandling sqlConn;

    public Bank(SQLHandling sqlConnection) {
        this.sqlConn = sqlConnection;
    }

    public SQLHandling getSqlConn() {
        return sqlConn;
    }


    public Account createAccount() throws Exception {
        String cardNmb = generateCardNumber();
        while (sqlConn.cardNmbTaken(cardNmb)) {
            cardNmb = generateCardNumber();
        }
        String pin = String.valueOf(new Random().nextInt(8999) + 1000);
        sqlConn.addAccount(cardNmb, pin);
        return new Account(cardNmb, pin);
    }

    private String generateCardNumber() {
        String accNumb = "400000";
        String rdn = String.valueOf(ThreadLocalRandom.current().nextLong(100000000L,899999999L) * 10);
        accNumb += rdn;
        int[] digits = accNumb.chars().map(c -> c - '0').toArray();
        int lastDigit = lastDigit(digits);
        return accNumb.substring(0, accNumb.length() - 1) + lastDigit;
    }

    public boolean checkCardLuhn(String card) {
        int[] digits = card.chars().map(c -> c - '0').toArray();
        return lastDigit(digits) == 0;
    }

    private int lastDigit(int[] digits) {
        int[] digits2 = Arrays.copyOfRange(digits, 0, digits.length - 1);
        for (int i = 0; i < digits2.length; i++) {
            if (i % 2 == 0) {
                digits2[i] *= 2;
                if (digits2[i] > 9) {
                    digits2[i] -= 9;
                }
            }
        }
        int sum = Arrays.stream(digits2).sum() + digits[digits.length - 1];
        int lastDigit = 10 - (sum % 10);
        if (lastDigit == 10) {
            lastDigit = 0;
        }
        return lastDigit;
    }
}
