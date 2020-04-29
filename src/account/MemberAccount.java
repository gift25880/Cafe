
//62130500003 กมลวิช วรเมธาเลิศ Kamolwish Woramethaleot

package account;

import person.Person;

public class MemberAccount extends Account{
    private int point;

    public MemberAccount(String user, Person person, int point) {
        super(user, person);
        this.point = point;
    }
    
    public MemberAccount(Account account, int point) {
        super(account);
        this.point = point;
    }
    
    public MemberAccount(String user, String name, String phone, int point) {
        super(user, name, phone);
        this.point = point;
    }
    
    public int getPoint() {
        return this.point;
    }
    
    public void setPoint(int point) {
        this.point = point;
    }
}
