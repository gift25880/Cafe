
import java.util.Scanner;


public class CafeManager {
    private Cafe cafe;
    private StaffAccount staff;
    
    public int optionMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Menu: ");
        System.out.println("----------------------------");
        System.out.println("1. Add Item");
        System.out.println("2. Remove Item");
        System.out.println("3. Checkout");
        System.out.println("4. Add Customer");
        System.out.println("5. Subscribe");
        System.out.println("6. List Orders");
        System.out.println("7. List Queues");
        System.out.println("8. List Tables");
        System.out.println("9. Serve");
        System.out.println("10. Clear Tables");
        System.out.println("11. Add Menu");
        System.out.println("12. Remove Menu");
        System.out.println("13. Check Stock");
        System.out.println("14. Add Stock");
        System.out.println("15. Reset Password");
        System.out.println("16. Logout");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        return choice;
    }
    
    public boolean login() {
        
    }
    
    public void logout() {
        
    }
    
    public void resetPass() {
        
    }
}
