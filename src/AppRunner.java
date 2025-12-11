import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor = new CoinAcceptor(100);
    private final CashAcceptor cashAcceptor = new CashAcceptor(100);
    private PaymentAcceptor paymentAcceptor;

    private static boolean isExit = false;
    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        choosePaymentMethod();
        print("Баланс: " + paymentAcceptor.getBalance());
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private void choosePaymentMethod(){
        print("Выберите способ оплаты");
        print(" 1 - Монеты");
        print(" 2 - Купюры");

        while (true){
            try {
                int select = new Scanner(System.in).nextInt();

                if(select == 1){
                    paymentAcceptor = coinAcceptor;
                } else if (select == 2) {
                    paymentAcceptor = cashAcceptor;
                }else {
                    print("Просьба выбрать способ оплаты! 1 или 2");
                    continue;
                }
                return;
            }catch (InputMismatchException n){
                print("Ошибка: введите число 1 или 2");
            }
        }
    }


    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (paymentAcceptor.getBalance() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);

        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    paymentAcceptor.withdraw(products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                } else if ("h".equalsIgnoreCase(action)) {
                    isExit = true;
                    break;
                } else if ("a".equalsIgnoreCase(action)) {
                    refillBalance();
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попрбуйте еще раз.");
            chooseAction(products);
        }


    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }


    private void refillBalance(){
        print("Просьба ввести сумму монет которую хотите пополнить: ");

        while (true){
            try {
                int actionSum = new Scanner(System.in).nextInt();

                if(actionSum < 0){
                    System.out.println("Ошибка сумма не может быть меньше чем 0!");
                    continue;
                }
                paymentAcceptor.deposit(actionSum);
                break;
            }catch (InputMismatchException n){
                n.getMessage();
                System.out.println("Просьба ввести сумму: ");
            }
        }

    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
