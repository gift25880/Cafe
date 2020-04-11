
public interface MemberService {
    public boolean addItem(Item item, int queueNumber);
    public boolean removeItem(Item item, int queueNumber);
    public double checkOut(double amount, MemberAccount member, int tableNumber);
    public int redeem(MemberAccount member);
}
