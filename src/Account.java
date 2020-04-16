
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
    
    public Person getPerson(){
        return person;
    }
    
    public String getName(){
        return person.getName();
    }
    
    public String getPhone(){
        return person.getPhone();
    }
    
    public String getId() {
        return this.id;
    }
    
    public boolean equals(Object obj) {
        
    }
}
