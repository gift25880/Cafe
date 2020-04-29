//62130500003 กมลวิช วรเมธาเลิศ Kamolwish Woramethaleot
package account;

import java.util.Objects;
import person.Person;

public class Account {

    private String user;
    private Person person;

    public Account(String user, Person person) {
        this.user = user;
        this.person = person;
    }

    public Account(String user, String name, String phone) {
        this.user = user;
        this.person = new Person(name, phone);
    }

    public Account(Account account) {
        this.user = account.user;
        this.person = account.person;
    }

    public Person getPerson() {
        return person;
    }

    public String getName() {
        return person.getName();
    }

    public String getPhone() {
        return person.getPhone();
    }

    public String getUser() {
        return this.user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        return this.user.equalsIgnoreCase(other.user);
    }

}
