
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MemberServiceManager {
    private Scanner sc = new Scanner(System.in);
    private MemberAccount member = null;
    
    void addItem(Cafe cafe) {
        String menuCode;
        int queueNumber;
        do {
            System.out.print("Enter code of the menu you want to add (or type 'quit' to exit): ");
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
            System.out.println("Something went wrong when adding item to your order list or the menu does not exist, please try again.");
        } else {
            System.out.println("The menu item has been added to your order list successfully!");
        }
    }
    void removeItem(Cafe cafe) {
        String menuCode;
        int queueNumber;
        do {
            System.out.print("Enter code if the menu you want to remove (or type 'quit' to exit): ");
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
        
        if (cafe.removeItem(menuCode.toUpperCase(), queueNumber) == null) {
            System.out.println("Something went wrong when removing the item from your order list or you didn't order this item, please try again.");
        } else {
            System.out.println("The menu item has been removed from your order list successfully!");
        }
    }
    void checkOutCafe(Cafe cafe) {
        int queueNumber;
        double amountOfMoney;
        boolean redeem = true;
        boolean isMember = true;
        do {
            System.out.print("Enter queue number: ");
            queueNumber = sc.nextInt();
            if (queueNumber <= 0) {
                System.out.println("Invalid queue number, please try again.");
            }
            break;
        } while (true);
        
        System.out.println("The total price is: "+cafe.getTotalPrice(queueNumber)+" baht");
        do {
            System.out.print("Enter amount of money: ");
            amountOfMoney = sc.nextDouble();
            if (amountOfMoney < cafe.getTotalPrice(queueNumber)) {
                System.out.println("Invalid amount, please try again.");
            }
            break;
        } while (true);
        do {
            System.out.println("Are you a member of this cafe?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Enter the choice number: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    isMember = true;
                    do {
                        System.out.print("Enter your username: ");
                        String username = sc.nextLine();
                        if (username == null || username.equals("")) {
                            System.out.println("This field can't be blank.");
                        }
                        if (searchingForMember(username) == false) {
                            System.out.println("Member not found, please try again.");
                        }
                        break;
                    } while (true);
                    do {
                        System.out.println("Do you want to redeem your point?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        System.out.print("Enter the choice number: ");
                        choice = sc.nextInt();
                        switch (choice) {
                            case 1:
                                redeem = true;
                                
                                break;
                            case 2:
                                redeem = false;
                                break;
                            default:
                                System.out.println("Invalid choice number, please enter 1 or 2 only.");
                        }
                        break;
                    } while (choice != 1 && choice != 2);
                    break;
                case 2:
                    isMember = false;
                    break;
                default:
                    System.out.println("Invalid choice number, please try again.");
            }
            break;
        } while (true);
       try {
           double change;
            if (isMember) {
                if (redeem) {
                    change = cafe.checkOut(amountOfMoney, member, queueNumber, redeem);
                } else {
                    change = cafe.checkOut(amountOfMoney, member, queueNumber, redeem);
                }
            } else {
                change = cafe.checkOut(amountOfMoney, null, queueNumber, redeem);
            }
            System.out.println("Your change is "+change+" baht");
            System.out.println("Thank you for dining at "+cafe.getCafeName()+".");
       } catch (IOException ex) {
            System.out.println("An IO Exception has occured: " + ex.getMessage());
       }
       
    }
    private boolean searchingForMember(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE username='"+username+"';");
            
            if (rs.next()) {
                member = new MemberAccount(rs.getString("username"), rs.getString("name"), rs.getString("phone"), rs.getInt("point"));
                return true;
            } 
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
        return false;
    }
}
