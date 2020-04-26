package service;


import account.MemberAccount;
import java.io.IOException;
import java.sql.SQLException;


public interface CustomerService {
    public boolean addItem(String id, int queueNumber, int amount);
    public boolean removeItem(String id, int queueNumber, int amount);
    public double checkOut(double total, int discount, double amount, MemberAccount member, int queueNumber, boolean redeem, int setPoints) throws IOException, SQLException;
    public int[] redeem(double total, MemberAccount member);
}
