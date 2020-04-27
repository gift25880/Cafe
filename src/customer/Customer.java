package customer;

import item.MenuItem;
import item.Item;
import java.util.LinkedList;
import service.TextFormatter;

public class Customer {

    private LinkedList<MenuItem> orders = new LinkedList();
    private LinkedList<MenuItem> servedOrders = new LinkedList();
    private int queueNumber;
    private boolean takeHome;

    public Customer(int no, boolean takeHome) {
        this.queueNumber = no;
        this.takeHome = takeHome;
    }

    public boolean add(Item item, int amount) {
        int i;
        for (i = 0; i < orders.size(); i++) {
            if (orders.get(i).getItem().getId().equalsIgnoreCase(item.getId())) {
                break;
            }
        }
        if (i >= orders.size()) {
            return orders.add(new MenuItem(item, amount));
        } else {
            orders.get(i).addAmount(amount);
            return true;
        }
    }

    public boolean remove(Item item, int amount) {
        int i;
        for (i = 0; i < orders.size(); i++) {
            if (orders.get(i).getItem().getId().equalsIgnoreCase(item.getId())) {
                break;
            }
        }
        if (i >= orders.size()) {
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
        while (orders.size() > 0) {
            MenuItem tmp = orders.poll();
            int j;
            for (j = 0; j < servedOrders.size(); j++) {
                if (servedOrders.get(j).getItem().getId().equalsIgnoreCase(tmp.getItem().getId())) {
                    break;
                }
            }
            if (j >= servedOrders.size()) {
                servedOrders.add(tmp);
            } else {
                servedOrders.get(j).addAmount(tmp.getAmount());
            }
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
        return TextFormatter.getCode("cyan") + "Queue Number: " + queueNumber + "\nDining Status: " + (takeHome ? "Takehome" : "Eat In") + "\nNumber of Preparing Orders: " + orders.size() + "\nNumber of Served Orders: " + servedOrders.size();
    }
}
