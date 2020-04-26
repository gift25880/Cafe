
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

    public Item add(Item item) {
        status = Status.PREPARING;
        for (int i = 0; i < orders.size(); i++) {
            if (i == i) {
                orders.get(i).addAmount(1);
            }
        }
        return item;
    }

    public Item remove(Item item) {
        status = Status.SERVED;
        for (int i = 0; i < orders.size(); i--) {
            orders.get(i).removeAmount(1);
        }
        return item;
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
