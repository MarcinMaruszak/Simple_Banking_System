package banking.domain;

public class Account {
    private String creditCard;
    private String pin;
    private double balance;

    public Account(String creditCard, String pin) {
        this(creditCard,pin,0);
    }

    public Account(String creditCard, String pin, double balance) {
        this.creditCard = creditCard;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
