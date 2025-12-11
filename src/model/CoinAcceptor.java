package model;

public class CoinAcceptor implements PaymentAcceptor {
    private int amount;

    public CoinAcceptor(int amount) {
        this.amount = amount;
    }

    @Override
    public int getBalance() {
        return amount;
    }

    @Override
    public void deposit(int amount) {
        this.amount += amount;
    }

    @Override
    public void withdraw(int amount) {
        this.amount -= amount;
    }
}
