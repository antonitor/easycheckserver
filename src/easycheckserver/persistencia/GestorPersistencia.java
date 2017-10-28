/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.persistencia;

import easycheckserver.persistencia.DbContract.TaulaReserva;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Toni
 */
public class GestorPersistencia {

    private Connection conn = null;
    private Driver driver = null;
    private final String URL = "jdbc:postgresql://localhost:5432/easycheck";
    private final String USER = "marcarniuser";
    private final String PASS = "marcarnipass";

    public GestorPersistencia() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Controller Not Found!!");
        }

    }

    public void open() {
        try {
            driver = DriverManager.getDriver(URL);
            Properties properties = new Properties();
            properties.setProperty("user", USER);
            properties.setProperty("password", PASS);
            conn = driver.connect(URL, properties);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
        }
    }

    public void getReserves() throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaReserva.NOM_TAULA);
            rs = stm.executeQuery();
        } finally {
            closeStatement(stm);
        }
        
        while (rs.next()) {
            int id = rs.getInt(TaulaReserva.ID);
            //en construcción
        }
        
    }

    public void closeStatement(PreparedStatement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
        }
    }
    

}
