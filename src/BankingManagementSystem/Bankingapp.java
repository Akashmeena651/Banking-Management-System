package BankingManagementSystem;

import java.sql.Connection;
import static java.lang.Class.forName;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Bankingapp {

    private static final String url="jdbc:mysql://localhost:3306/Banking_management_system";

    private static final String username="sql username";

    private static final String password="Add your sql user password";

    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

        Scanner scanner=new Scanner(System.in);

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            user user = new user(connection,scanner);
            Account account = new Account(connection,scanner);
            AccountManager accountmanager= new AccountManager(connection,scanner);

            String email;
            long account_number;
            while(true)
            {
                System.out.print("Welcome to Banking System ");
                System.out.println("1. Register");
                System.out.println("2. login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1=scanner.nextInt();


                switch(choice1)
                {
                    case 1:
                         user.register();
                         break;
                    case 2:
                         email=user.userlogin();
                         if(email!=null) {
                             System.out.println();
                             System.out.println("User logged In");
                             if (!account.account_exist(email)) {
                                 System.out.println();
                                 System.out.println("1. Open a New Bank Account");
                                 System.out.println("2. Exit");
                                 if (scanner.nextInt() == 1) {
                                     account_number = account.openaccount(email);
                                     System.out.println("Account Created Successfully");
                                     System.out.println("Your Account Number is: " + account_number);
                                 } else {
                                     break;
                                 }

                             }
                             account_number = account.get_accountnumber(email);


                             int choice2=1;
                             while (choice2 !=5) {
                                 System.out.println();
                                 System.out.println("1. Debit Money");
                                 System.out.println("2. Credit Money");
                                 System.out.println("3. Transfer Money");
                                 System.out.println("4. CheckBalance");
                                 System.out.println("5. Logout");
                                 System.out.println("Enter your Choice: ");
                                 choice2 = scanner.nextInt();
                                 switch (choice2) {
                                     case 1:
                                         accountmanager.debitaccount(account_number);
                                         break;
                                     case 2:
                                         accountmanager.creditaccount(account_number);
                                         break;
                                     case 3:
                                         accountmanager.transferamount(account_number);
                                         break;
                                     case 4:
                                         accountmanager.check_balance(account_number);
                                         break;
                                     case 5:
                                         break;
                                     default:
                                         System.out.println("Enter Valid Choice: ");
                                         break;
                                 }
                             }
                         }
                         else {
                             System.out.println("Incorrect Email or Password");

                         }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!");
                        System.out.println("Exiting System");
                        break;
                    default:
                        System.out.println("Enter Valid Choice: ");
                        break;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
