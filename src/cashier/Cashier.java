package cashier;


import account.Position;
import service.ColorCoder;
import service.Cafe;
import account.StaffAccount;
import person.Person;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Cashier {

    private Cafe cafe;
    private StaffAccount staff;
    private Scanner sc = new Scanner(System.in);

    public Cashier(Cafe cafe) {
        Objects.requireNonNull(cafe, "Cafe can't be blank.");
        this.cafe = cafe;
    }

    public StaffAccount getStaff() {
        return staff;
    }

    public void optionMenu() {
        int choice;
        if (login()) {
            do {
                System.out.println("\n" + ColorCoder.getAnsiEscapeCode("yellow") + "Menu: ");
                System.out.println("----------------------------");
                System.out.println("1. Show Menu");
                System.out.println("2. Add Item");
                System.out.println("3. Remove Item");
                System.out.println("4. Checkout");
                System.out.println("5. Add Customer");
                System.out.println("6. Subscribe");
                System.out.println("7. List Orders");
                System.out.println("8. List Queues");
                System.out.println("9. List Tables");
                System.out.println("10. Serve");
                int i = 11;
                if (staff.getPosition().equals(Position.MANAGER)) {
                    System.out.println(i++ + ". Add Menu");
                    System.out.println(i++ + ". Remove Menu");
                }
                System.out.println(i++ + ". Check Stock");
                System.out.println(i++ + ". Restock");
                System.out.println(i++ + ". Reset Password");
                System.out.println(i + ". Logout");
                System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your choice: ");
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        StaffServiceManager.listMenu(cafe);
                        break;
                    case 2:
                        CustomerServiceManager.addItem(cafe);
                        break;
                    case 3:
                        CustomerServiceManager.removeItem(cafe);
                        break;
                    case 4:
                        CustomerServiceManager.checkOutCafe(cafe);
                        break;
                    case 5:
                        StaffServiceManager.addCustomer(cafe);
                        break;
                    case 6:
                        StaffServiceManager.subscribe(cafe);
                        break;
                    case 7:
                        StaffServiceManager.listOrders(cafe);
                        break;
                    case 8:
                        StaffServiceManager.listQueues(cafe);
                        break;
                    case 9:
                        StaffServiceManager.listTables(cafe);
                        break;
                    case 10:
                        StaffServiceManager.serve(cafe);
                        break;
                    case 11:
                        if (staff.getPosition() == Position.MANAGER) {
                            StaffServiceManager.addMenu(cafe);
                            break;
                        } else {
                            StaffServiceManager.checkStock(cafe);
                            break;
                        }
                    case 12:
                        if (staff.getPosition() == Position.MANAGER) {
                            StaffServiceManager.removeMenu(cafe);
                            break;
                        } else {
                            StaffServiceManager.restock(cafe);
                            break;
                        }
                    case 13:
                        if (staff.getPosition() == Position.MANAGER) {
                            StaffServiceManager.checkStock(cafe);
                            break;
                        } else {
                            resetPass();
                            break;
                        }
                    case 14:
                        if (staff.getPosition() == Position.MANAGER) {
                            StaffServiceManager.restock(cafe);
                            break;
                        } else {
                            logout();
                            break;
                        }
                    case 15:
                        if (staff.getPosition() == Position.MANAGER) {
                            resetPass();
                            break;
                        } else {
                            System.out.println("Invalid choice, please try again.");
                            break;
                        }
                    case 16:
                        if (staff.getPosition() == Position.MANAGER) {
                            logout();
                            break;
                        } else {
                            System.out.println("Invalid choice, please try again.");
                            break;
                        }
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } while (choice != 0);
        } else {
            System.out.println("Cannot login, please try again.");
        }
    }

    public boolean login() {
        String inputUser = null, inputPass = null;
        System.out.println(ColorCoder.getAnsiEscapeCode("cyan") + "<<--LOG IN-->>");
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter Username: ");
            inputUser = sc.nextLine();
            if (inputUser.equals("") || inputUser == null) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Username must be filled.");
                continue;
            }
            break;
        } while (true);

        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter Password: ");
            inputPass = sc.nextLine();
            if (inputPass.equals("") || inputPass == null) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Password must be filled.");
                continue;
            }
            break;
        } while (true);

        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM staff WHERE username='" + inputUser + "' AND password='" + inputPass + "';");
            if (rs.next()) {
                Position staffPosition = Position.valueOf(rs.getString("position").toUpperCase());
                System.out.println(ColorCoder.getAnsiEscapeCode("green") + "Login Success!");
                staff = new StaffAccount(rs.getString("username"), new Person(rs.getString("name"), rs.getString("phone")), staffPosition, rs.getString("password"));
                System.out.println(ColorCoder.getAnsiEscapeCode("reset") + "Welcome, " + ColorCoder.getAnsiEscapeCode("green") +rs.getString("name") + ColorCoder.getAnsiEscapeCode("reset") + "!");
                this.cafe.setManager(this);
                return true;
            } else {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Id or password is not matched");
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM staff WHERE username='" + staff.getUser() + "' AND password='" + staff.getPassword() + "';");

            if (rs.next()) {
                String newPass, pw;
                do {
                    System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your new password: ");
                    newPass = sc.nextLine();
                    if (newPass.equals("") && newPass == null) {
                        System.out.println(ColorCoder.getAnsiEscapeCode("red") + "New password must be filled.");
                    } else if (newPass.equals(rs.getString("password"))) {
                        System.out.println(ColorCoder.getAnsiEscapeCode("red") + "New password must not be the same as current password.");
                    } else {
                        do {
                            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your previous password to confirm (or 'quit' to exit): ");
                            pw = sc.nextLine();
                            if (!(pw.equals(rs.getString("password")))) {
                                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Your password is incorrect");
                            } else if (pw == null || pw.equals("")) {
                                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "This field must be filled.");
                            } else if (pw.equalsIgnoreCase("quit")) {
                                break;
                            } else {
                                rs.updateString("password", newPass);
                                System.out.println(ColorCoder.getAnsiEscapeCode("green") + "Your password has been successfully changed!");
                                break;
                            }
                        } while (true);
                    }
                } while (true);
            }

        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }
}
