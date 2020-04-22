
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

    public boolean equals(Item item) {
        if (this == item) {
            return true;
        }
        if (item == null) {
            return false;
        }
        return true;
        
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int amount) {
        this.stock = amount;
    }
}
