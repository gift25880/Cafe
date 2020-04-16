
public class Item {
    private String id;
    private String name;
    private double price;
    private int stock;
    
    public Item(String id, String name, double price, int stock) {
        
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getPrice() {
        return this.price;
    }

    public String getId() {
        return id;
    }
    
    public boolean equals(Item item) {
        
    }
    
    public int getStock() {
        return this.stock;
    }
    
    public void setStock(int amount) {
        this.stock = amount;
    }
}
