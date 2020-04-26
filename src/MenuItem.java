
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
        if(this.amount < amount){
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

    public boolean equals(MenuItem item) {
        return this.item.getId().equals(item.item.getId());
    }
}
