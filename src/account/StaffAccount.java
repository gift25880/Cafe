
//62130500003 กมลวิช วรเมธาเลิศ Kamolwish Woramethaleot

package account;

import person.Person;

public class StaffAccount extends Account{
    private Position position;
    private String password;

    public StaffAccount(String id, Person person, Position position, String password) {
        super(id, person);
        this.position = position;
        this.password = password;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
