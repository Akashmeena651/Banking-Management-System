package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

import static java.lang.Long.getLong;

public class Account {

    private Connection connection;

    private Scanner scanner;

    public Account(Connection connection,Scanner scanner)
    {
        this.connection=connection;

        this.scanner=scanner;
    }



    public boolean account_exist(String email)
    {

        String query="Select * from accounts where email=?";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next())
            {
                return true;
            }
            else {
                return false;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;


    }



    public long generate_account_number()
    {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("Select account_number from accounts order by account_number DESC limit 1");
            if(rs.next())
            {
                long last_account_number = rs.getLong("account_number");
                return last_account_number+1;
            }
            else {

                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 10000100;

    }




    public long openaccount(String email) {

        if (!account_exist(email)) {
            scanner.nextLine();
            System.out.println("Enter Full Name: ");
            String fullname = scanner.nextLine();
            System.out.println("Enter Initial balance: ");
            double balance=scanner.nextFloat();
            System.out.println("Enter Security pin: ");
            int securitypin=scanner.nextInt();
            long accountnumber=generate_account_number();
            String query="INSERT INTO accounts(account_number,full_name, email, balance, security_pin) Values(?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement= connection.prepareStatement(query);
                preparedStatement.setLong(1,accountnumber);
                preparedStatement.setString(2,fullname);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setInt(5,securitypin);
                int affectedrow = preparedStatement.executeUpdate();
                if(affectedrow>0)
                {
                    return accountnumber;
                }
                else {
                    throw new RuntimeException("Account Creation Failed");
                }
            }catch(SQLException e)
            {
              e.printStackTrace();
            }
        }

        throw new RuntimeException("Account already exist");




    }

    public long get_accountnumber(String email)
    {
        String query="Select account_number from accounts where email=?";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next())
            {

                long accountnumber_=rs.getLong("account_number");
                return accountnumber_;

            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        throw new RuntimeException("Account Number Doesn't exist");

    }



}
