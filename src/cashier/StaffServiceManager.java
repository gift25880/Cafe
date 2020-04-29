//62130500048 ปฏิญญา ทองอ่วม Pathinya Thonguam
package cashier;

import item.MenuItem;
import service.TextFormatter;
import service.Cafe;
import customer.Customer;
import item.Item;
import person.Person;
import account.Account;
import item.Type;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StaffServiceManager {

    private static Scanner sc = new Scanner(System.in);

    static void addCustomer(Cafe cafe) {
        int choice = 0;
        int queue;
        do {
            try {
                System.out.println(TextFormatter.YELLOW + "Do you want to eat here or take home?");
                System.out.println("1. Eat Here");
                System.out.println("2. Take Home");
                System.out.print(TextFormatter.CYAN + "Enter the choice number: ");
                choice = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            switch (choice) {
                case 1:
                    queue = cafe.addCustomer(false);
                    if (queue == -1) {
                        System.out.println("\n" + TextFormatter.RED + "Sorry, there is no table available at the moment.");
                    } else {
                        System.out.println("\n" + TextFormatter.RESET + "Your queue number is " + TextFormatter.GREEN + queue + TextFormatter.RESET + ".");
                    }
                    break;
                case 2:
                    queue = cafe.addCustomer(true);
                    System.out.println("\n" + TextFormatter.RESET + "Your queue number is " + TextFormatter.GREEN + queue + TextFormatter.RESET + ".");
                    break;
                default:
                    System.out.println("\n" + TextFormatter.RED + "Invalid choice, please enter 1 or 2 only.\n");
            }
        } while (choice != 1 && choice != 2);
        sc.nextLine();
        System.out.println("\n----------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void subscribe(Cafe cafe) {
        String memberName, phone, username;
        do {
            System.out.print(TextFormatter.CYAN + "Enter your name: ");
            memberName = sc.nextLine();
            if (memberName.equals("") || memberName == null) {
                System.out.println("\n" + TextFormatter.RED + "Your name can't be blank.");
                continue;
            }
            break;
        } while (true);

        System.out.print(TextFormatter.CYAN + "Enter your phone number (optional): ");
        phone = sc.nextLine();
        if (phone.equals("")) {
            phone = null;
        }

        do {
            System.out.print(TextFormatter.CYAN + "Enter your username: ");
            username = sc.nextLine();
            if (username.equals("") || username == null) {
                System.out.println("\n" + TextFormatter.RED + "Your username can't be blank.");
                continue;
            }
            break;
        } while (true);

        try {
            Account newAcc = new Account(username, new Person(memberName, phone));
            if (cafe.addMember(newAcc)) {
                System.out.println("\n" + TextFormatter.RESET + "Welcome " + TextFormatter.GREEN + memberName + TextFormatter.RESET + "! You are now a member of this cafe!");
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("\n" + TextFormatter.RED + "Username is already taken.");
        } catch (SQLException ex) {
            System.out.println("\nAn SQL Exception has occured: " + ex.getMessage());
        } finally {
            System.out.println("\n----------------------------");
            System.out.print("Press enter to proceed... ");
            String pressKey = sc.nextLine();
        }
    }

    static void listOrders(Cafe cafe) {
        int queueNo;
        do {
            try {
                System.out.print(TextFormatter.CYAN + "Enter your queue number: ");
                queueNo = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            if (queueNo <= 0) {
                System.out.println("\n" + TextFormatter.RED + "Invalid number, please try again.\n");
                continue;
            }
            break;
        } while (true);
        MenuItem[][] orderInQueue = cafe.listOrders(queueNo);
        if (orderInQueue == null) {
            System.out.println("\n" + TextFormatter.RED + "This queue number does not exist.");
        } else if (orderInQueue[0].length == 0 && orderInQueue[1].length == 0) {
            System.out.println("\n" + TextFormatter.RED + "You didn't order anything yet.");
        } else {
            System.out.println("\n" + TextFormatter.YELLOW + "Your Order:");
            for (int i = 0; i < 2; i++) {
                switch (i) {
                    case 0:
                        System.out.println("\n" + TextFormatter.YELLOW + TextFormatter.BOLD + "Preparing Orders:");
                        break;
                    case 1:
                        System.out.println("\n" + TextFormatter.YELLOW + TextFormatter.BOLD + "Served Orders: ");
                        break;
                }
                if (orderInQueue[i].length == 0) {
                    System.out.println(TextFormatter.RED + "No order here");
                } else {
                    for (MenuItem order : orderInQueue[i]) {
                        System.out.println(order);
                    }
                }
            }
        }
        sc.nextLine();
        System.out.println("\n----------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void listQueues(Cafe cafe) {
        System.out.println(TextFormatter.YELLOW + TextFormatter.BOLD + "Queue List:");
        Customer[] queueList = cafe.listQueues();
        if (queueList != null && queueList.length > 0) {
            for (int i = 0; i < queueList.length; i++) {
                System.out.println((i + 1) + ". " + queueList[i]);
                System.out.println("Order Status: " + (queueList[i].getOrders()[0].length == 0 ? (queueList[i].getOrders()[1].length == 0 ? TextFormatter.RED + "No Orders Yet" : TextFormatter.GREEN + "Served") : TextFormatter.CYAN + "Preparing"));
            }
        } else {
            System.out.println(TextFormatter.RED + "The queue is empty.");
        }
        System.out.println("\n----------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void listTables(Cafe cafe) {
        System.out.println(TextFormatter.YELLOW + "Table List: " + TextFormatter.RESET);
        Customer[] tableList = cafe.listTables();
        for (int i = 0; i < tableList.length; i++) {
            if (tableList[i] == null) {
                System.out.println(TextFormatter.GREEN + "Table " + (i + 1) + ": Available" + TextFormatter.RESET);
            } else {
                System.out.println(TextFormatter.RESET + "Table " + (i + 1) + ": " + tableList[i]);
                System.out.println("Order Status: " + (tableList[i].getOrders()[0].length == 0 ? (tableList[i].getOrders()[1].length == 0 ? TextFormatter.RED + "No Orders Yet" : TextFormatter.GREEN + "Served") : TextFormatter.CYAN + "Preparing"));
            }
        }
        System.out.println("\n----------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void listMenu(Cafe cafe) {
        Item[][] menuList = cafe.getMenu();
        do {
            int choice;
            try {
                System.out.println(TextFormatter.CYAN + "What is the type of the menu you want to look at?");
                System.out.println("1. Bakery");
                System.out.println("2. Dessert");
                System.out.println("3. Beverage");
                System.out.println("0. Exit");
                System.out.print(TextFormatter.CYAN + "Enter choice number: ");
                choice = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.CYAN + TextFormatter.BOLD + "The entered data type is incompatible.");
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println("\n" + TextFormatter.YELLOW + "Bakery :");
                    break;
                case 2:
                    System.out.println("\n" + TextFormatter.YELLOW + "Dessert :");
                    break;
                case 3:
                    System.out.println("\n" + TextFormatter.YELLOW + "Beverage :");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice, please enter 0-3 only");
                    continue;
            }
            sc.nextLine();
            int index = choice - 1;
            if (menuList[index].length > 0) {
                int page = 1;
                System.out.println("\n" + TextFormatter.CYAN + "Page #" + page);
                System.out.println("-------------------------------------------------------------");
                for (int i = 0; i < menuList[index].length; i++) {
                    System.out.printf("%4s: %-25s%10.2f Baht\n", menuList[index][i].getId(), menuList[index][i].getName(), menuList[index][i].getPrice());
                    if ((i + 1) % 10 == 0 || i == menuList[index].length - 1) {
                        page++;
                        System.out.println("-------------------------------------------------------------");
                        System.out.print("Press enter to see next page, type 'stop' and press enter to cancel... ");
                        String pressKey = sc.nextLine();
                        if ("stop".equalsIgnoreCase(pressKey)) {
                            return;
                        }
                        if (menuList[index].length - (i + 1) > 0) {
                            System.out.println("\n");
                            System.out.println(TextFormatter.CYAN + "Page #" + page);
                            System.out.println("-------------------------------------------------------------");
                        } else {
                            System.out.println("\n");
                        }
                    }
                }
            } else {
                System.out.println("\n" + TextFormatter.RED + "No Menu Available");
                System.out.println("\n----------------------------");
                System.out.print("Press enter to proceed... ");
                String pressKey = sc.nextLine();
            }
            break;
        } while (true);
    }

    static void checkStock(Cafe cafe) {
        Item[][] menuList = cafe.getMenu();
        System.out.println(TextFormatter.YELLOW + "Item Stock: " + TextFormatter.RESET);
        for (int i = 0; i < 3; i++) {
            int page = 1;
            switch (i) {
                case 0:
                    System.out.println("\n" + TextFormatter.YELLOW + "Bakery: ");
                    break;
                case 1:
                    System.out.println("\n" + TextFormatter.YELLOW + "Dessert: ");
                    break;
                case 2:
                    System.out.println("\n" + TextFormatter.YELLOW + "Beverage: ");
                    break;
            }
            System.out.println("\n" + TextFormatter.CYAN + "Page #" + page);
            System.out.println("-------------------------------------------------------------");
            if (menuList[i].length > 0) {
                for (int j = 0; j < menuList[i].length; j++) {
                    if (menuList[i][j] != null) {
                        System.out.println((menuList[i][j].getStock() == 0 ? TextFormatter.RED : "") + menuList[i][j]);
                    }
                    if ((j + 1) % 10 == 0 || j == menuList[i].length - 1) {
                        page++;
                        System.out.println("-------------------------------------------------------------");
                        System.out.print("Press enter to see next page, type 'stop' and press enter to cancel... ");
                        String pressKey = sc.nextLine();
                        if ("stop".equalsIgnoreCase(pressKey)) {
                            return;
                        }
                        if (menuList[i].length - (j + 1) > 0) {
                            System.out.println("\n");
                            System.out.println(TextFormatter.CYAN + "Page #" + page);
                            System.out.println("-------------------------------------------------------------");
                        } else {
                            System.out.println("\n");
                        }
                    }
                }
            } else {
                System.out.println(TextFormatter.RED + "No Menu Available");
                System.out.println("-------------------------------------------------------------");
                System.out.print("Press enter to see next page, type 'stop' and press enter to cancel... ");
                String pressKey = sc.nextLine();
                if ("stop".equalsIgnoreCase(pressKey)) {
                    return;
                }
            }
        }
    }

    static void serve(Cafe cafe) {
        try {
            switch (cafe.serve()) {
                case 1:
                    System.out.println(TextFormatter.GREEN + "The order is successfully served.");
                    break;
                case -1:
                    System.out.println(TextFormatter.RED + "Their is currently no queue at the moment.");
                    break;
                case -2:
                    System.out.println(TextFormatter.RED + "Insufficient item in stock.");
                    break;
            }
            System.out.println("\n----------------------------");
            System.out.print("Press enter to proceed... ");
            String pressKey = sc.nextLine();
        } catch (SQLException ex) {
            System.out.println("\nAn SQL Exception has occured: " + ex.getMessage());
            System.out.println("\n----------------------------");
            System.out.print("Press enter proceed... ");
            String pressKey = sc.nextLine();
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
        int choice = 0;

        do {
            try {
                System.out.println("\n" + TextFormatter.YELLOW + "What is the menu type?");
                System.out.println("1. Bakery");
                System.out.println("2. Dessert");
                System.out.println("3. Beverage");
                System.out.println("0. Exit");
                System.out.print(TextFormatter.CYAN + "Enter choice number: ");
                choice = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
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
                    System.out.println("\n" + TextFormatter.RED + "Invalid choice, please enter 0-3 only.\n");
            }
        } while (choice != 1 && choice != 2 && choice != 3 && choice != 0);
        sc.nextLine();
        do {
            System.out.print("\n" + TextFormatter.CYAN + "Enter menu's name: ");
            menuName = sc.nextLine();
            if (menuName == null || menuName.equals("")) {
                System.out.println("\n" + TextFormatter.RED + "Menu's name can't be blank.\n");
                continue;
            }
            break;
        } while (true);

        do {
            try {
                System.out.print(TextFormatter.CYAN + "Enter menu's price: ");
                menuPrice = sc.nextDouble();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            if (menuPrice <= 0) {
                System.out.println("\n" + TextFormatter.RED + "The price must be not less than or equal to 0.\n");
                continue;
            }
            break;
        } while (true);

        do {
            try {
                System.out.print(TextFormatter.CYAN + "Enter amount in stock: ");
                amountInStock = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            if (amountInStock <= 0) {
                System.out.println("\n" + TextFormatter.RED + "Amount of the menu can't be less than or equal to 0.\n");
                continue;
            }
            break;
        } while (true);

        //Generating menuCode
        int numberFound = 0;
        String[] numberListInString = new String[code.length + 1];
        int[] numberListInInt = new int[numberListInString.length];
        for (int i = 0; i < code.length; i++) {
            numberListInString[i] = code[i].replace(codeHead, "").trim(); //https://stackoverflow.com/questions/8694984/remove-part-of-string-in-java >>> Remove part of a String
            numberListInInt[i] = Integer.parseInt(numberListInString[i]); //https://alvinalexander.com/java/edu/qanda/pjqa00010.shtml >>> Converting String to Integer
        }
        for (int i = 1; i <= 100; i++) {
            if (i != numberListInInt[i - 1]) {
                numberFound = i;
                break;
            }
        }
        String menuCode = (numberFound < 10) ? codeHead + "0" + Integer.toString(numberFound) : codeHead + Integer.toString(numberFound);
        //https://www.geeksforgeeks.org/different-ways-for-integer-to-string-conversions-in-java/ >>> Converting Integer to String

        try {
            if (cafe.addMenu(new Item(menuCode, menuName, menuPrice, amountInStock), menuType)) {
                System.out.println("\n" + TextFormatter.GREEN + "[" + menuCode + ": " + menuName + "] has been added successfully.");
            }
        } catch (SQLException ex) {
            System.out.println("\nAn SQL Exception has occured: " + ex.getMessage());
        } finally {
            sc.nextLine();
            System.out.println("\n----------------------------");
            System.out.print("Press enter to proceed... ");
            String pressKey = sc.nextLine();
        }
    }

    static void removeMenu(Cafe cafe) {
        String menuCode;
        do {
            System.out.print(TextFormatter.CYAN + "Enter the menu code (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println("\n" + TextFormatter.RED + "This field can't be blank.\n");
            } else if (menuCode.equals("quit")) {
                break;
            } else {
                try {
                    if (cafe.removeMenu(menuCode.toUpperCase())) {
                        System.out.println("\n" + TextFormatter.GREEN + "The menu [" + menuCode + "] has been removed successfully!");
                        break;
                    } else {
                        System.out.println("\n" + TextFormatter.RED + "Menu not found.");
                    }
                } catch (SQLException ex) {
                    System.out.println("\nAn SQL Exception has occured: " + ex.getMessage());
                    System.out.println("\n----------------------------");
                    System.out.print("Press enter proceed... ");
                    String pressKey = sc.nextLine();
                }
            }
        } while (true);
        System.out.println("\n----------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void restock(Cafe cafe) {
        String menuCode = null;
        int amount;
        do {
            System.out.print(TextFormatter.CYAN + "Enter the menuCode you want to restock (or type 'quit' to exit): ");
            menuCode = sc.nextLine();
            if (menuCode == null || menuCode.equals("")) {
                System.out.println("\n" + TextFormatter.RED + "This field can't be blank.\n");
                continue;
            } else if (menuCode.equalsIgnoreCase("quit")) {
                return;
            }
            break;
        } while (true);
        do {
            try {
                System.out.print(TextFormatter.CYAN + "Enter the amount you want to restock: ");
                amount = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            if (amount < 0) {
                System.out.println("\n" + TextFormatter.RED + "Amount can't be less than 0\n");
                continue;
            }
            break;
        } while (true);
        try {
            if (cafe.restock(menuCode.toUpperCase(), amount)) {
                System.out.println("\n" + TextFormatter.GREEN + "The menu " + menuCode.toUpperCase() + " has been added by " + amount + ".");
            } else {
                System.out.println("\n" + TextFormatter.RED + "Menu not found.");
            }
        } catch (SQLException ex) {
            System.out.println("\nAn SQL Exception has occured: " + ex.getMessage());
        } finally {
            sc.nextLine();
            System.out.println("\n----------------------------");
            System.out.print("Press enter to proceed... ");
            String pressKey = sc.nextLine();
        }
    }

}
