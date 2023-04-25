package com.example.ecommerse;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;

public class Order {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty quantity;
    private SimpleObjectProperty<Date> orderDate;
    private SimpleStringProperty orderStatus;

    public Order(int id, String name, int quantity, Date orderDate, String orderStatus) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.orderDate = new SimpleObjectProperty(orderDate);
        this.orderStatus = new SimpleStringProperty(orderStatus);
    }

    public static ObservableList<Order> getAllOrders(int cust_id)//int customer_id
    {
        String selectAllProducts="SELECT orders.id,product.name,orders.quantity,orders.order_date,orders.order_status FROM orders JOIN product ON orders.product_id=product.id WHERE orders.customer_id="+cust_id;
        return fetchOrderData(selectAllProducts);
    }

    public static ObservableList<Order> fetchOrderData(String query)
    {
        ObservableList<Order> data= FXCollections.observableArrayList();
        DbConnection dbConnection=new DbConnection();
        try {
            ResultSet rs=dbConnection.getQueryTable(query);
            while (rs.next())
            {
                Order product=new Order(rs.getInt("id"),rs.getString("name")
                        , rs.getInt("quantity"),rs.getDate("order_date"),rs.getString("order_status"));
                data.add(product);
            }
            return data;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean placeOrder(Customer customer, Product product)
    {
        String groupOrderId="SELECT max(group_order_id)+1 AS id FROM orders";
        DbConnection dbConnection=new DbConnection();
        try {
            ResultSet rs=dbConnection.getQueryTable(groupOrderId);
            if (rs.next())
            {
                String placeOrder="INSERT INTO orders(group_order_id,customer_id,product_id) VALUES("+rs.getInt("id")+","+customer.getId()+","+product.getId()+")";
                return dbConnection.updateDatabase(placeOrder) != 0;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public static int placeMultipleOrder(Customer customer, ObservableList<Product> productList)
    {
        String groupOrderId="SELECT max(group_order_id)+1 AS id FROM orders";
        DbConnection dbConnection=new DbConnection();
        try {
            ResultSet rs=dbConnection.getQueryTable(groupOrderId);
            int count=0;
            if (rs.next())
            {
                for (Product product:productList)
                {
                    String placeOrder="INSERT INTO orders(group_order_id,customer_id,product_id) VALUES("+rs.getInt("id")+","+customer.getId()+","+product.getId()+")";
                    count += dbConnection.updateDatabase(placeOrder);
                }
                return count;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public int getQuantity() {
        return quantity.get();
    }
    public Date getOrderDate(){
        return orderDate.get();
    }
    public String getOrderStatus() { return orderStatus.get(); }
}
