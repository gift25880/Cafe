
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
        this.amount = amount;
    }

    public void removeAmount(int amount) {
        this.amount = amount;
    }

    public Item getItem() {
        return this.item;

    }

    public int getAmount() {
        return this.amount;

    }

    public boolean equals(MenuItem item) {
        if (this == item) {
            return true;
        }
        if (item == null) {
            return false;
        }
        return true;
    }
}
