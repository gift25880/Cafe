package cashier;

import service.TextFormatter;
import service.Cafe;
import account.MemberAccount;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import policy.PointPolicy;

public class CustomerServiceManager {

    private static Scanner sc = new Scanner(System.in);

    static void addItem(Cafe cafe) {
        String menuCode;
        int queueNumber, amount;
        do {
            System.out.print(TextFormatter.getCode("cyan") + "Enter your queue number: ");
            queueNumber = sc.nextInt();
            if (queueNumber <= 0) {
                System.out.println("\n" + TextFormatter.getCode("red") + "Invalid number, please try again.\n");
                continue;
            }
            break;
        } while (true);
        sc.nextLine();
        do {
            System.out.print(TextFormatter.getCode("cyan") + "Enter code of the menu you want to add (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println("\n" + TextFormatter.getCode("red") + "This field can't be blank.\n");
                continue;
            } else if (menuCode.equalsIgnoreCase("quit")) {
                return;
            }
            break;
        } while (true);
        do {
            System.out.print(TextFormatter.getCode("cyan") + "How much do you want to add: ");
            amount = sc.nextInt();
            if (amount <= 0) {
                System.out.println("\n" + TextFormatter.getCode("red") + "Invalid amount, please try again.\n");
                continue;
            }
            break;
        } while (true);
        if (cafe.addItem(menuCode.toUpperCase(), queueNumber, amount)) {
            System.out.println("\n" + TextFormatter.getCode("green") + "The menu item has been added to your order list successfully!");
        } else {
            System.out.println("\n" + TextFormatter.getCode("red") + "Something went wrong when adding item to your order list or the menu does not exist, please try again.");
        }
        sc.nextLine();
        System.out.println("\n-------------------------------------------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void removeItem(Cafe cafe) {
        String menuCode;
        int queueNumber, amount;
        do {
            System.out.print(TextFormatter.getCode("cyan") + "Enter your queue number: ");
            queueNumber = sc.nextInt();
            if (queueNumber <= 0) {
                System.out.println("\n" + TextFormatter.getCode("red") + "Invalid number, please try again.\n");
                continue;
            }
            break;
        } while (true);
        sc.nextLine();
        do {
            System.out.print(TextFormatter.getCode("cyan") + "Enter code if the menu you want to remove (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println("\n" + TextFormatter.getCode("red") + "This field can't be blank.\n");
                continue;
            } else if (menuCode.equalsIgnoreCase("quit")) {
                return;
            }
            break;
        } while (true);
        do {
            System.out.print(TextFormatter.getCode("cyan") + "How much do you want to remove: ");
            amount = sc.nextInt();
            if (amount <= 0) {
                System.out.println("\n" + TextFormatter.getCode("red") + "Invalid amount, please try again.\n");
                continue;
            }
            break;
        } while (true);
        if (cafe.removeItem(menuCode.toUpperCase(), queueNumber, amount)) {
            System.out.println("\n" + TextFormatter.getCode("green") + "The menu item has been removed from your order list successfully!");
        } else {
            System.out.println("\n" + TextFormatter.getCode("red") + "Something went wrong when removing the item from your order list or you didn't order this item, please try again.");
        }
        sc.nextLine();
        System.out.println("\n-------------------------------------------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void checkOutCafe(Cafe cafe) {
        int queueNumber, queueOrder;
        double intake;
        boolean redeem = false;
        MemberAccount member = null;
        do {
            System.out.print(TextFormatter.getCode("cyan") + "Enter queue number: ");
            queueNumber = sc.nextInt();
            queueOrder = cafe.findServedQueue(queueNumber);
            if (queueOrder < 0) {
                System.out.println("\n" + TextFormatter.getCode("red") + "Invalid queue number, please try again.\n");
            } else {
                break;
            }
        } while (true);
        do {
            System.out.println("\n" + TextFormatter.getCode("yellow") + "Are you a member of this cafe?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print(TextFormatter.getCode("cyan") + "Enter the choice number: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    do {
                        sc.nextLine();
                        System.out.print(TextFormatter.getCode("cyan") + "Enter your username: ");
                        String username = sc.nextLine();
                        if (username == null || username.equals("")) {
                            System.out.println("\n" + TextFormatter.getCode("red") + "This field can't be blank.");
                            continue;
                        }
                        member = searchingForMember(username);
                        if (member == null) {
                            System.out.println("\n" + TextFormatter.getCode("red") + "Member not found, please try again.");
                            continue;
                        }
                        break;
                    } while (true);
                    System.out.println("\n" + TextFormatter.getCode("reset") + "You currrently have " + TextFormatter.getCode("green") + TextFormatter.getCode("bold") + member.getPoint() + TextFormatter.getCode("reset") + " points.");
                    System.out.println("This is worth " + TextFormatter.getCode("green") + TextFormatter.getCode("bold") + (int) (member.getPoint() / PointPolicy.POINT_TO_ONE_BATH) + TextFormatter.getCode("reset") + " baht.");
                    do {
                        System.out.println("\n" + TextFormatter.getCode("yellow") + "Do you want to redeem your point?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        System.out.print(TextFormatter.getCode("cyan") + "Enter the choice number: ");
                        choice = sc.nextInt();
                        switch (choice) {
                            case 1:
                                redeem = true;
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("\n" + TextFormatter.getCode("red") + "Invalid choice number, please enter 1 or 2 only.");
                        }
                        break;
                    } while (choice != 1 && choice != 2);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("\n" + TextFormatter.getCode("red") + "Invalid choice number, please try again.");
            }
            break;
        } while (true);
        double total = cafe.getTotalPrice(queueOrder);
        double net = total;
        int[] redeemValue = {member == null ? 0 : member.getPoint(), 0};
        if (redeem) {
            redeemValue = cafe.redeem(total, member);
            net = total - redeemValue[1];
        }
        System.out.println("\nThe total price is: " + TextFormatter.getCode("magenta") + net + TextFormatter.getCode("reset") + " Baht.");
        do {
            System.out.print("\n" + TextFormatter.getCode("cyan") + "Enter amount of money: ");
            intake = sc.nextDouble();
            if (intake < net) {
                System.out.println("\n" + TextFormatter.getCode("red") + "Invalid amount, please try again.");
                continue;
            }
            break;
        } while (true);
        try {
            double change = cafe.checkOut(total, redeemValue[1], intake, member, queueNumber, redeem, redeemValue[0]);
            if (change < 0) {
                System.out.println(TextFormatter.getCode("red") + "\nAn error has occured while checking out");
            } else {
                System.out.println("\nYour change is " + TextFormatter.getCode("magenta") + change + TextFormatter.getCode("reset") + " baht");
                System.out.println("\nThank you for dining at " + TextFormatter.getCode("green") + cafe.getCafeName().toUpperCase() + TextFormatter.getCode("reset") + ".");
            }
        } catch (IOException ex) {
            System.out.println("\nAn IO Exception has occured: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("\nAn SQL Exception has occured: " + ex.getMessage());
        } finally {
            sc.nextLine();
            System.out.println("\n-------------------------------------------------------------");
            System.out.print("Press enter to proceed... ");
            String pressKey = sc.nextLine();
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
