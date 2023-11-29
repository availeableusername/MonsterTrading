package at.technikum.apps.mtcg.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/mtcg"; //mtcg = name in db \c mtcg -> table users;
    //steps on laptop = CREATE DATABASE mtcg; \c mtgc, create table users(id varchar(50), name varchar(50)), profit
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
