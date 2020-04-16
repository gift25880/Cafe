
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class CafeManager {
    private Cafe cafe;
    private StaffAccount staff;
    private Scanner sc = new Scanner(System.in);
    
    public int optionMenu() {
        sc.reset();
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
        String inputId;
        String inputPass;
        
        do {
            System.out.print("Enter Id: ");
            inputId = sc.nextLine();
            if (inputId.equals("") && inputId == null) {
                System.out.println("Id must be filled.");
            }
        } while (!(inputId.equals("")) && inputId != null);
        
        System.out.println("\n");
        
        do {
            System.out.println("Enter Password");
            inputPass = sc.nextLine();
            if (inputPass.equals("") && inputPass == null) {
                System.out.println("Password must be filled.");
            }
        } while (!(inputPass.equals("")) && inputPass != null);
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");
                Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM staff WHERE id='"+inputId+"' AND password='"+inputPass+"';");
                Position staffPosition = Position.valueOf(rs.getString("position").toUpperCase());
                
                if (inputId.equals(rs.getString("id")) && inputPass.equals(rs.getString("password"))) {
                    System.out.println("Login Success!");
                    staff = new StaffAccount(rs.getString("id"), new Person(rs.getString("name"), rs.getString("phone")), staffPosition, rs.getString("password"));
                    
                    return true;
                } else {
                    System.out.println("Id or password is not matched");
                    return false;
                }
                
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
        return false;
    }
    
    public void logout() {
        
    }
    
    public void resetPass() {
        
    }
}
