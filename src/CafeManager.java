
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class CafeManager {

    private Cafe cafe;
    private StaffAccount staff;
    private Scanner sc = new Scanner(System.in);

    public CafeManager(Cafe cafe) {
        Objects.requireNonNull(cafe, "Cafe can't be blank.");
        this.cafe = cafe;
    }

    public int optionMenu() {
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
        System.out.println("10. Add Menu");
        System.out.println("11. Remove Menu");
        System.out.println("12. Check Stock");
        System.out.println("13. Restock");
        System.out.println("14. Reset Password");
        System.out.println("15. Logout");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        return choice;
    }

    public boolean login() {
        String inputId = null, inputPass = null;
        do {
            System.out.print("Enter Id: ");
            inputId = sc.nextLine();
            if (inputId.equals("") || inputId == null) {
                System.out.println("Id must be filled.");
                continue;
            }
            break;
        } while (true);

        System.out.println("\n");

        do {
            System.out.println("Enter Password: ");
            inputPass = sc.nextLine();
            if (inputPass.equals("") || inputPass == null) {
                System.out.println("Password must be filled.");
                continue;
            }
            break;
        } while (true);

        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM staff WHERE id='" + inputId + "' AND password='" + inputPass + "';");
            Position staffPosition = Position.valueOf(rs.getString("position").toUpperCase());

            if (rs.next()) {
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
        this.staff = null;
        System.exit(0);
    }

    public void resetPass() {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM staff WHERE id='" + staff.getId() + "' AND password='" + staff.getPassword() + "';");

            if (rs.next()) {
                String newPass = null, pw = null;
                do {
                    System.out.print("Enter your new password: ");
                    newPass = sc.nextLine();
                    if (newPass.equals("") && newPass == null) {
                        System.out.println("New password must be filled.");
                        continue;
                    } else if (newPass.equals(rs.getString("password"))) {
                        System.out.println("New password must not be the same as current password.");
                        continue;
                    } else {
                        do {
                            System.out.print("Enter your previous password to confirm (or 'quit' to exit): ");
                            pw = sc.nextLine();
                            if (!(pw.equals(rs.getString("password")))) {
                                System.out.println("Your password is incorrect");
                                continue;
                            } else if (pw == null || pw.equals("")) {
                                System.out.println("This field must be filled.");
                                continue;
                            } else if (pw.equals("quit")) {
                                break;
                            } else {
                                rs.updateString("password", newPass);
                                System.out.println("Your password has been successfully changed!");
                                break;
                            }
                        } while (true);
                    }
                } while (!pw.equals("quit"));
            }

        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }
}
