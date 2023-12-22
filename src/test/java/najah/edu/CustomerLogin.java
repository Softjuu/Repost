package najah.edu;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import entities.Customer;
import entities.Data;
import entities.InstallerAvailable;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.NoSuchElementException;
import java.time.format.DateTimeParseException;
public class CustomerLogin {
    private Customer customer;
   public InstallerAvailable worker;
   // static String path = "src/main/resources/back1/";
    static Logger logger = Logger.getLogger(Data.class.getName());

    public void menu(){
        logger.info("If you want to add new order enter number 1");
      logger.info("If you want to see your orders enter number 2");
        logger.info("If you want to see your information enter number 3");
        logger.info("If you want to Update your information enter number 4");
      logger.info("If you want to see all product enter number 5");
       logger.info("If you want to logout enter number 6");
      logger.info("If you want request installation services enter number 7");


    }

    private static String readFromFil(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) { //

        }
        return content.toString();
    }
    public Customer getCustomer() {
        return customer;
    }
    private static void writeToFil(String fileName, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(content);
        } catch (IOException e) {//

        }
    }





    public void customerPage(){
       logger.info("Welcome To The Customer Dashboard ");
        Scanner in=new Scanner(System.in);

        menu();
        try {
            int option = in.nextInt();

            while (true) {
                if (option == 1) {

                    Order  login=new  Order();
                    login.setCustomer(this.customer);



                    String path = "src/main/resources/back1/";
                    try {
                    Scanner scanner = new Scanner(System.in);
                   logger.info("Enter the name of Product:");
                    String inputData = scanner.nextLine();


                   logger.info("Enter the id of order:");
                    String inp = scanner.nextLine();


                   logger.info("Enter the number of Product to order:");
                    String input = scanner.nextLine();

                    String fileName = path + "order.txt";

                    int customerI = customer.getId();
                    String currentContent = readFromFil(fileName);

                   logger.info("If you want to confirm tho order enter 1 else enter 2 ");

                    int x = in.nextInt();

                    if (x==1) {

                        String newContent = currentContent +customerI + " " +inp + " " + inputData + " " + input + "\n";
                        writeToFil(fileName, newContent);

                        logger.info("Done. " + inputData + " has been added .");
                        menu();
                        //option = in.nextInt();

                    } else if (x ==2) {

                      logger.info("OK");
                        menu();
                  //      option = in.nextInt();


                    }
                    } catch (NoSuchElementException e) {

                     logger.info("Input not available. Please make sure to provide valid input.");
                    }
                }


                else if (option == 2) {

                    int customerUniqueId = customer.getId();
                    String filePath = "src/main/resources/back1/order.txt";
                   logger.info("CustomerName  email OrderID productName number of Products:");

                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(" ");

                            if (parts.length > 0) {
                                try {
                                    int uniqueCustomerIdInFile = Integer.parseInt(parts[0]);

                                    if (customerUniqueId == uniqueCustomerIdInFile) {
                                        System.out.println(line);
                                    }
                                } catch (NumberFormatException e) {
                                    // Handle the case where the first part is not a valid integer (e.g., empty string)
                                }
                            }
                        }
                    } catch (IOException e) {
                        //handle
                    }
                }




                else if (option == 3) {
                    logger.info(() -> String.valueOf(customer));

                }
                else if (option == 4) {
                    men();
                    int y = in.nextInt();
                    update(y);

                } else if (option == 6) {
                    break;
                }


                else if(option==5) {

                  logger.info("****************************************************Installation Appointments**********************************************************");
                   logger.info("ProductName  NamePicture  State  " + " Category"
                            +
                            "Coast \t\t\t\t\t" +
                            " ");

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
                             logger.info("error while reading " + e.getMessage());
                            }
                        }

                else if(option==7) {

                    System.err.println("Enter The Product Name to install ");
                    Scanner scanner = new Scanner(System.in);
                    String product = scanner.nextLine();

                  logger.info("Enter The Make of Your Car  ");
                    String make = scanner.nextLine();

                 logger.info("Enter The Date you want  ");
                    String date = scanner.nextLine();
                 LocalDate  newDate =   LocalDate.parse(date);

                   logger.info("****************************************************Installation Appointments**********************************************************");
                   logger.info("InstallerID        InstallerName          State          Date          Time ")  ;


                    String filePath = "src/main/resources/back1/Installation.txt";

                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",");

                            if (parts.length > 0) {
                                try {

                                   LocalDate installationDate = LocalDate.parse(parts[3]);
                                   String  available =parts[2];

                                    if (newDate.equals(installationDate) && available.equals("available")) {



                                    logger.info( parts[0] +"  "+"  " + parts[1] +"  "+"  "+ parts[2]  +"    " + parts[3] +"    " + parts[4] +"    " + parts[5]);


                                    }



                                } catch  (DateTimeParseException | NumberFormatException e)  {
                                    // Handle the case where the first part is not a valid integer (e.g., empty string)
                                }
                            }
                        }

                    } catch (IOException e) {
                        //handle
                    }

                        System.err.println("Choice The installation Number you want to installer  ");
                        String id = scanner.nextLine();
                        int newId = Integer.parseInt(id);
                        searchInsallation(newId, newDate, product, make);
                        // addInsallation(newId,product,make,newDate,time);}



                }





            menu();
                option = in.nextInt();

        }
    }
        catch (InputMismatchException e){
            logger.info("please enter a valid number ");
        }
    }







    public void men(){
    logger.info("If you want to update your Phone enter number 1");
     logger.info("If you want to update your Address enter number 2");
       logger.info("If you want to update your Password enter number 3");
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setInstaller(InstallerAvailable worker) {
        this.worker = worker;
    }

    public void updateMsg() {
      logger.info("Your Information Updated Successfully");
    }

    public void updateInfo(String attribute, String value) {
        if (attribute.equalsIgnoreCase("address")) {
            customer.setAddress(value);
        } else if (attribute.equalsIgnoreCase("password")) {
            customer.setPassword(value);
        } else if (attribute.equalsIgnoreCase("phone")) {
            customer.setPhone(value);
        }


    }


    public void searchInsallation( int id,LocalDate date,String product, String make ) {

        String path = "src/main/resources/back1/";
        String fileName = path + "Installation.txt";
        String currentContent = readFromFile(fileName);
        String[] lines = currentContent.split("\n");
      //  String available = "not-available";

        for (String line : lines) {
            String[] parts = line.split(",");

            if (parts.length > 0) {
                try {
                    int currentId = Integer.parseInt(parts[0].trim());
                    String currentAvailable = parts[2].trim();
                    LocalDate installationDate = LocalDate.parse(parts[3].trim());

                    if (currentId == id && currentAvailable.equals("available") && date.equals(installationDate)) {
                        LocalTime time = LocalTime.parse(parts[4]);

                        String timing = parts[5];
                        addInsallation(id,product,make,date,time,timing);
                    }
                } catch (NumberFormatException | DateTimeParseException e) {
                    // Handle the case where parsing fails
                   logger.info("Error parsing id or date: " + e.getMessage());
                }
            }
        }

    }




    public void addInsallation( int id,String product, String make,LocalDate date,LocalTime time,String timing ){

        String path = "src/main/resources/back1/";
        String fileName = path + "InstallationApointments.txt";



        String  customerName = customer.getFullName();
        String currentContent = readFromFil(fileName);

            String newContent = currentContent +id + "," +product + "," + make + "," + customerName+ ","+date + "," +time + "," +timing + "\n";
            writeToFil(fileName, newContent);

            System.out.println("Done. An email has been sent to you to confirm your order\n ");
       updateState(id, date);



    }

    public void updateState(int id, LocalDate date) {
        String path = "src/main/resources/back1/";
        String fileName = path + "Installation.txt";
        String currentContent = readFromFile(fileName);
        String[] lines = currentContent.split("\n");
        String available = "not-available";

        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(",");

            if (parts.length > 0) {
                try {
                    int currentId = Integer.parseInt(parts[0].trim());
                    String currentAvailable = parts[2].trim();
                    LocalDate installationDate = LocalDate.parse(parts[3].trim());

                    if (currentId == id && currentAvailable.equals("available") && date.equals(installationDate)) {
                        lines[i] = lines[i].replace("available", available);
                        notifyCustomer(customer);
                    }
                } catch (NumberFormatException | DateTimeParseException e) {
                    // Handle the case where parsing fails
                    System.err.println("Error parsing id or date: " + e.getMessage());
                }
            }
        }

        // Rebuild the content
        String updatedContent = String.join("\n", lines);
        writeToFile(fileName, updatedContent);
    }


    private String readFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return content.toString();
    }

    private void writeToFile(String fileName, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }






    public void update(int y){
    String attribute = "";
    String  value= "";

                    if (y == 1) {
        System.err.println("Enter new Phone number");
        try {
            Scanner i = new Scanner(System.in);
            if (i.hasNextLine()) {
                value = i.nextLine();
                attribute = "phone";
            } else {
                System.err.println("No input available. Please provide valid input.");
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error while reading input: " + e.getMessage());
        }
        updateMsg();
        System.err.println("Done");
    }



                    else if (y == 2) {
        System.err.println("Enter New Address");
        try {   Scanner i = new Scanner(System.in);
            value = i.nextLine();
            attribute = "address";
        } catch (NoSuchElementException e) {

            System.err.println("Input not available. Please make sure to provide valid input.");
        }updateMsg();
        System.err.println("Done");
    }
                    else if (y == 3) {
        try{ System.err.println("Enter new Password");
            Scanner i = new Scanner(System.in);
            value = i.nextLine();
            attribute = "password";

        } catch (NoSuchElementException e) {

            System.err.println("Input not available. Please make sure to provide valid input.");
        } updateMsg();
        System.err.println("Done");

    }

    updateInfo(attribute, value);}


    public void notifyCustomer(Customer customer) {
        customer.sendEmail("Your request has been installed",
                "Hello Mr/Ms "+customer.getFullName()+", you can take it after 3 day",
                "");

    }


}
