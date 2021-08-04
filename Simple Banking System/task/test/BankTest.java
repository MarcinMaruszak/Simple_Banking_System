import banking.domain.Account;
import banking.logic.Bank;
import banking.logic.SQLHandling;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankTest {

    Bank bank;
    SQLHandling sqlHandling;
    Account account;

    @BeforeAll
    void beforeAll() {
        sqlHandling = new SQLHandling();
        bank = new Bank(sqlHandling);
    }

    @BeforeEach
    void beforeEach() throws Exception {
        account = bank.createAccount();
    }

    @Test
    public void testCreateAccount() {
        assertTrue(bank.checkCardLuhn(account.getCreditCard()));
    }

    @Test
    public void lunAlgorithmChecker() {
        boolean correctNumber = bank.checkCardLuhn("4950324502881572");
        assertTrue(correctNumber);
    }

    @Test
    public void testCheckIfCardNumberTaken() {
        assertTrue(sqlHandling.cardNmbTaken(account.getCreditCard()));
    }

    @Test
    public void testBalance() throws Exception {
        assertEquals(0, bank.getSqlConn().getBalance(account.getCreditCard()));
    }


    @ParameterizedTest
    @CsvSource({"100, 100", "0, 0"})
    public void testBalanceAfterAddingIncome(int amount, int expected) throws Exception {
        sqlHandling.addIncome(account.getCreditCard(), amount);
        assertEquals(expected, sqlHandling.getBalance(account.getCreditCard()));
    }

    @Test
    public void testLogin() throws Exception {
        String card = sqlHandling.login(account.getCreditCard(), account.getPin());
        assertEquals(account.getCreditCard(), card);
    }

    @Test
    public void testAccountClosure() throws Exception {
        sqlHandling.closeAccount(account.getCreditCard());
        assertFalse(sqlHandling.cardNmbTaken(account.getCreditCard()));
    }

    @ParameterizedTest
    @CsvSource({"100, 100", "0, 0"})
    public void testBalance(int amount, int expected1) throws Exception {
        int expected = sqlHandling.getBalance(account.getCreditCard()) - amount;
        Account account1 = bank.createAccount();
        sqlHandling.doTransfer(account.getCreditCard(), account1.getCreditCard(), amount);
        assertEquals(expected1, sqlHandling.getBalance(account1.getCreditCard()));
        assertEquals(expected, sqlHandling.getBalance(account.getCreditCard()));
    }
}
