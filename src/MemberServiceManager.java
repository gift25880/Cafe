
import java.util.Scanner;


public class MemberServiceManager {
    private Scanner sc = new Scanner(System.in);
    
    void addItem(Cafe cafe) {
        String menuCode;
        int queueNumber;
        do {
            System.out.print("Enter Menu Code (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println("This field can;t be blank.");
            } else if (menuCode.equals("quit")) {
                return;
            }
            break;
        } while (true);
        do {
            System.out.print("Enter your queue number: ");
            queueNumber = sc.nextInt();
            if (queueNumber <= 0) {
                System.out.println("Invalid number, please try again.");
            }
            break;
        } while (true);
        
        if (cafe.addItem(menuCode.toUpperCase(), queueNumber) == null){
            System.out.println("Something went wrong when adding item to your order list, please try again.");
        } else {
            System.out.println("The menu item has been added to your order list successfully!");
        }
    }
    void removeItem(Cafe cafe) {
        
    }
    void checkOutCafe(Cafe cafe) {
        
    }
}
