package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;

    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {

        this.connection = connection;

        this.scanner = scanner;
    }

    void transferamount(long Sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Account Number: ");
        long receiver_account_number = scanner.nextInt();
        System.out.println("Enter Security pin: ");
        int security_pin = scanner.nextInt();
        System.out.println("Enter Amount to transfer: ");
        double amountsend = scanner.nextDouble();

        if(receiver_account_number!=0 && Sender_account_number!=0){
            connection.setAutoCommit(false);
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1, Sender_account_number);
                preparedStatement.setDouble(2, security_pin);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next())
                {   double curr_balance = rs.getDouble("balance");
                    if(amountsend<=curr_balance)
                    {
                        String debitquery="Update accounts set balance = balance - ? where account_number=?";
                        String creditquery="Update accounts set balance= balance + ? where account_number=?";
                        PreparedStatement creditpreparedstatement= connection.prepareStatement(creditquery);
                        PreparedStatement debitpreparedstatement=connection.prepareStatement(debitquery);
                        creditpreparedstatement.setDouble(1,amountsend);
                        creditpreparedstatement.setLong(2,receiver_account_number);
                        debitpreparedstatement.setDouble(1,amountsend);
                        debitpreparedstatement.setLong(2,Sender_account_number);
                        int debitaffectedrow = debitpreparedstatement.executeUpdate();
                        int creditaffectedrow=creditpreparedstatement.executeUpdate();
                        if(debitaffectedrow>0 && creditaffectedrow>0)
                        {
                            connection.commit();
                            connection.setAutoCommit(true);
                            System.out.println("Transaction Completed Successfully");
                        }
                        else{
                            connection.rollback();
                            connection.setAutoCommit(true);
                            System.out.println("Transaction Failed");
                        }
                    }
                    else {
                        System.out.println("Insufficient Balance");
                    }

                }
                else {
                    System.out.println("Invalid Pin");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Account Doesn't exist");
        }

    }


    double check_balance(long accountnumber) {
        System.out.println("Enter Security pin: ");
        int security_pin = scanner.nextInt();
        String query = "Select balance,account_number from accounts where account_number=? and security_pin=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, accountnumber);
            preparedStatement.setLong(2, security_pin);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                double balance=rs.getDouble("balance");
                System.out.println("Balance Rs "+balance);
                return balance;

            }
            else {
                System.out.println("Invalid pin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to get balance");
    }

    void debitaccount(long accountnumber) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount to debit: ");
        double amountneeded = scanner.nextDouble();
        System.out.println("Enter Security pin: ");
        int security_pin = scanner.nextInt();

        if (accountnumber != 0) {
            connection.setAutoCommit(false);
            try {

                PreparedStatement preparedStatement = connection.prepareStatement("Select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1, accountnumber);
                preparedStatement.setLong(2, security_pin);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    if (amountneeded <= balance) {
                        double remainingamount = balance - amountneeded;
                        String query = "Update accounts set balance=balance - ? where account_number=?";

                        PreparedStatement preparedStatement1 = connection.prepareStatement(query);
                        preparedStatement1.setDouble(1, amountneeded);
                        preparedStatement1.setLong(2, accountnumber);
                        int affectedrow = preparedStatement1.executeUpdate();
                        if (affectedrow>0) {
                            System.out.println("Rs" + amountneeded + "Debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            return;
                        }
                    } else {
                        System.out.println("Insufficient Balance");
                    }
                } else {
                    System.out.println("Invalid pin");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid Account Number");
        }


    }

    void creditaccount(long accountnumber) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount to credit: ");
        double amountneeded = scanner.nextDouble();
        System.out.println("Enter Security pin: ");
        int security_pin = scanner.nextInt();

        if (accountnumber != 0) {
            connection.setAutoCommit(false);
            try {

                PreparedStatement preparedStatement = connection.prepareStatement("Select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1, accountnumber);
                preparedStatement.setInt(2, security_pin);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    double balance = rs.getDouble("balance");

                        double remainingamount = balance + amountneeded;
                        String query = "Update accounts set balance=balance + ? where account_number=?";

                        PreparedStatement preparedStatement1 = connection.prepareStatement(query);
                        preparedStatement1.setDouble(1, amountneeded);
                        preparedStatement1.setLong(2, accountnumber);
                        int affectedrow = preparedStatement1.executeUpdate();
                        if (affectedrow>0) {
                            System.out.println("Rs" + amountneeded + "Credit Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            return;
                        }
                    }
                   else {
                    System.out.println("Invalid pin");
                  }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid Account Number");
        }


    }

}




