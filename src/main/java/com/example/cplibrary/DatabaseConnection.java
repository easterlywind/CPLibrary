package com.example.cplibrary;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "cplibrary";
        String databaseUserName = "root";
        String databasePassword = "123456";
        String url = "jdbc:mysql://localhost/" + databaseName + "?useSSL=false";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUserName, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }
}
