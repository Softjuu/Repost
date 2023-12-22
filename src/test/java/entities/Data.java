package najah.edu;


import najah.edu.Login;
import najah.edu.Order;

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
            order.setId(Integer.parseInt(arr[1]));
            order.setName(arr[2]);
            order.setNumber(Integer.parseInt(arr[3]));

            products = ProductFile.getProductByOrder(order.getId());
            order.setProducts(products);
            orders.add(order);
        }
        return orders;
    }

    public static Order getOrderByID(int id) {
        Order order = new Order();
        for (Order order1 : getOrders()) {
            if (order1.getId() == id) {
                order = order1;
                break;
            }
        }
        return order;
    }

    public static void updateOrders(List<Order> orders) {
        try (RandomAccessFile raf = new RandomAccessFile("src/main/resources/Back/order.txt", "rw")
        ) {
            removeFileContent("order");
            raf.seek(0);
            for (Order order : orders) {
                raf.write(order.toString().getBytes());
            }
        } catch (Exception e) {
            logger.info(msg);

        }
    }

    public static List<Installation> getInstallation() {
        List<Installation> workers = new ArrayList<>();
        for (String value : getObjects("InstallationApointments")) {
            if (!value.isEmpty()) {
                String[] arr = value.split(",");
                if (arr.length >= 5 && !arr[0].isEmpty() && !arr[4].isEmpty()) {
                    try {
                        Installation worker = new Installation(
                                Integer.parseInt(arr[0].trim()),
                                arr[1].trim(),
                                arr[2].trim(),
                                arr[3].trim(),
                                LocalDate.parse(arr[4].trim()),
                                LocalTime.parse(arr[5]),
                                arr[6]
                        );
                        workers.add(worker);
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.out.println("Warning: Invalid data format. Skipping entry. Data: " + Arrays.toString(arr));
                    }
                } else {
                    System.out.println("Warning: Incomplete data. Skipping entry. Data: " + Arrays.toString(arr));
                }
            }
        }
        return workers;
    }

    public static List<Installation> getInstallations() {
        List<Installation> workers = new ArrayList<>();
        for (String value : getObjects("InstallationApointments")) {
            String[] arr = value.split(",");

            if (arr.length >= 7 && !arr[0].isEmpty() && !arr[4].isEmpty()&& !arr[5].isEmpty()&& !arr[6].isEmpty()) {
                try {
                    Installation worker = new Installation(
                            Integer.parseInt(arr[0]),
                            arr[1],
                            arr[2],
                            arr[3],
                            LocalDate.parse(arr[4]),
                            LocalTime.parse(arr[5]),
                            arr[6]
                    );

                    workers.add(worker);
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println("Error parsing data: " + Arrays.toString(arr));
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid data: " + Arrays.toString(arr));
            }
        }
        return workers;
    }




    public static List<Product> getProduct() {
        List<Product> workers = new ArrayList<>();
        for (String value : getObjects("product")) {
            String[] arr = value.split(",");

            if (arr.length >= 6 && !arr[0].isEmpty() && !arr[4].isEmpty()&& !arr[5].isEmpty()) {
                try {
                  Product worker = new Product(
                           (arr[0]),
                            arr[1],
                            arr[2],
                           Category.valueOf(arr[3]) ,
                          Double.parseDouble(arr[4]),
                            Integer.parseInt(arr[5])

                    );

                    workers.add(worker);
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println("Error parsing data: " + Arrays.toString(arr));
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid data: " + Arrays.toString(arr));
            }
        }
        return workers;
    }

    public static List<InstallerAvailable> getInstallationsAvailable() {
        List<InstallerAvailable> workers = new ArrayList<>();
        for (String value : getObjects("Installation")) {
            String[] arr = value.split(",");

            if (arr.length >= 6 && !arr[0].isEmpty() && !arr[4].isEmpty()&& !arr[5].isEmpty()) {
                try {
                    InstallerAvailable worker = new InstallerAvailable(
                            Integer.parseInt(arr[0]),
                            arr[1],
                            arr[2],

                            LocalDate.parse(arr[3]),
                            LocalTime.parse(arr[4]), arr[5]
                    );

                    workers.add(worker);
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println("Error parsing data: " + Arrays.toString(arr));
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid data: " + Arrays.toString(arr));
            }
        }
        return workers;
    }
    public static int getInstallationId() {
        int id;

        id=getInstallation().get(getInstallation().size()-1).getId();
        return id+1;
    }
    public static Installation getInstallationById(int id){
        Installation worker=new Installation();
        for (Installation worker1:getInstallations()){
            if(worker1.getId()==id){
                worker=worker1;
                break;
            }
        }
        return worker;
    }


    public static InstallerAvailable getInstallationAvailableById(int id,LocalDate date){
        InstallerAvailable worker=new InstallerAvailable();
        for (InstallerAvailable worker1:getInstallationsAvailable()){
            if(worker1.getId()==id && worker1.getDate()==date){
                worker=worker1;
                break;
            }
        }
        return worker;
    }
    Customer customer=new Customer();

    public static void updateInstallation(List<Installation>workers){
        try (RandomAccessFile raf = new RandomAccessFile("src/main/resources/Back/InstallationApointments.txt", "rw")
        ){
            removeFileContent("InstallationApointments");
            raf.seek(0);
            for (Installation worker:workers) {
                raf.writeBytes(worker.getId() + "," + worker.getNameProduct() + "," + worker.getService() + "," + worker.getNameCustomer() + "," +
                        worker.getDate()  + "\r\n");

            }
        }
        catch(Exception e){
            logger.info(msg);

        }}
    }


