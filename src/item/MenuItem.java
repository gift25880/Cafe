//62130500003 กมลวิช วรเมธาเลิศ Kamolwish Woramethaleot
package item;

import java.util.Objects;

public class MenuItem {

    private Item item;
    private int amount;

    public MenuItem(Item item, int amount) {
        this.item = item;
        this.amount = amount;

    }

    public void setItem(Item item) {
        this.item = item;

    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public int removeAmount(int amount) {
        if (this.amount < amount) {
            return -1;
        }
        this.amount -= amount;
        return this.amount;
    }

    public Item getItem() {
        return this.item;

    }

    public int getAmount() {
        return this.amount;

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
        final MenuItem other = (MenuItem) obj;
        return Objects.equals(this.item, other.item);
    }

    public String toString() {
        return String.format("%4s: %-25s%10.2f Baht %4s", item.getId(), item.getName(), item.getPrice(), "[x" + amount + "]");
    }
}
