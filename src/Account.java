
public class Account {
    private String id;
    private Person person;

    public Account(String id, Person person) {
        this.id = id;
        this.person = person;
    }
    
    public Account(String id, String name, String phone) {
        this.id = id;
        this.person = new Person(name, phone);
    }
    
    public Account(Account account) {
        this.id = account.id;
        this.person = account.person;
    }
    
    public String getId() {
        return this.id;
    }
    
    public boolean equals(Object obj) {
        
    }
}
