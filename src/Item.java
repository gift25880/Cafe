
public class Item {
    private String name;
    private double price;
    private int stock;
    
    public Item(String name, double price, int stock) {
        
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getPrice() {
        return this.price;
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
