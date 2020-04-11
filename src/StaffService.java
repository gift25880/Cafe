
public interface StaffService {
    public int addCustomer(boolean takeHome);
    public boolean addMenu(Item item);
    public boolean removeMenu(Item item);
    public boolean addMember(Account member);
    public boolean serve();
    public MenuItem[] listOrders(int queueNumber);
    public Customer[] listQueues();
    public Customer[] listTables();
    public boolean clearTables();
    public Item[] getMenu();
    public boolean addStock(String menuName, int amount);
}
