package account;


import person.Person;
import account.Account;


public class MemberAccount extends Account{
    private int point;

    public MemberAccount(String id, Person person, int point) {
        super(id, person);
        this.point = point;
    }
    
    public MemberAccount(Account account, int point) {
        super(account);
        this.point = point;
    }
    
    public MemberAccount(String id, String name, String phone, int point) {
        super(id, name, phone);
        this.point = point;
    }
    
    public int getPoint() {
        return this.point;
    }
    
    public void setPoint(int point) {
        this.point = point;
    }
    
    public void setId(String id) {
        super.setUser(id);
    }
}
