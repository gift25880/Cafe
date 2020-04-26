package customer;


import item.MenuItem;
import item.Item;
import java.util.LinkedList;

public class Customer {

    private LinkedList<MenuItem> orders;
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
    
    public String toString(){
        return "Customer:\nQueue Number: " + queueNumber + "\nDining Status: " + (takeHome ? "Takehome" : "Eat In") + "\nNumber of Orders: " + orders.size();
    }
}
