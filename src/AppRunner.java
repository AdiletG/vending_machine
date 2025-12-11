import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;
    private final CashAcceptor cashAcceptor;

    private static boolean isExit = false;
    private int payment;
    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
        cashAcceptor = new CashAcceptor(100);
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

        changePayment(changePaymentMenu());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private int changePaymentMenu(){
        print("Выберите способ оплаты");
        print(" 1 - Оплата монетами\n" +
                " 2 - Оплата наличными(купюрами)");
        while (true){
            try {
                payment = new Scanner(System.in).nextInt();

                if(payment < 0 || payment > 2){
                    System.out.println("Просьба выбрать способ оплаты!");
                    continue;
                }
                return payment;
            }catch (InputMismatchException n){
                n.getMessage();
                System.out.println("Просьба выбрать способ оплаты!");
            }
        }
    }

    private void changePayment(int payment){
        switch (payment){
            case 1:
                print("Монет на сумму: " + coinAcceptor.getAmount());
                break;
            case 2:
            default:
                print("Купюр на сумму: " + cashAcceptor.getCash());
                break;
        }
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
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
                    if(payment == 1){
                        coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    }else {
                        cashAcceptor.setCash(cashAcceptor.getCash() - products.get(i).getPrice());
                    }
                    payment = 0;
                    print("Вы купили " + products.get(i).getName());
                    break;
                } else if ("h".equalsIgnoreCase(action)) {
                    isExit = true;
                    break;
                } else if ("a".equalsIgnoreCase(action)) {
                    if(payment == 1){
                        coinReplenishment();
                    }else {
                        cashReplenishment();
                    }

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

    private void coinReplenishment(){
        print("Просьба ввести сумму монет которую хотите пополнить: ");

        while (true){
            try {
                int actionSum = new Scanner(System.in).nextInt();

                if(actionSum < 0){
                    System.out.println("Ошибка сумма не может быть меньше чем 0!");
                    continue;
                }
                coinAcceptor.setAmount(coinAcceptor.getAmount() + actionSum);
                break;
            }catch (InputMismatchException n){
                n.getMessage();
                System.out.println("Просьба ввести сумму: ");
            }
        }

    }

    private void cashReplenishment(){
        print("Просьба ввести сумму монет которую хотите пополнить: ");

        while (true){
            try {
                int actionSum = new Scanner(System.in).nextInt();

                if(actionSum < 0){
                    System.out.println("Ошибка сумма не может быть меньше чем 0!");
                    continue;
                }
                cashAcceptor.setCash(cashAcceptor.getCash() + actionSum);
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
