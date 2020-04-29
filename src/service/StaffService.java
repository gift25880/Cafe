
//62130500048 ปฏิญญา ทองอ่วม Pathinya Thonguam

package service;

import item.MenuItem;
import item.Type;
import customer.Customer;
import item.Item;
import account.Account;
import java.sql.SQLException;


public interface StaffService {
    public int addCustomer(boolean takeHome);
    public boolean addMenu(Item item, Type type) throws SQLException;
    public boolean removeMenu(String id) throws SQLException;
    public boolean addMember(Account member) throws SQLException;
    public int serve();
    public MenuItem[][] listOrders(int queueNumber);
    public Customer[] listQueues();
    public Customer[] listTables();
    public Item[][] getMenu();
    public boolean restock(String id, int amount) throws SQLException;
}
