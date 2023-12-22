package edu;

import entities.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.*;
import java.io.FileWriter;
import java.time.LocalDate;


import java.time.LocalTime;
import java.util.logging.Logger;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;




public class AdminLogin {
    CustomerLogin customerlogin;


    static Logger logger1 = Logger.getLogger(Data.class.getName());

    Admin admin = new Admin();
    Logger logger = Logger.getLogger(AdminLogin.class.getName());
    String msg = "Enter valid number";
    String msgInv = "Invalid Input, try again";
    String statusString = "waiting";
    private boolean logged;
    private Customer customer;

    public void setEmail(String email) {
        this.admin.setEmail(email);
    }

    public void adminMenu() {
        System.err.println("To See all customers details enter number 1 ");
        System.err.println("To View all installation appointments enter number 2 ");
        System.err.println("To See all product enter number 3 ");
        //System.err.println("To Add new order enter number 4");

        System.err.println("To logout enter number 8");
    }

    public void customerMenu() {

        while (true) {
            System.err.println("If you want to add a new customer, enter number 1");
            System.err.println("If you want to delete a customer, enter number 2");
        //    System.err.println("If you want to contact a customer, enter number 3");
            System.err.println("If you want to update a customer, enter number 3");
            System.err.println("If you want to go back, enter number 4");

            Scanner in = new Scanner(System.in);

            if (in.hasNextInt()) {
                int x = in.nextInt();

                if (x == 4) {
                    break;
                }

                if (x == 1) {
                    RecordCustomer customer = new RecordCustomer();
                    customer.newCustomer();
                } else if (x == 2) {
                    getCustomer();
                } else if (x == 3) {
                    System.err.println("Enter the unique ID to update the customer:");
                    Scanner scanner = new Scanner(System.in);
                    int customerUniqueId = scanner.nextInt();

                    System.err.println("Enter the field name to update (e.g., address, phone):");
                    String fieldName = scanner.next();

                    String filePath = "src/main/resources/back1/Customer.txt";

                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        StringBuilder newFileContent = new StringBuilder();
                        String line;

                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",");

                            if (parts.length > 0) {
                                try {
                                    int uniqueCustomerIdInFile = Integer.parseInt(parts[0]);

                                    if (customerUniqueId == uniqueCustomerIdInFile) {
                                        // Update the specified field in the line
                                        Customer customer = parseCustomerLine(line);
                                        updateField(customer, fieldName);
                                        newFileContent.append(customer.toString()).append("\n");
                                    } else {
                                        newFileContent.append(line).append("\n");
                                    }
                                } catch (NumberFormatException e) {
                                    // Handle the exception
                                }
                            }
                        }

                        // Write the modified content back to the file
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                            bw.write(newFileContent.toString());
                        } catch (IOException e) {
                            // Handle the exception
                        }

                    } catch (IOException e) {
                        // Handle the exception
                    }
                } else {
                    System.err.println("Invalid input. Please enter a valid number.");
                }
            } else {
                System.err.println("Invalid input. Please enter a valid number.");
                in.next(); // Consume the invalid input to avoid an infinite loop
            }
        }

    }



    private static Customer parseCustomerLine(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        String email= parts[2];
        String phoneNumber = parts[3];
        String address = parts[4];

        String pass = parts[5];

        Customer customer = new Customer();
        customer.setId(id);
        customer.setFullName(name);
        customer.setEmail(email);
        customer.setPhone(phoneNumber);


        customer.setAddress(address);
        customer.setPassword(pass);


        return customer;
    }

   public static void updateField(Customer customer, String fieldName) {
        Scanner scanner = new Scanner(System.in); // Declare the scanner here

        switch (fieldName.toLowerCase()) {
            case "address":
                System.err.println("Enter the new address:");
                String newAddress = scanner.nextLine();
                customer.setAddress(newAddress);
                break;
            case "phone":
                System.err.println("Enter the new phone number:");
                String newPhoneNumber = scanner.next();
                customer.setPhone(newPhoneNumber);
                break;
            // Add cases for other fields as needed
            default:
                System.err.println("Invalid field name");
                break;
        }
    }

    public void customerOptions(int option) {
        if (option == 1) {
            RecordCustomer customer = new RecordCustomer();
            customer.newCustomer();

        } else if (option == 2) {

            getCustomer();

        }
    }

    public void getCustomer() {
        System.err.println("Enter the ID of the customer ");
        Scanner in = new Scanner(System.in);
        Customer customer = new Customer();
        try {
            int id = in.nextInt();
            customer = Data.getCustomerById(id);

        } catch (InputMismatchException e) {
            logger.info(msgInv);
        }
        deleteCustomer(customer);
    }

    public void deleteCustomer(Customer customer) {
        int flag = 0;
        List<Customer> customers = Data.getCustomers();
        for (Customer customer1 : customers) {
            if (customer1.getId() == (customer.getId())) {
                customers.remove(customer1);
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            Data.updateCustomers(customers);
            System.err.println("The customer removed successfully");
        } else {
            System.err.println("This Customer is not exist in our customers");
        }

    }


    public void orderMenu() {
        boolean cond = true;
        while (true) {

          //  logger.info("If you want to change the product coast enter number 1");
            logger.info("If you want to add product enter number 2");

            logger.info("If you want to back enter number 3");

            Scanner in = new Scanner(System.in);
            int x;
            x = in.nextInt();
            if (x == 4) {
                break;
            }
            if (x == 1) {

                System.err.println("Enter the name update the product:");
                Scanner scanner = new Scanner(System.in);
                String UniqueId = scanner.next();
                updateProduct(UniqueId);


            } else if (x == 2) {
                recordProduct();

            }
        }

        }



    public boolean isExistOrder(int id) {
        Order order = Data.getOrderByID(id);
        return order.getId() == id;
    }

    public void changeStatus(int orderId, String status) {
        List<Order> orders = Data.getOrders();
        Customer customer = new Customer();
        for (Order order : orders) {
            if (order.getId() == orderId) {
                customer = order.getCustomer();
                // order.setStatus(status);
                break;
            }
        }
        Data.updateOrders(orders);
        //  notifyCustomer(customer);
    }




     public void adminPage() {
    Scanner in = new Scanner(System.in);
      // boolean cond = true;
       while (true) {
          try {
              adminMenu();
           int option = in.nextInt();
          if (option == 8) {
             logger.info("Goodbye");
               break;
          }

           else    if (option == 1) {
                  List<Customer> customers = Data.getCustomers();
                  System.err.println("****************************************************Customers**********************************************************");
                  System.err.println("ID               Name                                Email                           Mobile Number" +
                          "                        Address  " +
                          "");
                  for (Customer customer : customers) {
                      System.err.println(customer.getId() + "\t\t\t\t" + customer.getFullName() + getSpaces(customer.getFullName()) + customer.getEmail() + getSpaces(customer.getEmail())
                              + customer.getPhone() + getSpaces(customer.getPhone()) + customer.getAddress()
                      );
                  }
                  customerMenu();

              }

           else if (option == 2) {
              printInstallation();

                  workerMenu();
              }

           else if (option == 3) {
              printProduct();
                  orderMenu();

              } else if (option == 4) {
                  //   takenOrder();
              }

      } catch (Exception e) {
          logger.info("Enter a valid option number ");
            break;
          }
      }
     }

    public void workerMenu() {

        while (true) {
            System.err.println("If you want to add new installation appointments enter number 1");
            System.err.println("If you want to delete installation appointments enter number 2");
            System.err.println("If you want to update installation appointments time enter number 3");
            System.err.println("If you want to back enter number 4");

            Scanner in = new Scanner(System.in);
            int x;
            x = in.nextInt();
            if (x == 4) {
                break;
            }
            if (x == 1) {

                recordWorker();

            } else if (x == 2) {
                deleteInstallation();

            } else if (x == 3) {
                //updateWorker();

                System.err.println("Enter the unique ID to update the appointment time:");
                Scanner scanner = new Scanner(System.in);
                int UniqueId = scanner.nextInt();
                update(UniqueId);
            }
        } }

    public  void update(int x) {

    int customerUniqueId =x;

    String filePath = "src/main/resources/back1/InstallationApointments.txt";

                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        StringBuilder newFileContent = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            if (parts.length > 0) {
                try {
                    int uniqueCustomerIdInFile = Integer.parseInt(parts[0]);

                    if (customerUniqueId == uniqueCustomerIdInFile) {
                        // Update the date in the line
                        Installation appointment = parseInstallationLine(line);
                        updateTime(appointment);
                        newFileContent.append(appointment.toString()).append("\n");
                    } else {
                        newFileContent.append(line).append("\n");
                    }
                } catch (NumberFormatException e) {
                    // Handle the exception
                }
            }
        }

        // Write the modified content back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(newFileContent.toString());
        } catch (IOException e) {
            // Handle the exception
        }

    } catch (IOException e) {
        // Handle the exception
    }
     }




                    private static Installation parseInstallationLine(String line) {
                        String[] parts = line.split(",");
                        int id = Integer.parseInt(parts[0]);
                        String nameProduct = parts[1];
                        String service = parts[2];
                        String nameCustomer = parts[3];
                        LocalDate date = LocalDate.parse(parts[4]); // Assuming the date is in the format yyyy-MM-dd
                        LocalTime time = LocalTime.parse(parts[5]);
                        String timing = parts[6];
                        return new Installation(id, nameProduct, service, nameCustomer, date,time,timing);
                    }

                    public  void updateTime(Installation appointment) {
                        System.err.println("Enter the new time:");
                        Scanner scanner = new Scanner(System.in);
                        String newTimeStr = scanner.next();
                        System.err.println("Enter the new timing:");
                        String newTiming = scanner.next();
                        NewTime(appointment,newTimeStr,newTiming);}
    public  void  NewTime(Installation appointment,String time,String timing) {
        LocalTime newTime = LocalTime.parse(time);
                        appointment.setTime(newTime);
        appointment.setTiming(timing);
                        System.err.println("Done");
                    }



    public  void updateProduct(String x) {

       String customerUniqueId =x;

        String filePath = "src/main/resources/back1/product";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder newFileContent = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > 0) {
                    try {
                        String uniqueCustomerIdInFile =(parts[0]);

                        if (customerUniqueId .equals(uniqueCustomerIdInFile) ) {
                            // Update the date in the line
                           Product appointment = parseProductLine(line);
                            updateState(appointment);
                            newFileContent.append(appointment.toString()).append("\n");
                        } else {
                            newFileContent.append(line).append("\n");
                        }
                    } catch (NumberFormatException e) {
                        // Handle the exception
                    }
                }
            }

            // Write the modified content back to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                bw.write(newFileContent.toString());
            } catch (IOException e) {
                // Handle the exception
            }

        } catch (IOException e) {
            // Handle the exception
        }
    }


    private static Product parseProductLine(String line) {
        String[] parts = line.split(",");

        String nameProduct = parts[0];
        String service = parts[1];
        String nameCustomer = parts[2];
        Category date = Category.valueOf(parts[3]); // Assuming the date is in the format yyyy-MM-dd
        Double id = Double.parseDouble(parts[4]);
        int timing = Integer.parseInt(parts[5]);
        return new Product( nameProduct, service, nameCustomer, date,id,timing);
    }

    public  void updateState(Product appointment) {
        System.err.println("Enter the new coast:");
        Scanner scanner = new Scanner(System.in);
        String newTimeStr = scanner.next();

        NewCoast(appointment,newTimeStr);}
    public  void  NewCoast(Product appointment,String time) {
        double newTime = Double.parseDouble(time);
        appointment.setCost(newTime);

        System.err.println("Done");
    }



    public void workerOptions(int x) {
        if (x == 1) {
            recordWorker();
        } else if (x == 2) {
            deleteInstallation();
        } else if (x == 3) {
          // updateWorker();

        }
    }



    public void updateInstallations(String attribute, String value, Installation worker) {

        if (attribute.equalsIgnoreCase("ProductName")) {
            worker.setNameProduct(value);
        } else if (attribute.equalsIgnoreCase("Service")) {
            worker.setService(value);
        } else if (attribute.equalsIgnoreCase("CustomerName")) {
            worker.setNameCustomer(value);
        } else if (attribute.equalsIgnoreCase("Date")) {
            worker.setDate(LocalDate.parse(value));
        }

        List<Installation> workers = Data.getInstallation();
        for (Installation worker1 : workers) {
            int ind = workers.indexOf(worker1);
            if (worker1.getId() == worker.getId()) {
                workers.remove(ind);
                workers.add(ind, worker);
                break;
            }
        }

        Data.updateInstallation(workers);
    }




    public void deleteInstallation() {


        logger.info("Enter the ID of the worker you want to delete ");
        Scanner inn = new Scanner(System.in);
        int customerUniqueId = inn.nextInt();


        deleteInstallations(customerUniqueId);
    }
        public void deleteInstallations(int x) {
      int  customerUniqueId=x;
            String filePath = "src/main/resources/back1/InstallationApointments.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder newFileContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > 0) {
                    try {
                        int uniqueCustomerIdInFile = Integer.parseInt(parts[0]);

                        if (customerUniqueId != uniqueCustomerIdInFile) {

                            newFileContent.append(line).append("\n");
                        }
                    } catch (NumberFormatException e) {

                    }
                }
            }


            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.print(newFileContent.toString());
                System.err.println(" removed successfully");
            } catch (IOException e) {

            }

        } catch (IOException e) {

        }


}
    public void recordWorker() {
        Scanner in=new Scanner(System.in);
        Installation worker=new Installation();
        System.err.println("Enter Installer Id to install ");
        worker.setId(Integer.parseInt(in.nextLine()));
        System.err.println("Enter Product Name to install ");
        worker.setNameProduct(in.nextLine());
        System.err.println("Enter Make Car ");
        worker.setService(in.nextLine());
        System.err.println("Enter Customer Name ");
        worker.setNameCustomer(in.nextLine());
        System.err.println("Enter Installation Date ");
        worker.setDate(LocalDate.parse(in.nextLine()));
        System.err.println("Enter Installation Time ");
        worker.setTime(LocalTime.parse(in.nextLine()));
        System.err.println("Enter timing ");
        worker.setTiming(in.nextLine());
        System.err.println("Ok");
        addInstallation(worker);
    }

    public void printInstallation() {
        System.err.println("****************************************************Installation Appointments**********************************************************");
        System.err.println("InstallerID  ProductName  Service  " + " NameCustomer"
                +
                "Date   Time   \t\t\t\t\t" +
                "");

        String fileName = "src/main/resources/back1/InstallationApointments.txt";

        try {

            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);

            }


            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("error while reading " + e.getMessage());
        }


    }


    public void printProduct() {
        System.err.println("****************************************************Installation Appointments**********************************************************");
        System.err.println("ProductName  picture name  " + " state"
                +
                "Catogry   cost    OrderID   \t\t\t\t\t" +
                "");

        String fileName = "src/main/resources/back1/product";

        try {

            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);

            }


            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("error while reading " + e.getMessage());
        }


    }


        public boolean isExistCustomer ( int id){
            int flag = 0;
            for (Customer customer : Data.getCustomers()) {
                if (customer.getId() == id) {
                    flag = 1;
                    break;
                }
            }
            return flag == 1;
        }

    public boolean isExistInstallation ( int id){
        int flag = 0;
        for (Installation customer : Data.getInstallations()) {
            if (customer.getId() == id) {
                flag = 1;
                break;
            }
        }
        return flag == 1;
    }


    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    public String getSpaces(String att) {
       // return " ".repeat(Math.max(0, 35 - att.length()));
        return "";
    }

    public void notExistMsg() {
        logger.info("This Order is not exist on our orders");
    }

    public void setLogged(boolean b) {
        this.logged = b;
    }

    public boolean isLogged() {
        return logged;
    }


    public void addInstallation(Installation worker) {
        Data.storeObject("InstallationApointments",worker);
        logger.info("The Installation appointments added successfully");
    }

    public void recordProduct() {
        Scanner in=new Scanner(System.in);
        Product worker=new Product();
        System.err.println("Enter Product Name ");
        worker.setName(in.nextLine());
        System.err.println("Enter Picture Name  ");
        worker.setPictureName(in.nextLine());
        System.err.println("Enter the state ");
        worker.setMaterial(in.nextLine());
        System.err.println("Enter the Catogry ");
        worker.setCategory(Category.valueOf(in.nextLine()));
        System.err.println("Enter the cost ");
        worker.setCost(Double.parseDouble(in.nextLine()));
        System.err.println("Enter the order id ");
        worker.setOrderId(Integer.parseInt(in.nextLine()));

        System.err.println("Ok");
        addProduct(worker);
    }
    public void addProduct(Product worker) {
        Data.storeObject("product",worker);
        logger.info("The product added successfully");
    }

    }
