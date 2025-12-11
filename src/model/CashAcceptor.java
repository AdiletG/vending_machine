package model;

public class CashAcceptor implements PaymentAcceptor {
    private int cash;

    public CashAcceptor(int cash) {
        this.cash = cash;
    }

    @Override
    public int getBalance() {
        return cash;
    }

    @Override
    public void deposit(int amount) {
        this.cash += amount;
    }

    @Override
    public void withdraw(int amount) {
        this.cash -= amount;
    }
}
