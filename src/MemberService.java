
public interface MemberService {
    public Item addItem(String id, int queueNumber);
    public Item removeItem(String id, int queueNumber);
    public double checkOut(double amount, MemberAccount member, int queueNumber, boolean redeem);
    public int redeem(double amount, MemberAccount member);
}
