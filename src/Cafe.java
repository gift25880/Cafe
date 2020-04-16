
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");
                Statement stmt = conn.createStatement()) {
            ResultSet rs = null;
            for (int i = 0; i < 3; i++) {
                int j = 0;
                switch (i) {
                    case 1:
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'Bakery';");
                        break;
                    case 2:
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'Dessert';");
                        break;
                    case 3:
                        rs = stmt.executeQuery("SELECT * FROM menu WHERE type = 'Beverage';");
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

    public double checkOutTakeHome(double amount, MemberAccount member, int queueNumber) {

    }

    @Override
    public int redeem(MemberAccount member) {

    }

    @Override
    public int addCustomer(boolean takeHome) {

    }

    @Override
    public boolean addMenu(Item item) {

    }

    @Override
    public boolean removeMenu(Item item) {

    }

    @Override
    public boolean addMember(Account member) {

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
    public boolean clearTables() {

    }

    @Override
    public Item[] getMenu() {

    }

    @Override
    public boolean addStock(String menuName, int amount) {

    }

}
