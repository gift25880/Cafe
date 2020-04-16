
public class MemberAccount extends Account{
    private int point = 0;

    public MemberAccount(String id, Person person) {
        super(id, person);
    }
    
    public MemberAccount(Account account) {
        super(account);
    }
    
    public MemberAccount(String id, String name, String phone) {
        super(id, name, phone);
    }
    
    public int getPoint() {
        return this.point;
    }
    
    public void setId(int point) {
        this.point = point;
    }
}
