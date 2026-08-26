/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Haidar
 */
public class DatabaseConnection {
    
    private static final String URL = System.getenv("DB_URL");

    private static final String USER = System.getenv("DB_USER");

    private static final String PASSWORD = System.getenv("DB_PASSWORD");


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
