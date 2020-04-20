
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class StaffServiceManager {

    private static Scanner sc = new Scanner(System.in);

    static void addCustomer(Cafe cafe) {
        int choice = 0;
        int queue;
        do {
            System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Do you want to eat here or take home?");
            System.out.println("1. Eat Here");
            System.out.println("2. Take Home");
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter the choice number: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    queue = cafe.addCustomer(false);
                    if (queue == -1) {
                        System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Sorry, there is no table available at the moment.");
                    } else {
                        System.out.println("Your queue number is " + queue);
                    }
                    break;
                case 2:
                    queue = cafe.addCustomer(true);
                    System.out.println("Your queue number is " + queue);
                    break;
                default:
                    System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid choice, please enter 1 or 2 only.");
            }
        } while (choice != 1 && choice != 2);
    }

    static void subscribe(Cafe cafe) {
        String memberName, phone, username;
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your name: ");
            memberName = sc.nextLine();
            if (memberName.equals("") || memberName == null) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Your name can't be blank.");
                continue;
            }
            break;
        } while (true);

        System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your phone number (optional): ");
        phone = sc.nextLine();
        if (phone.equals("")) {
            phone = null;
        }

        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your username: ");
            username = sc.nextLine();
            if (username.equals("") || username == null) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Your username can't be blank.");
                continue;
            }
            break;
        } while (true);

        try {
            Account newAcc = new Account(username, new Person(memberName, phone));
            if (cafe.addMember(newAcc)) {
                System.out.println("Welcome " + ColorCoder.getAnsiEscapeCode("green") + username + ColorCoder.getAnsiEscapeCode("reset") + "! You are now a member of this cafe!");
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Username is already taken.");
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }

    static void listOrders(Cafe cafe) {
        System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter your queue number: ");
        int queueNo = sc.nextInt();
        MenuItem[] orderInQueue = cafe.listOrders(queueNo);
        if (orderInQueue == null) {
            System.out.println(ColorCoder.getAnsiEscapeCode("red") + "You didn't order anything yet.");
        } else {
            System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Your Order:");
            for (int i = 0; i < orderInQueue.length; i++) {
                System.out.println(orderInQueue[i]);
            }
        }
    }

    static void listQueues(Cafe cafe) {
        System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Queue List:");
        Customer[] queueList = cafe.listQueues();
        for (int i = 0; i < queueList.length; i++) {
            System.out.println(queueList[i]);
        }
    }

    static void listTables(Cafe cafe) {
        System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Table List: ");
        Customer[] tableList = cafe.listTables();
        for (int i = 0; i <= tableList.length; i++) {
            if (tableList[i] == null) {
                System.out.println(ColorCoder.getAnsiEscapeCode("green") + "Table " + (i + 1) + ": Available");
            } else {
                System.out.println("Table " + (i + 1) + ": " + tableList[i]);
            }
        }
    }

    static void listMenu(Cafe cafe) {
        Item[][] menuList = cafe.getMenu();
        do {
            System.out.println(ColorCoder.getAnsiEscapeCode("cyan") + "What is the type of the menu you want to look at?");
            System.out.println("1. Bakery");
            System.out.println("2. Dessert");
            System.out.println("3. Beverage");
            System.out.println("0. Exit");
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter choice number: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Bakery :");
                    System.out.println("-----------------------------------------------");
                    for (Item item : menuList[0]) {
                        System.out.printf("%4s: %-35s%10.2f Baht", item.getId(), item.getName(), item.getPrice());
                    }
                    break;
                case 2:
                    System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Dessert :");
                    System.out.println("-----------------------------------------------");
                    for (Item item : menuList[1]) {
                        System.out.printf("%4s: %-35s%10.2f Baht", item.getId(), item.getName(), item.getPrice());
                    }
                    break;

                case 3:
                    System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Beverage :");
                    System.out.println("-----------------------------------------------");
                    for (Item item : menuList[2]) {
                        System.out.printf("%4s: %-35s%10.2f Baht", item.getId(), item.getName(), item.getPrice());
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice, please enter 0-3 only");
            }
        } while (true);
    }

    static void checkStock(Cafe cafe) {
        Item[][] menuList = cafe.getMenu();
        int page = 1;
        System.out.println(ColorCoder.getAnsiEscapeCode("cyan") + "Page #" + page);
        System.out.println("-------------------------------------------------------------");
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Bakery: ");
                case 1:
                    System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Dessert: ");
                case 2:
                    System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "Beverage: ");
            }
            for (Item item : menuList[i]) {
                sc.reset();
                if (item != null) {
                    System.out.println((item.getStock() == 0 ? ColorCoder.getAnsiEscapeCode("red") : null) + item);
                }
                if ((i + 1) % 10 == 0) {
                    page++;
                    System.out.println("------------------------------------------------");
                    System.out.print("Press enter see next page, type 'stop' and press enter  to cancel... ");
                    String pressKey = sc.nextLine();
                    if ("stop".equalsIgnoreCase(pressKey)) {
                        return;
                    }
                    if (menuList[i].length - (i + 1) >= 10) {
                        System.out.println("\n");
                        System.out.println(ColorCoder.getAnsiEscapeCode("cyan") + "Page #" + page);
                        System.out.println("-------------------------------------------------------------");
                    } else {
                        System.out.println("\n");
                    }
                }
            }
        }
    }

    static void serve(Cafe cafe) {
        if (cafe.serve()) {
            System.out.println(ColorCoder.getAnsiEscapeCode("green") + "The order is successfully served.");
        } else {
            System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Their is currently no queue at the moment.");
        }
    }

    static void addMenu(Cafe cafe) {
        Item[][] menuList = cafe.getMenu();
        String code[] = null;
        String codeHead = null;
        Type menuType = null;
        String menuName;
        double menuPrice;
        int amountInStock;
        int choice;

        do {
            System.out.println(ColorCoder.getAnsiEscapeCode("yellow") + "What is the menu type?");
            System.out.println("1. Bakery");
            System.out.println("2. Dessert");
            System.out.println("3. Beverage");
            System.out.println("0. Exit");
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter choice number: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    menuType = Type.BAKERY;
                    codeHead = "BK";
                    code = new String[menuList[0].length];
                    for (int i = 0; i < menuList[0].length; i++) {
                        code[i] = menuList[0][i].getId();
                    }
                    break;
                case 2:
                    menuType = Type.DESSERT;
                    codeHead = "DS";
                    code = new String[menuList[1].length];
                    for (int i = 0; i < menuList[1].length; i++) {
                        code[i] = menuList[1][i].getId();
                    }
                    break;
                case 3:
                    menuType = Type.BEVERAGE;
                    codeHead = "BV";
                    code = new String[menuList[2].length];
                    for (int i = 0; i < menuList[2].length; i++) {
                        code[i] = menuList[2][i].getId();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Invalid choice, please enter 0-3 only");
            }
        } while (choice != 1 && choice != 2 && choice != 3 && choice != 0);

        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter menu's name: ");
            menuName = sc.nextLine();
            if (menuName == null || menuName.equals("")) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Menu's name can't be blank.");
            }
            break;
        } while (true);

        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter menu's price: ");
            menuPrice = sc.nextDouble();
            if (menuPrice < 0) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "The price must be not less than or equal to 0.");
            }
            break;
        } while (true);

        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter amount in stock: ");
            amountInStock = sc.nextInt();
            if (amountInStock < 0) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Amount of the menu can't be less than or equal to 0.");
            }
            break;
        } while (true);

        //Generating menuCode
        int numberFound = 0;
        String[] numberListInString = new String[code.length];
        int[] numberListInInt = new int[numberListInString.length];
        for (int i = 0; i < code.length; i++) {
            numberListInString[i] = code[i].replace(codeHead, "").trim();
            numberListInInt[i] = Integer.parseInt(numberListInString[i]);
        }
        for (int i = 1; i <= 100; i++) {
            if (i != numberListInInt[i - 1]) {
                numberFound = i;
                break;
            }
        }
        String menuCode = (numberFound < 10) ? codeHead + "0" + Integer.toString(numberFound) : codeHead + Integer.toString(numberFound);

        try {
            if (cafe.addMenu(new Item(menuCode, menuName, menuPrice, amountInStock), menuType)) {
                System.out.println("The menu [" + menuCode + ": " + menuName + "] has been added successfully.");
            }
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }

    static void removeMenu(Cafe cafe) {
        String menuCode;
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter the menu code (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "This field can't be blank.");
            } else if (menuCode.equals("quit")) {
                break;
            } else {
                try {
                    if (cafe.removeMenu(menuCode.toUpperCase())) {
                        System.out.println(ColorCoder.getAnsiEscapeCode("green") + "The menu " + menuCode + " has been removed successfully!");
                        break;
                    } else {
                        System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Menu not found.");
                    }
                } catch (SQLException ex) {
                    System.out.println("An SQL Exception has occured: " + ex.getMessage());
                }
            }
        } while (true);
    }

    static void restock(Cafe cafe) {
        String menuCode = null;
        int amount;
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter the menuCode you want to restock (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "This field can't be blank.");
            } else if (menuCode.equalsIgnoreCase("quit")) {
                return;
            }
            break;
        } while (true);
        do {
            System.out.print(ColorCoder.getAnsiEscapeCode("cyan") + "Enter the amount you want to restock: ");
            amount = sc.nextInt();
            if (amount < 0) {
                System.out.println(ColorCoder.getAnsiEscapeCode("red") + "Amount can't be less than 0");
                continue;
            }
            break;
        } while (true);
        try {
            if (cafe.restock(menuCode.toUpperCase(), amount)) {
                System.out.println(ColorCoder.getAnsiEscapeCode("cyan") + "The menu " + menuCode + " has been added by " + amount + ".");
            }
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }

}
