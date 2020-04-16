
public interface StaffService {
    public int addCustomer(boolean takeHome);
    public boolean addMenu(Item item, Type type);
    public boolean removeMenu(String id);
    public boolean addMember(Account member);
    public boolean serve();
    public MenuItem[] listOrders(int queueNumber);
    public Customer[] listQueues();
    public Customer[] listTables();
    public Item[][] getMenu();
    public boolean restock(String id, int amount);
}
