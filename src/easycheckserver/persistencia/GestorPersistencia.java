/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.persistencia;

import easycheckserver.model.Reserva;
import easycheckserver.model.Servei;
import easycheckserver.model.Treballador;
import easycheckserver.persistencia.DbContract.TaulaReserva;
import easycheckserver.persistencia.DbContract.TaulaServeis;
import easycheckserver.persistencia.DbContract.TaulaTreballador;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println(ex.getErrorCode() + ": "+ ex.getMessage());
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": "+ ex.getMessage());
        }
    }

    public Treballador getTreballadorId(int id) {
        Treballador treballador = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaTreballador.NOM_TAULA + " WHERE _id="+id);
            rs = stm.executeQuery();
            while (rs.next()) {
                String nom = rs.getString(TaulaTreballador.NOM);
                String cognom1 = rs.getString(TaulaTreballador.COGNOM1);
                String cognom2 = rs.getString(TaulaTreballador.COGNOM2);
                String login = rs.getString(TaulaTreballador.LOGIN);
                String password = rs.getString(TaulaTreballador.PASSWORD);
                int esAdmin = rs.getInt(TaulaTreballador.ADMIN);
                treballador = new Treballador(id, nom, cognom1, cognom2, login, password, esAdmin, getServeisTreballador(id));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": "+ ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return treballador;
    }
    
    public List<Servei> getServeisTreballador(int idTreballador) {
        List<Servei> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.ID_TREBALLADOR + "=" +idTreballador);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaServeis.ID);
                String descripcio = rs.getString(TaulaServeis.DESCRIPCIO);
                String data_servei = rs.getString(TaulaServeis.DATASERVEI);
                String hora_inici = rs.getString(TaulaServeis.HORAINICI);
                String hora_final = rs.getString(TaulaServeis.HORAFINAL);                
                llista.add(new Servei(id, descripcio, idTreballador, data_servei, hora_inici, hora_final, getReservesServei(id)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": "+ ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }  

    public List<Reserva> getReservesServei(int idServei) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaReserva.NOM_TAULA + " WHERE " + TaulaReserva.IDSERVEI + "=" + idServei);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaReserva.NOM);
                String cognom1 = rs.getString(TaulaReserva.COGNOM1);
                String cognom2 = rs.getString(TaulaReserva.COGNOM2);
                String telf = rs.getString(TaulaReserva.TELF);
                String email = rs.getString(TaulaReserva.EMAIL);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                int checkin  = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaReserva.DNI);
                llista.add(new Reserva(id, idServei, loc, data_reserva, nom, cognom1, cognom2, telf, email, qrcode, dni, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": "+ ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public void closeStatement(PreparedStatement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

}
