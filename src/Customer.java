
import java.util.ArrayList;

public class Customer {
    private ArrayList<MenuItem> orders;
    private Status status;
    private int queueNumber;
    private boolean takeHome;
    
    public Customer(Status status, int no) {
        this.status = status;
        this.queueNumber = no;
    }
    
    public Item add(Item item) {
        
    }
    
    public Item remove(Item item) {
        
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public MenuItem[] getOrders() {
        
    }
    
    public boolean equals(Customer c) {
        
    }
    
    public boolean isTakeHome() {
        return this.takeHome;
    }
    
    public int getQueueNumber() {
        return queueNumber;
    }
}
