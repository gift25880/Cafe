
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
        Customer[] queueList = cafe.listQueues();
        for (int i = 0; i < queueList.length; i++) {
            System.out.println(queueList[i]);
        }
    }

    void listTables(Cafe cafe) {
        System.out.println("Table List: ");
        Customer[] tableList = cafe.listTables();
        for (int i = 0; i <= tableList.length; i++) {
            if (tableList[i] == null) {
                System.out.println("Table " + (i + 1) + ": Available");
            } else {
                System.out.println("Table " + (i + 1) + ": " + tableList[i]);
            }
        }
    }

    void listMenu(Cafe cafe) {
        Item[][] menuList = cafe.getMenu();
        System.out.println("Menu: ");
        System.out.println("----------------------------");
        System.out.println("Bakery:");
        for (Item item : menuList[0]) {
                System.out.println(item);
        }
        System.out.println("----------------------------");
        System.out.println("Dessert:");
        for (Item item : menuList[1]) {
                System.out.println(item);
        }
        System.out.println("----------------------------");
        System.out.println("Beverage:");
        for (Item item : menuList[2]) {
                System.out.println(item);
        }
        System.out.println("----------------------------");
    }

    void serve(Cafe cafe) {
        if (cafe.serve()) {
            System.out.println("The order is successfully served.");
        } else {
            System.out.println("The order is not yet ready to be served.");
        }
    }

    void addMenu(Cafe cafe) {
        Item[][] menuList = cafe.getMenu();
        String[] bakeryCode = new String[menuList[0].length];
        String[] dessertCode = new String[menuList[1].length];
        String[] beverageCode = new String[menuList[2].length];
        Type menuType = null;
        String menuName;
        double menuPrice;
        int amountInStock;
        int choice;
        
        for (int i = 0; i < menuList[0].length; i++) {
            bakeryCode[i] = menuList[0][i].getId();
        }
        for (int i = 0; i < menuList[1].length; i++) {
            dessertCode[i] = menuList[1][i].getId();
        }
        for (int i = 0; i < menuList[2].length; i++) {
            beverageCode[i] = menuList[2][i].getId();
        }
        
        do {
            System.out.println("What is the menu type?");
            System.out.println("1. Bakery");
            System.out.println("2. Dessert");
            System.out.println("3. Beverage");
            System.out.print("Enter choice number: ");
            choice = sc.nextInt();
            switch(choice) {
                case 1:
                    menuType = Type.BAKERY;
                    break;
                case 2:
                    menuType = Type.DESSERT;
                    break;
                case 3:
                    menuType = Type.BEVERAGE;
                    break;
                default:
                    System.out.println("Invalid choice, please enter 1-3 only");
            }
        } while (choice != 1 || choice != 2 || choice != 3);
        
        do {
            System.out.print("Enter menu's name");
            menuName = sc.nextLine();
            if (menuName == null || menuName.equals("")) {
                System.out.println("Menu's name can't be blank.");
            }
            break;
        } while(true);
        
        do {
            System.out.print("Enter menu's price: ");
            menuPrice = sc.nextDouble();
            if (menuPrice < 0) {
                System.out.println("The price must not less than 0.");
            }
            break;
        } while (true);
        
        do {
            System.out.print("Enter amount in stock: ");
            amountInStock = sc.nextInt();
            if (amountInStock < 0) {
                System.out.println("Amount of the menu can't be less than 0.");
            }
            break;
        } while (true);
        
        //Generating menuCode
        String[] numberListInString;
        int[] numberListInInt;
        int numberFound = 0;
        String codeNo;
        String menuCode = null;
        
        if (menuType == Type.BAKERY) {
            numberListInString = new String[bakeryCode.length];
            numberListInInt = new int[numberListInString.length];
            for (int i = 0; i < bakeryCode.length; i++) {
                numberListInString[i] = bakeryCode[i].replace("BK", "").trim();
                numberListInInt[i] = Integer.parseInt(numberListInString[i]);
            }
            for (int i = 1; i <= numberListInInt.length; i++) {
                if (i == numberListInInt[i-1]) {
                    continue;
                } else {
                    numberFound = i;
                    break;
                }
            }
            codeNo = Integer.toString(numberFound);
            if (numberFound < 10) {
                menuCode = "BK0"+codeNo;
            } else {
                menuCode = "BK"+codeNo;
            }
        }
        if (menuType == Type.DESSERT) {
            numberListInString = new String[dessertCode.length];
            numberListInInt = new int[numberListInString.length];
            for (int i = 0; i < dessertCode.length; i++) {
                numberListInString[i] = dessertCode[i].replace("DS", "").trim();
                numberListInInt[i] = Integer.parseInt(numberListInString[i]);
            }
            for (int i = 1; i <= numberListInInt.length; i++) {
                if (i == numberListInInt[i-1]) {
                    continue;
                } else {
                    numberFound = i;
                    break;
                }
            }
            codeNo = Integer.toString(numberFound);
            if (numberFound < 10) {
                menuCode = "DS0"+codeNo;
            } else {
                menuCode = "DS"+codeNo;
            }
        }
        if (menuType == Type.BEVERAGE) {
            numberListInString = new String[beverageCode.length];
            numberListInInt = new int[numberListInString.length];
            for (int i = 0; i < beverageCode.length; i++) {
                numberListInString[i] = beverageCode[i].replace("BV", "").trim();
                numberListInInt[i] = Integer.parseInt(numberListInString[i]);
            }
            for (int i = 1; i <= numberListInInt.length; i++) {
                if (i == numberListInInt[i-1]) {
                    continue;
                } else {
                    numberFound = i;
                    break;
                }
            }
            codeNo = Integer.toString(numberFound);
            if (numberFound < 10) {
                menuCode = "BV0"+codeNo;
            } else {
                menuCode = "BV"+codeNo;
            }
        }
        
        try {
            cafe.addMenu(new Item(menuCode, menuName, menuPrice, amountInStock), menuType);
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }

    void removeMenu(Cafe cafe) {

    }

    void checkStock(Cafe cafe) {

    }

    void addStock() {

    }

}
