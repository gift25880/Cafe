
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class StaffServiceManager {
    private Scanner sc = new Scanner(System.in);
    
    void addCustomer(Cafe cafe) {
        int choice = 0;
        int queue;
        do {
            System.out.println("Do you want to eat here or take home?");
            System.out.println("1. Eat Here");
            System.out.println("2. Take Home");
            System.out.print("Enter the choice number: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1: 
                    queue = cafe.addCustomer(false);
                    if (queue == -1) {
                        System.out.println("Sorry, there is no table available at the moment.");
                    } else {
                        System.out.println("Your queue number is "+queue);
                    }
                    break;
                case 2:
                    queue = cafe.addCustomer(true);
                    System.out.println("Your queue number is "+queue);
                    break;
                default:
                    System.out.println("Invalid choice, please enter 1 or 2 only.");
            }
        } while (choice != 1 && choice != 2);
    }
    
    void subscribe(Cafe cafe) {
        String memberName;
        String phone;
        String username;
        do {
            System.out.print("Enter your name: ");
            memberName = sc.nextLine();
            if (memberName.equals("") || memberName == null) {
                System.out.println("Your name can't be blank.");
            }
        } while (memberName.equals("") || memberName == null);
        
        System.out.print("Enter your phone number (optional): ");
        phone = sc.nextLine();
        if (phone.equals("")) {
            phone = null;
        }
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id FROM member");
            String[] memberIdInDb = new String[1000000000];
            int i = 0;
            int count = 0;
            boolean available = true;
            while(rs.next()) {
                memberIdInDb[i++] = rs.getString("id");
                count++;
            }
            do {
                System.out.print("Enter your username: ");
                username = sc.nextLine();
                if (username.equals("") || username == null) {
                    System.out.println("Your username can't be blank.");
                }
                for (int j = 0; j < count; j++) {
                    if (username.equals(memberIdInDb[j])) {
                        System.out.println("Username Unavilable.");
                        available = false;
                    }
                }
            } while (username.equals("") || username == null || available == false);
            
            Account newAcc = new Account(username, new Person(memberName, phone));
            cafe.addMember(newAcc);
            System.out.println("Welcome "+username+"! You are now a member of this cafe!");
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
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
