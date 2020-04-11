
import java.util.LinkedList;


public class Cafe implements MemberService, StaffService, PointPolicy{
    private String cafeName;
    private Customer tables[];
    private int count;
    private LinkedList<Customer> queue;
    
    public Cafe(String cafeName, int maxTables) {
        
    }
    
    public boolean isFull() {
        
    }

    @Override
    public boolean addItem(Item item, int queueNumber) {
        
    }

    @Override
    public boolean removeItem(Item item, int queueNumber) {
        
    }

    @Override
    public double checkOut(double amount, MemberAccount member, int tableNumber) {
        
    }
    
    public double checkOutTakeHome(double amount, MemberAccount member, int queueNumber) {
        
    }

    @Override
    public int redeem(MemberAccount member) {
        
    }

    @Override
    public int addCustomer(boolean takeHome) {
        
    }

    @Override
    public boolean addMenu(Item item) {
       
    }

    @Override
    public boolean removeMenu(Item item) {
        
    }

    @Override
    public boolean addMember(Account member) {
        
    }

    @Override
    public boolean serve() {
        
    }

    @Override
    public MenuItem[] listOrders(int queueNumber) {
        
    }

    @Override
    public Customer[] listQueues() {
        
    }

    @Override
    public Customer[] listTables() {
        
    }

    @Override
    public boolean clearTables() {
        
    }

    @Override
    public Item[] getMenu() {
        
    }

    @Override
    public boolean addStock(String menuName, int amount) {
        
    }
    
}
