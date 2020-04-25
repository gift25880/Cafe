
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class Cafe implements CustomerService, StaffService, PointPolicy {

    private String cafeName;
    private Cashier cashier;
    private Customer tables[];
    private Item menu[][];
    private int count = 0;
    private int lastQueueNumber = 0;
    private LinkedList<Customer> queue;
    private LinkedList<Customer> checkOutQueue;

    public Cafe(String cafeName, int maxTables) {
        Objects.requireNonNull(cafeName, "The cafe name cannot be blank.");
        this.cafeName = cafeName;
        tables = new Customer[maxTables > 0 ? maxTables : 10];
        fetchMenu();
    }
    
    public String getCafeName() {
        return this.cafeName;
    }
    
    public void setManager(Cashier manager){
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
                        rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM menu WHERE type = ''BAKERY';");
                        rs.next();
                        menu[0] = new Item[rs.getInt("rowcount")];
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'BAKERY';");
                        break;
                    case 1:
                        rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM menu WHERE type = ''DESSERT';");
                        rs.next();
                        menu[1] = new Item[rs.getInt("rowcount")];
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'DESSERT';");
                        break;
                    case 2:
                        rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM menu WHERE type = ''BEVERAGE';");
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

    private int findQueue(int queueNumber) {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getQueueNumber() == queueNumber) {
                return i;
            }
        }
        return -1;
    }

    public int findCheckOutQueue(int queueNumber) {
        for (int i = 0; i < checkOutQueue.size(); i++) {
            if (checkOutQueue.get(i).getQueueNumber() == queueNumber) {
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
    public Item addItem(String id, int queueNumber) {
        int i = findQueue(queueNumber);
        if (i >= 0) {
            return queue.get(i).add(findMenu(id));
        } else {
            i = findCheckOutQueue(queueNumber);
            if (i >= 0) {
                queue.add(checkOutQueue.remove(i));
                queue.peekLast().setStatus(Status.PREPARING);
                return queue.peekLast().add(findMenu(id));
            } else {
                return null;
            }
        }
    }

    @Override
    public Item removeItem(String id, int queueNumber) {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getQueueNumber() == queueNumber) {
                return queue.get(i).remove(findMenu(id));
            }
        }
        return null;
    }

    public double getTotalPrice(int queueOrder) {
        Customer c = checkOutQueue.get(queueOrder);
        MenuItem[] mi = c.getOrders();
        double totalprice = 0;
        for (int j = 0; j <= mi.length; j++) {
            totalprice += mi[j].getAmount() * mi[j].getItem().getPrice();
        }
        return totalprice;
    }

    @Override
    public double checkOut(double total, int discount, double amount, MemberAccount member, int queueNumber, boolean redeem, int setPoints) throws IOException {
        int i = findCheckOutQueue(queueNumber);
        if (i >= 0) {
            if (!checkOutQueue.get(i).isTakeHome()) {
                for (int j = 0; j < tables.length; j++) {
                    if (tables[j].getQueueNumber() == queueNumber) {
                        tables[j] = null;
                        break;
                    }
                }
            }
            if (redeem) {
                member.setPoint(setPoints);
            } else {
                int points = 0;
                double tmp = total;
                while (tmp >= PointPolicy.BATH_TO_ONE_POINT) {
                    points++;
                    tmp -= PointPolicy.BATH_TO_ONE_POINT;
                }
                member.setPoint(member.getPoint() + points);
            }
            printReceipt(checkOutQueue.remove(i), total, discount, member.getUser());
                return (total - discount) - amount;
        } else {
            return i;
        }
    }

    @Override
    public int[] redeem(double total, MemberAccount member) {
        int points = member.getPoint();
        int discount = 0;
        while (points >= PointPolicy.POINT_TO_ONE_BATH && discount < (int) total) {
            discount++;
            points -= PointPolicy.POINT_TO_ONE_BATH;
        }
        return new int[]{points, discount};
    }

    public void printReceipt(Customer c, double total, double discount, String user) throws IOException {
        File file = new File("receipt/" + LocalDate.now() + "/receipt_queue_" + c.getQueueNumber() + ".txt");
        file.getParentFile().mkdirs();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("**THANK YOU FOR DINING AT " + this.cafeName.toUpperCase() + "**\n");
            bw.write("Check Out Time: " + LocalDateTime.now().format(format) + "\n");
            bw.write("Cashier: " + cashier.getStaff().getName() + "\n");
            bw.write("----------------------------------------\n");
            bw.write("Queue Number: " + c.getQueueNumber() + "\n");
            bw.write("Member: " + (user == null ? "-" : user) + "\n");
            bw.write("Dining Status: " + (c.isTakeHome() ? "Takehome" : "Eat In") + "\n");
            bw.write("----------------------------------------\n");
            MenuItem[] mi = c.getOrders();
            int i = 1;
            for (MenuItem menuOrder : mi) {
                bw.write(String.format("%2d", i++) + ". " + menuOrder.getItem().getName() + " [x" + menuOrder.getAmount() + "] Price: " + (menuOrder.getAmount() * menuOrder.getItem().getPrice()) + "\n");
            }
            bw.write("\n----------------------------------------\n");
            bw.write(String.format("%26s%8.2f", "Total Price: ", total) + "\n");
            bw.write(String.format("%26s%8.2f", "Discount: ", discount) + "\n");
            bw.write(String.format("%26s%8.2f", "Net Price: ", (total - discount)) + "\n");
            bw.write("----------------------------------------\n");
        }
    }

    @Override
    public int addCustomer(boolean takeHome) {
        Customer c = new Customer(Status.PREPARING, ++lastQueueNumber);
        queue.add(c);
        if (!takeHome) {
            if (!isFull()) {
                for (int i = 0; i < tables.length; i++) {
                    if (tables[i] == null) {
                        tables[i] = c;
                        count++;
                    }
                }
            } else {
                return -1;
            }
        }
        return lastQueueNumber;
    }

    @Override
    public boolean addMenu(Item item, Type type) throws SQLException {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.247.136.57:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO menu VALUES (" + item.getId() + ", " + item.getName() + ", " + item.getPrice() + ", " + item.getStock() + ", " + type.name() + ");");
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
                rs.deleteRow();
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
            stmt.executeUpdate("INSERT INTO member VALUES (" + member.getUser() + ", " + member.getName() + ", " + member.getPhone() + ", " + 0 + ");");
            return true;
        }
    }

    @Override
    public boolean serve() {
        Customer add = queue.poll();
        if (add == null) {
            return false;
        } else {
            checkOutQueue.add(add);
            add.setStatus(Status.SERVED);
            return true;
        }
    }

    @Override
    public MenuItem[] listOrders(int queueNumber) {
        int i = findQueue(queueNumber);
        if (i >= 0) {
            return queue.get(i).getOrders();
        } else {
            i = findCheckOutQueue(queueNumber);
            if (i >= 0) {
                return checkOutQueue.get(i).getOrders();
            } else {
                return null;
            }
        }
    }

    @Override
    public Customer[] listQueues() {
        return queue.toArray(new Customer[queue.size()]);
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
                rs.updateInt("stock", sum);
                return true;
            } else {
                return false;
            }
        } finally {
            fetchMenu();
        }
    }

}
