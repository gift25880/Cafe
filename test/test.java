
import cashier.Cashier;
import service.Cafe;



public class test {
    public static void main(String[] args) {
        Cafe cafe = new Cafe("All Cafe", 50);
        Cashier cashier = new Cashier(cafe);
        cashier.optionMenu();
    }
}
