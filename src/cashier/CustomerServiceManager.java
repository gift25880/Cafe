//62130500048 ปฏิญญา ทองอ่วม Pathinya Thonguam
package cashier;

import service.TextFormatter;
import service.Cafe;
import account.MemberAccount;
import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import policy.PointPolicy;

public class CustomerServiceManager {

    private static Scanner sc = new Scanner(System.in);

    static void addItem(Cafe cafe) {
        String menuCode;
        int queueNumber, amount;
        do {
            try {
                System.out.print(TextFormatter.CYAN + "Enter your queue number: ");
                queueNumber = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            if (queueNumber <= 0) {
                System.out.println("\n" + TextFormatter.RED + "Invalid number, please try again.\n");
                continue;
            }
            break;
        } while (true);
        sc.nextLine();
        do {
            System.out.print(TextFormatter.CYAN + "Enter code of the menu you want to add (or type 'quit' to exit): ");
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
                System.out.print(TextFormatter.CYAN + "How much do you want to add: ");
                amount = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            if (amount <= 0) {
                System.out.println("\n" + TextFormatter.RED + "Invalid amount, please try again.\n");
                continue;
            }
            break;
        } while (true);
        if (cafe.addItem(menuCode.toUpperCase(), queueNumber, amount)) {
            System.out.println("\n" + TextFormatter.GREEN + "The menu item has been added to your order list successfully!");
        } else {
            System.out.println("\n" + TextFormatter.RED + "Something went wrong when adding item to your order list or the menu does not exist, please try again.");
        }
        sc.nextLine();
        System.out.println("\n----------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void removeItem(Cafe cafe) {
        String menuCode;
        int queueNumber, amount;
        do {
            try {
                System.out.print(TextFormatter.CYAN + "Enter your queue number: ");
                queueNumber = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            if (queueNumber <= 0) {
                System.out.println("\n" + TextFormatter.RED + "Invalid number, please try again.\n");
                continue;
            }
            break;
        } while (true);
        sc.nextLine();
        do {
            System.out.print(TextFormatter.CYAN + "Enter code if the menu you want to remove (or type 'quit' to exit): ");
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
                System.out.print(TextFormatter.CYAN + "How much do you want to remove: ");
                amount = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            if (amount <= 0) {
                System.out.println("\n" + TextFormatter.RED + "Invalid amount, please try again.\n");
                continue;
            }
            break;
        } while (true);
        if (cafe.removeItem(menuCode.toUpperCase(), queueNumber, amount)) {
            System.out.println("\n" + TextFormatter.GREEN + "The menu item has been removed from your order list successfully!");
        } else {
            System.out.println("\n" + TextFormatter.RED + "Something went wrong when removing the item from your order list or you didn't order this item, please try again.");
        }
        sc.nextLine();
        System.out.println("\n----------------------------");
        System.out.print("Press enter to proceed... ");
        String pressKey = sc.nextLine();
    }

    static void checkOutCafe(Cafe cafe) {
        int queueNumber, queueOrder;
        double intake;
        boolean redeem = false;
        MemberAccount member = null;
        do {
            try {
                System.out.print(TextFormatter.CYAN + "Enter queue number: ");
                queueNumber = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.\n");
                continue;
            }
            queueOrder = cafe.findServedQueue(queueNumber);
            if (queueOrder < 0) {
                System.out.println("\n" + TextFormatter.RED + "Invalid queue number, please try again." + TextFormatter.RESET);
                sc.nextLine();
                System.out.println("\n----------------------------");
                System.out.print("Press enter to proceed... ");
                String pressKey = sc.nextLine();
                return;
            } else {
                break;
            }
        } while (true);
        double total = cafe.getTotalPrice(queueOrder);
        do {
            int choice;
            try {
                System.out.println("\n" + TextFormatter.YELLOW + "Are you a member of this cafe?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print(TextFormatter.CYAN + "Enter the choice number: ");
                choice = sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.");
                continue;
            }
            switch (choice) {
                case 1:
                    do {
                        sc.nextLine();
                        System.out.print("\n" + TextFormatter.CYAN + "Enter your username: ");
                        String username = sc.nextLine();
                        if (username == null || username.equals("")) {
                            System.out.println("\n" + TextFormatter.RED + "This field can't be blank.");
                            continue;
                        }
                        member = cafe.searchForMember(username);
                        if (member == null) {
                            System.out.println("\n" + TextFormatter.RED + "Member not found, please try again.");
                            continue;
                        }
                        break;
                    } while (true);
                    System.out.println("\n" + TextFormatter.RESET + "Account " + TextFormatter.YELLOW + member.getUser() + TextFormatter.RESET + " currrently have " + TextFormatter.GREEN + TextFormatter.BOLD + member.getPoint() + TextFormatter.RESET + " points.");
                    System.out.println("This is worth " + TextFormatter.GREEN + TextFormatter.BOLD + (int) (member.getPoint() / PointPolicy.POINT_TO_ONE_BATH) + TextFormatter.RESET + " baht.");
                    System.out.println("The total price is " + TextFormatter.CYAN + total + TextFormatter.RESET + " baht.");
                    do {
                        try {
                            System.out.println("\n" + TextFormatter.YELLOW + "Do you want to redeem your point?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            System.out.print(TextFormatter.CYAN + "Enter the choice number: ");
                            choice = sc.nextInt();
                        } catch (InputMismatchException ex) {
                            sc.nextLine();
                            System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.");
                            continue;
                        }
                        switch (choice) {
                            case 1:
                                redeem = true;
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("\n" + TextFormatter.RED + "Invalid choice number, please enter 1 or 2 only.");
                                continue;
                        }
                        break;
                    } while (true);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("\n" + TextFormatter.RED + "Invalid choice number, please try again.");
            }
            break;
        } while (true);
        double net = total;
        int[] redeemValue = {member == null ? 0 : member.getPoint(), 0};
        if (redeem) {
            redeemValue = cafe.redeem(total, member);
            net = total - redeemValue[1];
        }
        if (member == null || !redeem) {
            System.out.println("\n" + TextFormatter.RESET + "The net price is " + TextFormatter.CYAN + net + TextFormatter.RESET + " baht.");
        } else {
            System.out.println("\nThe net price is " + TextFormatter.CYAN + net + TextFormatter.RESET + " baht from a total of " + TextFormatter.CYAN + total + TextFormatter.RESET + " baht.");
        }
        do {
            try {
                System.out.print("\n" + TextFormatter.CYAN + "Enter amount of money: ");
                intake = sc.nextDouble();
            } catch (InputMismatchException ex) {
                sc.nextLine();
                System.out.println("\n" + TextFormatter.RED + TextFormatter.BOLD + "The entered data type is incompatible.");
                continue;
            }
            if (intake < net) {
                System.out.println("\n" + TextFormatter.RED + "Invalid amount, please try again.");
                continue;
            }
            break;
        } while (true);
        try {
            double change = cafe.checkOut(total, redeemValue[1], intake, member, queueNumber, redeem, redeemValue[0]);
            if (change < 0) {
                System.out.println(TextFormatter.RED + "\nAn error has occured while checking out");
            } else {
                System.out.println("\n" + TextFormatter.RESET + "Your change is " + TextFormatter.YELLOW + change + TextFormatter.RESET + " baht");
                if (member != null) {
                    System.out.println("The current points for " + TextFormatter.YELLOW + member.getName() + TextFormatter.RESET + " is " + TextFormatter.GREEN + TextFormatter.BOLD + member.getPoint() + TextFormatter.RESET + " points.");
                }
                System.out.println("\nThank you for dining at " + TextFormatter.GREEN + cafe.getCafeName().toUpperCase() + TextFormatter.RESET + ".");
            }
        } catch (IOException ex) {
            System.out.println("\nAn IO Exception has occured: " + ex.getMessage());
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
