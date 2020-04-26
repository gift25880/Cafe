package cashier;


import service.ColorCoder;
import service.Cafe;
import account.MemberAccount;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CustomerServiceManager {

    private static Scanner sc = new Scanner(System.in);

    static void addItem(Cafe cafe) {
        String menuCode;
        int queueNumber, amount;
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your queue number: ");
            queueNumber = sc.nextInt();
            if (queueNumber <= 0) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid number, please try again.");
                continue;
            }
            break;
        } while (true);
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter code of the menu you want to add (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "This field can't be blank.");
                continue;
            } else if (menuCode.equalsIgnoreCase("quit")) {
                return;
            }
            break;
        } while (true);
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "How much do you want to add: ");
            amount = sc.nextInt();
            if (amount <= 0) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid amount, please try again.");
                continue;
            }
            break;
        } while (true);
        if (cafe.addItem(menuCode.toUpperCase(), queueNumber, amount)) {
            System.out.println(ColorCoder.getAnsiEscapeCode("green") + "The menu item has been added to your order list successfully!");
        } else {
            System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Something went wrong when adding item to your order list or the menu does not exist, please try again.");
        }
    }

    static void removeItem(Cafe cafe) {
        String menuCode;
        int queueNumber, amount;
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your queue number: ");
            queueNumber = sc.nextInt();
            if (queueNumber <= 0) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid number, please try again.");
                continue;
            }
            break;
        } while (true);
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter code if the menu you want to remove (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "This field can't be blank.");
                continue;
            } else if (menuCode.equalsIgnoreCase("quit")) {
                return;
            }
            break;
        } while (true);
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "How much do you want to add: ");
            amount = sc.nextInt();
            if (amount <= 0) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid amount, please try again.");
                continue;
            }
            break;
        } while (true);
        if (cafe.removeItem(menuCode.toUpperCase(), queueNumber, amount)) {
            System.out.println(ColorCoder.getAnsiEscapeCode("green") + "The menu item has been removed from your order list successfully!");
        } else {
            System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Something went wrong when removing the item from your order list or you didn't order this item, please try again.");
        }
    }

    static void checkOutCafe(Cafe cafe) {
        int queueNumber, queueOrder;
        double intake;
        boolean redeem = false;
        MemberAccount member = null;
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter queue number: ");
            queueNumber = sc.nextInt();
            queueOrder = cafe.findServedQueue(queueNumber);
            if (queueOrder < 0) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid queue number, please try again.");
            } else {
                break;
            }
        } while (true);
        do {
            System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Are you a member of this cafe?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter the choice number: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    do {
                        System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your username: ");
                        String username = sc.nextLine();
                        if (username == null || username.equals("")) {
                            System.out.println(ColorCoder.getAnsiEscapeCode("red") + "This field can't be blank.");
                            continue;
                        }
                        if (searchingForMember(username) == null) {
                            System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Member not found, please try again.");
                            continue;
                        }
                        break;
                    } while (true);
                    do {
                        System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Do you want to redeem your point?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter the choice number: ");
                        choice = sc.nextInt();
                        switch (choice) {
                            case 1:
                                redeem = true;
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid choice number, please enter 1 or 2 only.");
                        }
                        break;
                    } while (choice != 1 && choice != 2);
                    break;
                case 2:
                    break;
                default:
                    System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid choice number, please try again.");
            }
            break;
        } while (true);
        double total = cafe.getTotalPrice(queueOrder);
        double net = total;
        int[] redeemValue = {member.getPoint(), 0};
        if (redeem) {
            redeemValue = cafe.redeem(total, member);
            net = total - redeemValue[1];
        }
        System.out.println("The total price is: " + ColorCoder.getAnsiEscapeCode("magenta") + net + ColorCoder.getAnsiEscapeCode("reset") + " Baht.");
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter amount of money: ");
            intake = sc.nextDouble();
            if (intake < net) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid amount, please try again.");
            }
            break;
        } while (true);
        try {
            double change = cafe.checkOut(total, redeemValue[1], intake, member, queueNumber, redeem, redeemValue[0]);
            if(change<0){
                System.out.println("");
            }
            System.out.println("Your change is " + ColorCoder.getAnsiEscapeCode("magenta") + change + ColorCoder.getAnsiEscapeCode("reset") + " baht");
            System.out.println("Thank you for dining at " + ColorCoder.getAnsiEscapeCode("green") + cafe.getCafeName() + ColorCoder.getAnsiEscapeCode("reset") + ".");
        } catch (IOException ex) {
            System.out.println("An IO Exception has occured: " + ex.getMessage());
        } catch(SQLException ex){
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }

    private static MemberAccount searchingForMember(String username) {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE username='" + username + "';");
            if (rs.next()) {
                return new MemberAccount(rs.getString("username"), rs.getString("name"), rs.getString("phone"), rs.getInt("point"));
            }
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
        return null;
    }
}
