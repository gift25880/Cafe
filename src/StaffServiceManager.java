
import java.util.Scanner;


public class StaffServiceManager {
    private Scanner sc = new Scanner(System.in);
    
    void addCustomer(Cafe cafe) {
        int choice = 0;
        do {
            System.out.println("Do you want to eat here or take home?");
            System.out.println("1. Eat Here");
            System.out.println("2. Take Home");
            System.out.print("Enter the choice number: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1: 
                    cafe.addCustomer(false);
                    break;
                case 2:
                    cafe.addCustomer(true);
                    break;
                default:
                    System.out.println("Invalid choice, please enter 1 or 2 only.");
            }
        } while (choice != 1 && choice != 2);
    }
    
    void subscribe(Cafe cafe) {
        
    }
    
    void listOrders(Cafe cafe) {
        
    }
    
    void listQueues(Cafe cafe) {
        
    }
    
    void listTables(Cafe cafe) {
        
    }
    
    void serve(Cafe cafe) {
        
    }
    
    void clearTables(Cafe Cafe) {
        
    }
    
    void addMenu(Cafe cafe) {
        
    }
    
    void removeMenu(Cafe cafe) {
        
    }
    
    void checkStock(Cafe cafe) {
        
    }
    
    void addStock() {
        
    }
}
