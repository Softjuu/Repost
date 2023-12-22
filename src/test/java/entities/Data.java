package edu;

import edu.Login;
import edu.Order;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.time.LocalTime;
public class Data {
    static String msg = "Error";

    private Data() {
    }

    static String path = "src/main/resources/back1/";
    static Logger logger = Logger.getLogger(Data.class.getName());

    public static List<String> getObjects(String fileName) {
        List<String> strings = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(path + fileName + ".txt", "rw")) {
            raf.seek(0);
            String s;
            while ((s = raf.readLine()) != null) {
                strings.add(s);
            }
        } catch (Exception e) {//ignored
        }
        return strings;
    }

    public static void storeObject(String fileName, Object object) {
        try (RandomAccessFile raf = new RandomAccessFile(path + fileName + ".txt", "rw")) {

            raf.seek(raf.length());
            raf.write(object.toString().getBytes());
        } catch (Exception e) {
            logger.info(msg);

        }
    }

    public static void removeFileContent(String fileName) {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(path + fileName + ".txt"))
        ) {
            writer.write("");
            writer.flush();
        } catch (Exception ignored) {//ignored
        }
    }

    public static List<Login> users() {
        List<Login> list = new ArrayList<>();
        for (String value : getObjects("Login")) {
            String[] arr = value.split(" ");
            Login login = new Login(arr[0], arr[1], arr[2]);
            list.add(login);
        }

        return list;
    }

    public static Customer getCustomerById(int id) {
        Customer customer=new Customer();
        for (Customer custom:getCustomers()){
            if(custom.getId()==id){
                customer=custom;
                break;
            }
        }
        return customer;
    }

    public static List<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        for (String value : getObjects("Customer")) {
            String[] arr = value.split(",");

            try {
                int customerId = Integer.parseInt(arr[0]);
                Customer customer = new Customer(customerId, arr[1], arr[2], arr[3], arr[4], arr[5]);
                customers.add(customer);
            } catch (NumberFormatException e) {
                System.err.println("\\\\\\\\\\ ");

            }


        }
        return customers;
    }

    public static Customer getCustomerBy(String email) {
        Customer foundCustomer = new Customer();
        for (Customer customer : getCustomers()) {
            if (customer.getEmail().equals(email)) {
                foundCustomer = customer;
                break;
            }
        }
        return foundCustomer;
    }

    public static void updateCustomers(List<Customer> customers) {
        try (RandomAccessFile raf = new RandomAccessFile("src/main/resources/back1/Customer.txt", "rw")
        ) {
            removeFileContent("Customer");
            raf.seek(0);
            for (Customer customer : customers) {
                raf.writeBytes(customer.getId() + "," + customer.getFullName() + "," + customer.getEmail() + "," + customer.getPhone() + "," +
                        customer.getAddress() + "," + customer.getPassword() + "\r\n");

            }
        } catch (Exception e) {//ignored
        }
    }

    public static int getId() {
        int id;

        id = getCustomers().get(getCustomers().size() - 1).getId();
        return id + 1;
    }

    public static void updateLogin(List<Login> loginList) {
        try (RandomAccessFile raf = new RandomAccessFile("src/main/resources/back1/Login.txt", "rw")
        ) {
            removeFileContent("Login");
            raf.seek(0);
            for (Login login : loginList) {
                raf.write(login.toString().getBytes());
            }
        } catch (Exception e) {
            logger.info(msg);

        }
    }

    public static List<Order> getOrderByCustomer(Customer customer) {
        List<Order> orders = new ArrayList<>();
        for (Order order1 : getOrders()) {
            if (order1.getCustomer().getId() == (customer.getId())) {
                orders.add(order1);
            }
        }
        return orders;
    }


    public static List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        for (String value : getObjects("Orders")) {
            List<Product> products;
            String[] arr = value.split(",");
            Order order = new Order();

            Customer customer = getCustomerById(Integer.parseInt(arr[0]));
            order.setCustomer(customer);


            
