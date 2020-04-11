
public class MemberAccount extends Account{
    private int point;

    public MemberAccount(String id, Person person) {
        super(id, person);
    }
    
    public int getPoint() {
        return this.point;
    }
    
    public void setId(int point) {
        this.point = point;
    }
}
