package banking.domain;

import java.util.Objects;
import java.util.Random;

public class CreditCard {
    private String cardNumber;
    private int pin;



    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return cardNumber.equals(that.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber);
    }
}
