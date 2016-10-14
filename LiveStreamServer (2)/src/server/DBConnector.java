/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author vikrant
 */
public class DBConnector {
    // Database info
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_PATH = "jdbc:mysql://172.31.78.96/users";
    
    // Credentials
    private static final String USER = "root";
    private static final String PASS = "chunnu123";
    
    public static void selectQuery (String query) {
        Connection con = null ;
        Statement st = null ;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            
            // Free up resources
            con.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    public static ClientInformation getInfo (int id,Socket sock) {
        Connection con = null ;
        Statement st = null ;
        ResultSet rs = null ;
        ClientInformation info = null ;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            String query = "SELECT * FROM Users WHERE id=" + id + ";";
            rs = st.executeQuery(query);
            if(rs.next()) {
                info = new ClientInformation(rs.getInt("id"), 
                        rs.getString("username"), 
                        rs.getString("name"), 
                        rs.getString("gender"),sock);
            }
            // Free up resources
            con.close();
            st.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;
        
    }
     
    public void insertNewUser (String uname,String pass, String name, String gender) {
        Connection con = null ;
        Statement st = null ;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            String query = "INSERT INTO Users "
                    + "(username,password,name,gender) "
                    + "VALUES (\"" + uname
                    + "\",\"" + pass
                    + "\",\"" + name
                    + "\",\"" + gender
                    + "\") ;";
            System.out.println(query);
            st.executeUpdate(query);
            // Free up resources
            con.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkUserExistence (String username,String password) {
        Connection con ;
        Statement st ;
        ResultSet rs ;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            rs = st.executeQuery("SELECT password FROM Users WHERE username='" + username + "'");
            if (rs.first()) {
                String pass = rs.getString("password");
                if ( pass == null ? password == null : pass.equals(password) ) {
                    con.close();
                    rs.close();
                    st.close(); 
                    return true;
                } else {
                    con.close();
                    rs.close();
                    st.close(); 
                    return false;
                }
            } else {
                con.close();
                rs.close();
                st.close(); 
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
             Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, e);            
        }
        return true;
    }
    
    public void updateQuery(String query) {
        Connection con = null ;
        Statement st = null ;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            st.executeQuery(query);
            // Free up resources
            con.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteQuery (String query) {
        Connection con = null ;
        Statement st = null ;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            st.executeQuery(query);
            // Free up resources
            con.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}