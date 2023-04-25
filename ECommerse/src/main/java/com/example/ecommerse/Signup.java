package com.example.ecommerse;

public class Signup {
    public static boolean customerLogin(Customer customer)  {
        DbConnection dbConnection = new DbConnection();
        String name= customer.getName();
        String userpassword= customer.getPassword();
        String email= customer.getEmail();
        String mobile= customer.getMobile();
        String query;
        query = "INSERT INTO customer(name,email,mobile,password) VALUES('"+name+"','"+email+"','"+mobile+"','"+userpassword+"')";
        int rs = dbConnection.updateDatabase(query);
        return rs != 0;
    }
}
