package customer;

import item.MenuItem;
import item.Item;
import java.util.LinkedList;

public class Customer {

    private LinkedList<MenuItem> orders = new LinkedList();
    private LinkedList<MenuItem> servedOrders = new LinkedList();
    private int queueNumber;
    private boolean takeHome;

    public Customer(int no) {
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

    public LinkedList<MenuItem> serve() {
        LinkedList<MenuItem> serve = new LinkedList();
        for (int i = 0; i < orders.size(); i++) {
            MenuItem tmp = orders.poll();
            servedOrders.add(tmp);
            serve.add(tmp);
        }
        return serve;
    }

    public MenuItem[][] getOrders() {
        MenuItem[][] allOrders = new MenuItem[2][];
        allOrders[0] = orders.toArray(new MenuItem[orders.size()]);
        allOrders[1] = servedOrders.toArray(new MenuItem[servedOrders.size()]);
        return allOrders;
    }

    public boolean equals(Customer c) {
        return this.queueNumber == c.queueNumber;
    }

    public boolean isTakeHome() {
        return this.takeHome;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public String toString() {
        return "Customer:\nQueue Number: " + queueNumber + "\nDining Status: " + (takeHome ? "Takehome" : "Eat In") + "\nNumber of Orders: " + orders.size();
    }
}
