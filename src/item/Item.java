//62130500003 กมลวิช วรเมธาเลิศ Kamolwish Woramethaleot
package item;

import java.util.Objects;

public class Item {

    private String id;
    private String name;
    private double price;
    private int stock;

    public Item(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public String getId() {
        return this.id;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int amount) {
        this.stock = amount;
    }

    public String toString() {
        return String.format("%4s: %-25s%10.2f Baht %4s", id, name, price, "[x" + stock + "]");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        return this.id.equalsIgnoreCase(other.id);
    }

}
