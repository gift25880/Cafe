package cashier;

import account.Position;
import service.TextFormatter;
import service.Cafe;
import account.StaffAccount;
import person.Person;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Cashier {

    private Cafe cafe;
    private StaffAccount staff;
    private Scanner sc = new Scanner(System.in);

    public Cashier(Cafe cafe) {
        Objects.requireNonNull(cafe, "\n" + TextFormatter.getCode("red") + "Cafe can't be blank.\n");
        this.cafe = cafe;
    }

    public StaffAccount getStaff() {
        return staff;
    }

    public void optionMenu() {
        int choice;
        while (!login()) {
            System.out.println(TextFormatter.getCode("red") + TextFormatter.getCode("bold") + "              !!! CANNOT LOGIN !!!\n");
            System.out.println(TextFormatter.getCode("red") + TextFormatter.getCode("bold") + ">> If you wish to try again press y and then enter... <<");
            System.out.println(TextFormatter.getCode("red") + TextFormatter.getCode("bold") + "     >> Press only enter to terminate the system <<");
            String pressKey = sc.nextLine();
            if (!pressKey.equalsIgnoreCase("y")) {
                return;
            }
        }
        do {
            try {
                System.out.println("\n" + TextFormatter.getCode("yellow") + TextFormatter.getCode("bold") + "    <<--MENU-->>");
                System.out.println("=======================");
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
                System.out.println("=======================");
                System.out.print(TextFormatter.getCode("cyan") + "Enter your choice: ");
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
                            System.out.println("\n" + TextFormatter.getCode("red") + "Invalid choice, please try again.\n");
                            break;
                        }
                    case 16:
                        if (staff.getPosition() == Position.MANAGER) {
                            logout();
                            break;
                        } else {
                            System.out.println("\n" + TextFormatter.getCode("red") + "Invalid choice, please try again.\n");
                            break;
                        }
                    default:
                        System.out.println("\n" + TextFormatter.getCode("red") + "Invalid choice, please try again.\n");
                        sc.nextLine();
                        System.out.println("\n----------------------------");
                        System.out.print("Press enter to proceed... ");
                        String pressKey = sc.nextLine();
                }
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.getCode("red") + TextFormatter.getCode("bold") + "The entered data type is incompatible.");
                System.out.println("\n----------------------------");
                System.out.print("Press enter to proceed... ");
                String pressKey = sc.nextLine();
            }
        } while (true);
    }

    public boolean login() throws InputMismatchException {
        String inputUser = null, inputPass = null;
        System.out.println("\n" + TextFormatter.getCode("yellow") + TextFormatter.getCode("bold") + "    <<--LOG IN-->>");
        System.out.println(TextFormatter.getCode("reset") + "========================");
        do {
            System.out.print(TextFormatter.getCode("cyan") + "Enter Username: ");
            inputUser = sc.nextLine();
            if (inputUser.equals("") || inputUser == null) {
                System.out.println("\n" + TextFormatter.getCode("red") + "Username must be filled.\n");
                continue;
            }
            break;
        } while (true);

        do {
            System.out.print(TextFormatter.getCode("cyan") + "Enter Password: ");
            inputPass = sc.nextLine();
            if (inputPass.equals("") || inputPass == null) {
                System.out.println("\n" + TextFormatter.getCode("red") + "Password must be filled.\n");
                continue;
            }
            break;
        } while (true);

        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM staff WHERE username='" + inputUser + "' AND password='" + inputPass + "';");
            if (rs.next()) {
                Position staffPosition = Position.valueOf(rs.getString("position").toUpperCase());
                System.out.println("\n" + TextFormatter.getCode("green") + "Login Success!");
                staff = new StaffAccount(rs.getString("username"), new Person(rs.getString("name"), rs.getString("phone")), staffPosition, rs.getString("password"));
                System.out.println("\n" + TextFormatter.getCode("reset") + ">> Welcome, " + TextFormatter.getCode("green") + TextFormatter.getCode("bold") + rs.getString("name") + TextFormatter.getCode("reset") + "! <<");
                this.cafe.setManager(this);
                return true;
            } else {
                System.out.println("\n" + TextFormatter.getCode("red") + "Id or password is not matched.\n");
                return false;
            }

        } catch (SQLException ex) {
            System.out.println("\nAn SQL Exception has occured: " + ex.getMessage() + "\n");
        }
        return false;
    }

    public void logout() {
        System.out.println("\n" + TextFormatter.getCode("red") + "===SUCCESSFULLY LOGGED OUT===\n");
        this.staff = null;
        System.exit(0);
    }

    public void resetPass() throws InputMismatchException {
        sc.nextLine();
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM staff WHERE username='" + staff.getUser() + "' AND password='" + staff.getPassword() + "';");
            if (rs.next()) {
                String newPass, pw;
                do {
                    System.out.print(TextFormatter.getCode("cyan") + "Enter your new password: ");
                    newPass = sc.nextLine();
                    if (newPass.equals("") || newPass == null) {
                        System.out.println("\n" + TextFormatter.getCode("red") + "New password must be filled.\n");
                    } else if (newPass.equals(rs.getString("password"))) {
                        System.out.println("\n" + TextFormatter.getCode("red") + "New password must not be the same as current password.\n");
                    } else {
                        do {
                            System.out.print(TextFormatter.getCode("cyan") + "Enter your previous password to confirm (or 'quit' to exit): ");
                            pw = sc.nextLine();
                            if (pw.equalsIgnoreCase("quit")) {
                                return;
                            } else if (pw == null || pw.equals("")) {
                                System.out.println("\n" + TextFormatter.getCode("red") + "This field must be filled.\n");
                            } else if (!(pw.equals(rs.getString("password")))) {
                                System.out.println("\n" + TextFormatter.getCode("red") + "Your password is incorrect.\n");
                            } else {
                                staff.setPassword(newPass);
                                stmt.execute("UPDATE staff SET password = '" + newPass + "' WHERE username = '" + staff.getUser() + "';");
                                System.out.println("\n" + TextFormatter.getCode("green") + "Your password has been successfully changed!");
                                return;
                            }
                        } while (true);
                    }
                } while (true);
            }

        } catch (SQLException ex) {
            System.out.println("\nAn SQL Exception has occured: " + ex.getMessage() + "\n");
        } finally {
            System.out.println("\n----------------------------");
            System.out.print("Press enter to proceed... ");
            String pressKey = sc.nextLine();
        }
    }
}
