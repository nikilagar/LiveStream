/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
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
    
    public void selectQuery (String query) {
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
    
    public static ArrayList<FriendInformation> fetchBuddyList(int id) {
        Connection con = null ;
        Statement st = null ;
        ResultSet rs = null ;
        ArrayList<FriendInformation> info = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            String query = "SELECT fr_id,frusername,frname FROM friends WHERE id = '" + id + "';";
            rs = st.executeQuery(query);
            for (int i=0 ; rs.next() ; i++ ) {
                FriendInformation f = new FriendInformation();
                f.id = rs.getInt("fr_id");
                f.name = rs.getString("frname");
                f.username = rs.getString("frusername");
                info.add(f);
            }
            // Free up resources
            rs.close();
            con.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }
    
    public static ArrayList<FriendInformation> searchUser(String username) {
        Connection con = null ;
        Statement st = null ;
        ResultSet rs = null ;
        ArrayList<FriendInformation> info = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            String query = "SELECT id,username,name FROM Users WHERE username = '" + username + "';";
            rs = st.executeQuery(query);
            for (int i=0 ; rs.next() ; i++ ) {
                FriendInformation f = new FriendInformation();
                f.id = rs.getInt("id");
                f.name = rs.getString("name");
                f.username = rs.getString("username");
                info.add(f);
            }
            // Free up resources
            rs.close();
            con.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }
    
    public int fetchID (String uname) {
        Connection con = null ;
        Statement st = null ;
        ResultSet rs = null ;
        int id = 0;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            String query = "SELECT id FROM Users WHERE username='" + uname + "';";
            System.out.println(query);
            rs = st.executeQuery(query);
            if(rs.next()) {
                id = rs.getInt("id");
            }
            // Free up resources
            rs.close();
            con.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return id;
    }
    
    public ClientInformation getInfo (String uname) {
        Connection con = null ;
        Statement st = null ;
        ResultSet rs = null ;
        ClientInformation info = null ;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            String query = "SELECT * FROM Users WHERE username='" + uname + "';";
            rs = st.executeQuery(query);
            try {
                info = new ClientInformation(rs.getInt("id"), 
                        rs.getString("username"), 
                        rs.getString("name"), 
                        rs.getString("gender"), 
                        new Socket(Client.SERVERIP,1234));
            } catch (IOException ex) {
                Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Free up resources
            con.close();
            rs.close();
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
    
    public static void addFriend(int fromid , int toid) {
        Connection con = null ;
        Statement st = null ;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_PATH,USER,PASS);
            st = con.createStatement();
            FriendInformation from = new FriendInformation(), to = new FriendInformation();
            String query1 = "SELECT id,username,name FROM Users WHERE id = " + fromid + ";";
            rs = st.executeQuery(query1);
            if(rs.next()) {
                from.id = rs.getInt("id");
                from.name = rs.getString("name");
                from.username = rs.getString("username");
            }
            String query2 = "SELECT id,username,name FROM Users WHERE id = " + toid + ";";
            rs = st.executeQuery(query2);
            if(rs.next()) {
                to.id = rs.getInt("id");
                to.name = rs.getString("name");
                to.username = rs.getString("username");
            }
                
            String query3 = "INSERT INTO friends "
                    + "(id,fr_id,username,frusername,name,frname) "
                    + "VALUES (" + fromid
                    + "," + toid
                    + ",\"" + from.username
                    + "\",\"" + to.username
                    + "\",\"" + from.name
                    + "\",\"" + to.name
                    + "\") ;";
            
            String query4 = "INSERT INTO friends "
                    + "(id,fr_id,username,frusername,name,frname) "
                    + "VALUES (" + toid
                    + "," + fromid
                    + ",\"" + to.username
                    + "\",\"" + from.username
                    + "\",\"" + to.name
                    + "\",\"" + from.name
                    + "\") ;";
            st.executeUpdate(query3);
            st.executeUpdate(query4);
            // Free up resources
            con.close();
            st.close();
            rs.close();
            
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