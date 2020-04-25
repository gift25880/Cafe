
import java.io.IOException;


public interface CustomerService {
    public Item addItem(String id, int queueNumber);
    public Item removeItem(String id, int queueNumber);
    public double checkOut(double total, int discount, double amount, MemberAccount member, int queueNumber, boolean redeem, int setPoints) throws IOException;
    public int[] redeem(double total, MemberAccount member);
}
