
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
                        System.out.println("Your queue number is " + queue);
                    }
                    break;
                case 2:
                    queue = cafe.addCustomer(true);
                    System.out.println("Your queue number is " + queue);
                    break;
                default:
                    System.out.println("Invalid choice, please enter 1 or 2 only.");
            }
        } while (choice != 1 && choice != 2);
    }

    void subscribe(Cafe cafe) {
        String memberName, phone, username;
        do {
            System.out.print("Enter your name: ");
            memberName = sc.nextLine();
            if (memberName.equals("") || memberName == null) {
                System.out.println("Your name can't be blank.");
                continue;
            }
            break;
        } while (true);

        System.out.print("Enter your phone number (optional): ");
        phone = sc.nextLine();
        if (phone.equals("")) {
            phone = null;
        }

        do {
            System.out.print("Enter your username: ");
            username = sc.nextLine();
            if (username.equals("") || username == null) {
                System.out.println("Your username can't be blank.");
                continue;
            }
            break;
        } while (true);

        try {
            Account newAcc = new Account(username, new Person(memberName, phone));
            if (cafe.addMember(newAcc)) {
                System.out.println("Welcome " + username + "! You are now a member of this cafe!");
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("Username is already taken.");
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }

    void listOrders(Cafe cafe) {
        System.out.print("Enter your queue number: ");
        int queueNo = sc.nextInt();
        MenuItem[] orderInQueue = cafe.listOrders(queueNo);
        if (orderInQueue == null) { 
            System.out.println("You didn't order anything yet.");
        } else {
            System.out.println("Your Order:");
            for (int i = 0; i < orderInQueue.length; i++) {
                System.out.println(orderInQueue[i]);
            }
        }
    }

    void listQueues(Cafe cafe) {
        System.out.println("Queue List:");
        cafe.listQueues();
    }

    void listTables(Cafe cafe) {
        System.out.println("Table List: ");
        Customer[] tableList = cafe.listTables();
        for (int i = 0; i <= tableList.length; i++) {
            if (tableList[i] == null) {
                System.out.println("Table "+(i+1)+": Available");
            } else {
                System.out.println("Table "+(i+1)+": "+tableList[i]);
            }
        }
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
