package model;

public interface PaymentAcceptor {
    int getBalance();
    void deposit(int amount);
    void withdraw(int amount);
}
