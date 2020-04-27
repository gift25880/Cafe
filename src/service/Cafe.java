package service;

import item.MenuItem;
import account.MemberAccount;
import item.Type;
import customer.Customer;
import cashier.Cashier;
import item.Item;
import account.Account;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Objects;
import policy.PointPolicy;

public class Cafe implements CustomerService, StaffService, PointPolicy {

    private String cafeName;
    private Cashier cashier;
    private Customer tables[];
    private Item menu[][];
    private int count = 0;
    private int lastQueueNumber = 1;
    private LinkedList<Customer> preparingQueue = new LinkedList();
    private LinkedList<Customer> servedQueue = new LinkedList();

    public Cafe(String cafeName, int maxTables) {
        Objects.requireNonNull(cafeName, "The cafe name cannot be blank.");
        this.cafeName = cafeName;
        tables = new Customer[maxTables > 0 ? maxTables : 10];
        fetchMenu();
    }

    public String getCafeName() {
        return this.cafeName;
    }

    public void setManager(Cashier manager) {
        this.cashier = manager;
    }

    private void fetchMenu() {
        menu = new Item[3][];
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = null;
            for (int i = 0; i < 3; i++) {
                int j = 0;
                switch (i) {
                    case 0:
                        rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM menu WHERE type = 'BAKERY';");
                        rs.next();
                        menu[0] = new Item[rs.getInt("rowcount")];
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'BAKERY';");
                        break;
                    case 1:
                        rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM menu WHERE type = 'DESSERT';");
                        rs.next();
                        menu[1] = new Item[rs.getInt("rowcount")];
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'DESSERT';");
                        break;
                    case 2:
                        rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM menu WHERE type = 'BEVERAGE';");
                        rs.next();
                        menu[2] = new Item[rs.getInt("rowcount")];
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'BEVERAGE';");
                        break;
                }
                while (rs.next()) {
                    menu[i][j++] = new Item(rs.getString("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
        }
    }

    public boolean isFull() {
        return this.count >= this.tables.length;
    }

    private int findPreparingQueue(int queueNumber) {
        for (int i = 0; i < preparingQueue.size(); i++) {
            if (preparingQueue.get(i).getQueueNumber() == queueNumber) {
                return i;
            }
        }
        return -1;
    }

    public int findServedQueue(int queueNumber) {
        for (int i = 0; i < servedQueue.size(); i++) {
            if (servedQueue.get(i).getQueueNumber() == queueNumber) {
                return i;
            }
        }
        return -1;
    }

    private Item findMenu(String id) {
        int i;
        if (id.startsWith("BK")) {
            i = 0;
        } else if (id.startsWith("DS")) {
            i = 1;
        } else if (id.startsWith("BV")) {
            i = 2;
        } else {
            return null;
        }
        for (Item item : menu[i]) {
            if (item != null) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public boolean addItem(String id, int queueNumber, int amount) {
        fetchMenu();
        int i = findPreparingQueue(queueNumber);
        Item item = findMenu(id);
        if (item != null) {
            if (i >= 0) {
                if (item.getStock() >= amount) {
                    return preparingQueue.get(i).add(item, amount);
                } else {
                    return false;
                }
            } else {
                i = findServedQueue(queueNumber);
                if (i >= 0) {
                    if (item.getStock() >= amount) {
                        preparingQueue.add(servedQueue.remove(i));
                        return preparingQueue.peekLast().add(item, amount);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean removeItem(String id, int queueNumber, int amount) {
        int i = findPreparingQueue(queueNumber);
        if (i >= 0) {
            return preparingQueue.get(i).remove(findMenu(id), amount);
        }
        return false;
    }

    public double getTotalPrice(int queueOrder) {
        Customer c = servedQueue.get(queueOrder);
        MenuItem[][] mi = c.getOrders();
        double totalprice = 0;
        for (MenuItem[] orders : mi) {
            for (int j = 0; j < orders.length; j++) {
                totalprice += orders[j].getAmount() * orders[j].getItem().getPrice();
            }
        }
        return totalprice;
    }

    @Override
    public double checkOut(double total, int discount, double amount, MemberAccount member, int queueNumber, boolean redeem, int setPoints) throws IOException, SQLException {
        int i = findServedQueue(queueNumber);
        if (!servedQueue.get(i).isTakeHome()) {
            for (int j = 0; j < tables.length; j++) {
                if (tables[j].getQueueNumber() == queueNumber) {
                    tables[j] = null;
                    break;
                }
            }
        }
        if (i >= 0) {
            if (member != null) {
                if (redeem) {
                    member.setPoint(setPoints);
                } else {
                    double tmp = total;
                    int points = (int) (tmp / PointPolicy.BATH_TO_ONE_POINT);
                    member.setPoint(member.getPoint() + points);
                }
                try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE username ='" + member.getUser() + "';");
                    if (rs.next()) {
                        stmt.execute("UPDATE member SET point = " + member.getPoint() + " WHERE username = '" + member.getUser() + "';");
                    } else {
                        return -1;
                    }
                }
            }
            printReceipt(servedQueue.remove(i), total, discount, amount, member);
            return amount - (total - discount);
        } else {
            return i;
        }
    }

    @Override
    public int[] redeem(double total, MemberAccount member) {
        int points = member.getPoint();
        int discount = (int) (points / PointPolicy.POINT_TO_ONE_BATH);
        if (discount > total) {
            discount = (int) total;
        }
        points -= discount * PointPolicy.POINT_TO_ONE_BATH;
        return new int[]{points, discount};
    }

    public void printReceipt(Customer c, double total, double discount, double amount, MemberAccount member) throws IOException {
        File file = new File("receipt/" + LocalDate.now() + "/receipt_queue_" + c.getQueueNumber() + ".txt");
        file.getParentFile().mkdirs();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try ( PrintWriter pw = new PrintWriter(file)) {
            pw.println("**THANK YOU FOR DINING AT " + this.cafeName.toUpperCase() + "**");
            pw.println("Check Out Time: " + LocalDateTime.now().format(format));
            pw.println("Cashier: " + cashier.getStaff().getName());
            pw.println("----------------------------------------");
            pw.println("Queue Number: " + c.getQueueNumber());
            pw.println("Member: " + (member == null ? "-" : member.getName() + " [" + member.getUser() + "]"));
            pw.println("Dining Status: " + (c.isTakeHome() ? "Takehome" : "Eat In"));
            pw.println("----------------------------------------");
            MenuItem[][] mi = c.getOrders();
            int i = 1;
            for (MenuItem menuOrder : mi[1]) {
                pw.println(String.format("%2d", i++) + ". " + menuOrder.getItem().getName() + " [x" + menuOrder.getAmount() + "] Price: " + (menuOrder.getAmount() * menuOrder.getItem().getPrice()));
            }
            pw.println("\n----------------------------------------");
            pw.println(String.format("%26s%8.2f", "Total Price: ", total));
            pw.println(String.format("%26s%8.2f", "Discount: ", discount));
            pw.println(String.format("%26s%8.2f", "Net Price: ", (total - discount)));
            pw.println(String.format("%26s%8.2f", "Cash: ", amount));
            pw.println(String.format("%26s%8.2f", "Change: ", amount - (total - discount)));
            pw.println("----------------------------------------");
        }
    }

    @Override
    public int addCustomer(boolean takeHome) {
        Customer c = new Customer(lastQueueNumber++, takeHome);
        preparingQueue.add(c);
        if (!takeHome) {
            if (!isFull()) {
                for (int i = 0; i < tables.length; i++) {
                    if (tables[i] == null) {
                        tables[i] = c;
                        count++;
                        break;
                    }
                }
            } else {
                return -1;
            }
        }
        return preparingQueue.peekLast().getQueueNumber();
    }

    @Override
    public boolean addMenu(Item item, Type type) throws SQLException {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO menu VALUES ('" + item.getId() + "', '" + item.getName() + "', '" + item.getPrice() + "', '" + item.getStock() + "', '" + type.name() + "');");
            return true;
        } finally {
            fetchMenu();
        }
    }

    @Override
    public boolean removeMenu(String id) throws SQLException {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu WHERE id = '" + id + "';");
            if (rs.next()) {
                stmt.execute("DELETE FROM menu WHERE id = '" + id + "';");
                return true;
            } else {
                return false;
            }
        } finally {
            fetchMenu();
        }
    }

    @Override
    public boolean addMember(Account member) throws SQLException {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO member VALUES ('" + member.getUser() + "', '" + member.getName() + "', '" + member.getPhone() + "', 0);");
            return true;
        }
    }

    @Override
    public int serve() {
        Customer add = preparingQueue.peek();
        if (add == null) {
            return -1;
        } else {
            MenuItem[] serve = add.getOrders()[0];
            try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
                for (MenuItem item : serve) {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM menu WHERE id ='" + item.getItem().getId() + "';");
                    if (rs.next()) {
                        int stock = rs.getInt("stock");
                        if (stock >= item.getAmount()) {
                            stmt.execute("UPDATE menu SET stock = " + (stock - item.getAmount()) + " WHERE id = '" + item.getItem().getId() + "';");
                        } else {
                            return -2;
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("An SQL Exception has occured: " + ex.getMessage());
            }
            fetchMenu();
            add.serve();
            servedQueue.add(preparingQueue.poll());
            return 1;
        }
    }

    @Override
    public MenuItem[][] listOrders(int queueNumber) {
        int i = findPreparingQueue(queueNumber);
        if (i >= 0) {
            return preparingQueue.get(i).getOrders();
        } else {
            i = findServedQueue(queueNumber);
            if (i >= 0) {
                return servedQueue.get(i).getOrders();
            } else {
                return null;
            }
        }
    }

    @Override
    public Customer[] listQueues() {
        return preparingQueue.toArray(new Customer[preparingQueue.size()]);
    }

    @Override
    public Customer[] listTables() {
        return tables;
    }

    @Override
    public Item[][] getMenu() {
        fetchMenu();
        return menu;
    }

    @Override
    public boolean restock(String id, int amount) throws SQLException {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu WHERE id = '" + id + "';");
            if (rs.next()) {
                int sum = rs.getInt("stock") + amount;
                stmt.execute("UPDATE menu SET stock = " + sum + " WHERE id = '" + id + "';");
                return true;
            } else {
                return false;
            }
        } finally {
            fetchMenu();
        }
    }

}
