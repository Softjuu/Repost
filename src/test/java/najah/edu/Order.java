package najah.edu;

import entities.Customer;
import entities.Data;

import entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Order {
    private Customer customer;
    private int id;


    private String name;



    private  List<Product> products;

   private int number;
    @Override
    public String toString() {

        return customer.getId()+","+id+","+name+","+number+"\r\n";
    }
    public  String getString(){
        String threeSpace="\t\t\t";
        StringBuilder str;
        str = new StringBuilder( this.getCustomer().getFullName() + threeSpace+
            this.getName() +
                threeSpace+ "\n");
        for (Product product:this.getProducts()){
          str.append(product.toString()).append("  ").append("\n");
        }
        str.append("\t\t\n");
        return str.toString();
    }

    public Order() {
        products=new ArrayList<>();
    }

    public void setNumber(int number) {
        this.number = number;
    }
   // private  List<Product> products;

    public Customer getCustomer() {
        return customer;
    }
    public void setProducts(List<Product> products) {
        this.products = products;

    }
    public List<Product> getProducts() {
      return products;
   }
  

   public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setCustomer(Customer customer) {
        this.customer=customer;



    }






}
