/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucas B Tsuchiya
 */
public class ConnectionFactory {
    //private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DRIVER = "org.sqlite.JDBC";
    //private static final String URL = "jdbc:mysql://localhost:3306/arduinojava";
     //class.forName("org.sqlite.JDBC");
    //private static final String URL = "jdbc:sqlite:C:/Users/Lucas B Tsuchiya/Documents/NetBeansProjects/JavaArduino/bd/arduinojava.sqlite";
    private static final String URL = "jdbc:sqlite:arduinojava.sqlite";

    private static final String USER = "root";
    private static final String PASS = "asus2018";
    
    /**
     *
     * 
     * @return
     */
    public static Connection getConnection(){
        try{
            Class.forName(DRIVER);
            //return DriverManager.getConnection(URL, USER, PASS);
            return DriverManager.getConnection(URL);
            
        } catch (ClassNotFoundException | SQLException ex){
            throw new RuntimeException(ex);   
        }       
    }
    
    public static void closeConnection(Connection con){
        if(con != null){
            try{
                con.close();
            }catch (SQLException ex) {
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void closeConnection(Connection con, PreparedStatement stmt){
        if(stmt != null){
            try {
                stmt.close();
            }catch (SQLException ex){
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        closeConnection(con);
    }
    
    public static void closeConnection(Connection con, PreparedStatement stmt, ResultSet rs){
        if(con != null){
            try{
                rs.close();
            }catch (SQLException ex) {
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        closeConnection(con, stmt);
    }
}