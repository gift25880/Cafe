
import java.io.IOException;


public interface CustomerService {
    public boolean addItem(String id, int queueNumber, int amount);
    public boolean removeItem(String id, int queueNumber, int amount);
    public double checkOut(double total, int discount, double amount, MemberAccount member, int queueNumber, boolean redeem, int setPoints) throws IOException;
    public int[] redeem(double total, MemberAccount member);
}
