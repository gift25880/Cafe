
//62130500003 กมลวิช วรเมธาเลิศ Kamolwish Woramethaleot

package item;

import item.Item;

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
    
    public String toString(){
        return String.format("%4s: %-25s%10.2f Baht %4s", item.getId(), item.getName(), item.getPrice(), "[x" + amount + "]");
    }
}
