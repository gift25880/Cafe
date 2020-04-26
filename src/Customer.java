
import java.util.LinkedList;

public class Customer {

    private LinkedList<MenuItem> orders;
    private Status status;
    private int queueNumber;
    private boolean takeHome;

    public Customer(Status status, int no) {
        this.status = status;
        this.queueNumber = no;
    }

    public boolean add(Item item, int amount) {
        int i = orders.indexOf(item);
        if (i < 0) {
            return orders.add(new MenuItem(item, amount));
        } else {
            orders.get(i).addAmount(amount);
            return true;
        }
    }

    public boolean remove(Item item, int amount) {
        int i = orders.indexOf(item);
        if (i < 0) {
            return false;
        } else {
            int remaining = orders.get(i).removeAmount(amount);
            if (remaining < 0) {
                return false;
            } else if (remaining == 0) {
                orders.remove(i);
                return true;
            }
            return true;
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public MenuItem[] getOrders() {
        return orders.toArray(new MenuItem[orders.size()]);
    }

    public boolean equals(Customer c) {
        if (this == c) {
            return true;
        }
        if (c == null) {
            return false;
        }
        return true;
    }

    public boolean isTakeHome() {
        return this.takeHome;
    }

    public int getQueueNumber() {
        return queueNumber;
    }
}
