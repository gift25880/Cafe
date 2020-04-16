
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Objects;

public class Cafe implements MemberService, StaffService, PointPolicy {

    private String cafeName;
    private Customer tables[];
    private Item menu[][];
    private int count = 0;
    private LinkedList<Customer> queue;

    public Cafe(String cafeName, int maxTables) {
        Objects.requireNonNull(cafeName, "The cafe name cannot be blank.");
        this.cafeName = cafeName;
        tables = new Customer[maxTables > 0 ? maxTables : 10];
        fetchMenu();
    }

    private void fetchMenu() {
        menu = new Item[3][100];
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = null;
            for (int i = 0; i < 3; i++) {
                int j = 0;
                switch (i) {
                    case 0:
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'BAKERY';");
                        break;
                    case 1:
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'DESSERT';");
                        break;
                    case 2:
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
        return this.count == this.tables.length;
    }

    @Override
    public boolean addItem(Item item, int queueNumber) {

    }

    @Override
    public boolean removeItem(Item item, int queueNumber) {

    }

    @Override
    public double checkOut(double amount, MemberAccount member, int tableNumber) {

    }

    @Override
    public int redeem(MemberAccount member) {

    }

    @Override
    public int addCustomer(boolean takeHome) {
        int queueNum = queue.peekLast().getQueueNumber() + 1;
        Customer c = new Customer(Status.PREPARING, queueNum);
        queue.add(c);
        if (!takeHome) {
            if (!isFull()) {
                for (int i = 0; i < tables.length; i++) {
                    if (tables[i] == null) {
                        tables[i] = c;
                    }
                }
            } else {
                return -1;
            }
        }
        return queueNum;
    }

    @Override
    public boolean addMenu(Item item, Type type) {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO menu VALUES (" + item.getId() + ", " + item.getName() + ", " + item.getPrice() + ", " + item.getStock() + ", " + type + ");");
            System.out.println("The menu [" + item.getId() + " (" + item.getName() +")] has been added successfully.");
            return true;
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("This menu already exists.");
            return false;
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
            return false;
        } finally {
            fetchMenu();
        }
    }

    @Override
    public boolean removeMenu(String id) {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu WHERE id = '" + id + "';");
            if (rs.next()) {
                String deleteId = rs.getString("id");
                String deleteName = rs.getString("name");
                rs.deleteRow();
                System.out.println("The menu [" + deleteId + " (" + deleteName +")] has been removed successfully.");
                return true;
            } else {
                System.out.println("Menu not found.");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
            return false;
        } finally {
            fetchMenu();
        }
    }

    @Override
    public boolean addMember(Account member) {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO member VALUES (" + member.getName() + ", " + member.getPhone() + ", " + member.getId() + ", " + 0 + ");");
            System.out.println("Member [" + member.getId() + "] has been added successfully.");
            return true;
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("This account is already a member of this cafe.");
            return false;
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean serve() {

    }

    @Override
    public MenuItem[] listOrders(int queueNumber) {

    }

    @Override
    public Customer[] listQueues() {

    }

    @Override
    public Customer[] listTables() {

    }

    @Override
    public Item[][] getMenu() {
        fetchMenu();
        return menu;
    }

    @Override
    public boolean restock(String id, int amount) {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu WHERE id = '" + id + "';");
            if(rs.next()){
                int sum = rs.getInt("stock") + amount;
                rs.updateInt("stock", sum);
                System.out.println("The amount of " + rs.getString("name") + " has been restocked to " + sum + ".");
                return true;
            } else{
                System.out.println("Menu not found.");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("An SQL Exception has occured: " + ex.getMessage());
            return false;
        }
    }

}
