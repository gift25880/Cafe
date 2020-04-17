
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
    private int lastQueueNumber = 0;
    private LinkedList<Customer> queue;
    private LinkedList<Customer> checkOutQueue;

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

    private int findCheckOutQueue(int queueNumber) {
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

    public double getTotalPrice(int queueNumber) {
        int i = findQueue(queueNumber);
        Customer c;
        if (i >= 0) {
            c = queue.get(i);
        } else {
            i = findCheckOutQueue(queueNumber);
            if (i >= 0) {
                c = checkOutQueue.get(i);
            } else {
                return -1;
            }
        }
        MenuItem[] mi = c.getOrders();
        double totalprice = 0;
        for (int j = 0; j <= mi.length; j++) {
            totalprice += mi[j].getAmount() * mi[j].getItem().getPrice();
        }
        return totalprice;
    }

    @Override
    public double checkOut(double amount, MemberAccount member, int queueNumber, boolean redeem) {
        int i = findCheckOutQueue(queueNumber);
        if (i >= 0) {
            double total = getTotalPrice(queueNumber);
            if (redeem) {
                return total - redeem(total, member);
            } else {
                int points = 0;
                double tmp = total;
                while (tmp >= PointPolicy.BATH_TO_ONE_POINT) {
                    points++;
                    tmp -= PointPolicy.BATH_TO_ONE_POINT;
                }
                member.setPoint(member.getPoint() + points);
                return total - amount;
            }
        } else {
            return i;
        }
    }

    @Override
    public int redeem(double total, MemberAccount member) {
        int points = member.getPoint();
        int discount = 0;
        while (points >= PointPolicy.POINT_TO_ONE_BATH && discount < (int) total) {
            discount++;
            points -= PointPolicy.POINT_TO_ONE_BATH;
        }
        member.setPoint(points);
        return discount;
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
    public boolean addMenu(Item item, Type type) {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO menu VALUES (" + item.getId() + ", " + item.getName() + ", " + item.getPrice() + ", " + item.getStock() + ", " + type + ");");
            System.out.println("The menu [" + item.getId() + " (" + item.getName() + ")] has been added successfully.");
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
                System.out.println("The menu [" + deleteId + " (" + deleteName + ")] has been removed successfully.");
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
    public boolean restock(String id, int amount) {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://35.240.242.174:3306/Cafe?zeroDateTimeBehavior=convertToNull", "int103", "int103");  Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu WHERE id = '" + id + "';");
            if (rs.next()) {
                int sum = rs.getInt("stock") + amount;
                rs.updateInt("stock", sum);
                System.out.println("The amount of " + rs.getString("name") + " has been restocked to " + sum + ".");
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

}
