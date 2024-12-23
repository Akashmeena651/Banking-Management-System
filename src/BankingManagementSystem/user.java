package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class user {

    private Connection connection;

    private Scanner scanner;

    public user(Connection connection,Scanner scanner)
    {
        this.connection=connection;

        this.scanner=scanner;
    }

    public void register()
    {       scanner.nextLine();
            System.out.println("Enter email address: ");
            String email=scanner.nextLine();
            System.out.println("Enter Full Name: ");
            String fullname= scanner.nextLine();
            System.out.println("Enter password: ");
            String password=scanner.nextLine();

            try {
                PreparedStatement userpreparedstatement = connection.prepareStatement("Select * from user where email=?");
                userpreparedstatement.setString(1, email);
                ResultSet rs = userpreparedstatement.executeQuery();
                if (rs.next()) {

                    System.out.println("User already exist");
                }
                else
                {

                    String query = "INSERT INTO user(full_name,email,password) VALUES(?, ?, ?)";
                    try {

                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, fullname);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, password);
                        int affectedrow = preparedStatement.executeUpdate();
                        if (affectedrow > 0) {
                            System.out.println("User Register Successfully");
                            return;
                        } else {
                            System.out.println("Failed to Register User");
                            return;
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }catch (SQLException e)
            {
                e.printStackTrace();
            }

    }




    public String userlogin()
    {   scanner.nextLine();
        System.out.println("Enter Email Address: ");
        String email = scanner.nextLine();
        System.out.println("Enter Passowrd: ");
        String password = scanner.nextLine();


        if(userexist(email))
        {
            String query = "Select * from user where email=? and password=?";
            try{

               PreparedStatement preparedStatement= connection.prepareStatement(query);
               preparedStatement.setString(1,email);
               preparedStatement.setString(2,password);
               ResultSet rs = preparedStatement.executeQuery();
               if(rs.next())
               {

                   return email;
               }
               else {
                   return null;
               }

            }catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        else {
            System.out.println("User Doesn't Exist!!");
        }

        return null;

    }






    public boolean userexist(String email)
    {
        String query="Select * from user where email=?";
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



}
