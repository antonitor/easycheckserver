/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.persistencia;

import easycheckserver.model.Client;
import easycheckserver.model.Reserva;
import easycheckserver.model.Servei;
import easycheckserver.model.Treballador;
import easycheckserver.persistencia.DbContract.TaulaClient;
import easycheckserver.persistencia.DbContract.TaulaReserva;
import easycheckserver.persistencia.DbContract.TaulaServeis;
import easycheckserver.persistencia.DbContract.TaulaTreballador;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private Object TaulaServei;

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
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        }
    }

    public Treballador getTreballadorId(int id) {
        Treballador treballador = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaTreballador.NOM_TAULA + " WHERE _id=" + id);
            rs = stm.executeQuery();
            while (rs.next()) {
                String nom = rs.getString(TaulaTreballador.NOM);
                String cognom1 = rs.getString(TaulaTreballador.COGNOM1);
                String cognom2 = rs.getString(TaulaTreballador.COGNOM2);
                String dni = rs.getString(TaulaTreballador.DNI);
                String login = rs.getString(TaulaTreballador.LOGIN);
                String password = rs.getString(TaulaTreballador.PASSWORD);
                int esAdmin = rs.getInt(TaulaTreballador.ADMIN);
                treballador = new Treballador(id, nom, cognom1, cognom2, dni, login, password, esAdmin, getServeisTreballador(id));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return treballador;
    }

    public List<Treballador> getTreballadors() {
        List<Treballador> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaTreballador.NOM_TAULA);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaTreballador.ID);
                String nom = rs.getString(TaulaTreballador.NOM);
                String cognom1 = rs.getString(TaulaTreballador.COGNOM1);
                String cognom2 = rs.getString(TaulaTreballador.COGNOM2);
                String dni = rs.getString(TaulaTreballador.DNI);
                String login = rs.getString(TaulaTreballador.LOGIN);
                String password = rs.getString(TaulaTreballador.PASSWORD);
                int esAdmin = rs.getInt(TaulaTreballador.ADMIN);
                //Mai ha de tornar el SuperAdmin
                if (id != 1) {
                    llista.add(new Treballador(id, nom, cognom1, cognom2, dni, login, password, esAdmin, getServeisTreballador(id)));
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
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
            stm = conn.prepareStatement("SELECT "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.ID + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.QRCODE + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DATARESERVA + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.LOCALITZADOR + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.NOM + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM1 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM2 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.TELF + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.EMAIL + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaClient.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDCLIENT + " = " + TaulaClient.NOM_TAULA + "." + TaulaClient.ID
                    + " WHERE " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + "=" + idServei);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaClient.NOM);
                String cognom1 = rs.getString(TaulaClient.COGNOM1);
                String cognom2 = rs.getString(TaulaClient.COGNOM2);
                String telf = rs.getString(TaulaClient.TELF);
                String email = rs.getString(TaulaClient.EMAIL);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaClient.DNI);
                Client client = new Client(nom, cognom1, cognom2, telf, email, dni);
                llista.add(new Reserva(id, idServei, client, loc, data_reserva,  qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Reserva> getReserves() {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs;
        try {
            stm = conn.prepareStatement("SELECT "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.ID + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.QRCODE + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DATARESERVA + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.LOCALITZADOR + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.NOM + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM1 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM2 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.TELF + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.EMAIL + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaClient.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDCLIENT + " = " + TaulaClient.NOM_TAULA + "." + TaulaClient.ID);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaClient.NOM);
                String cognom1 = rs.getString(TaulaClient.COGNOM1);
                String cognom2 = rs.getString(TaulaClient.COGNOM2);
                String telf = rs.getString(TaulaClient.TELF);
                String email = rs.getString(TaulaClient.EMAIL);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaClient.DNI);
                Client client = new Client(nom, cognom1, cognom2, telf, email, dni);
                llista.add(new Reserva(id, idServei, client, loc, data_reserva,  qrcode, checkin));            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
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
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Reserva> getReservesQRCode(String qrcode) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs;
        try {
            stm = conn.prepareStatement("SELECT "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.ID + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.QRCODE + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DATARESERVA + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.LOCALITZADOR + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.NOM + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM1 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM2 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.TELF + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.EMAIL + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaClient.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDCLIENT + " = " + TaulaClient.NOM_TAULA + "." + TaulaClient.ID
                    + " WHERE " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.QRCODE + " LIKE '" + qrcode + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaClient.NOM);
                String cognom1 = rs.getString(TaulaClient.COGNOM1);
                String cognom2 = rs.getString(TaulaClient.COGNOM2);
                String telf = rs.getString(TaulaClient.TELF);
                String email = rs.getString(TaulaClient.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaClient.DNI);
                Client client = new Client(nom, cognom1, cognom2, telf, email, dni);
                llista.add(new Reserva(id, idServei, client, loc, data_reserva,  qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Reserva> getReservesLoc(String loc) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs;
        try {
            stm = conn.prepareStatement("SELECT "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.ID + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.QRCODE + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DATARESERVA + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.LOCALITZADOR + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.NOM + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM1 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM2 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.TELF + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.EMAIL + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaClient.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDCLIENT + " = " + TaulaClient.NOM_TAULA + "." + TaulaClient.ID
                    + " WHERE " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.LOCALITZADOR + " LIKE '" + loc + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaClient.NOM);
                String cognom1 = rs.getString(TaulaClient.COGNOM1);
                String cognom2 = rs.getString(TaulaClient.COGNOM2);
                String telf = rs.getString(TaulaClient.TELF);
                String email = rs.getString(TaulaClient.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaClient.DNI);
                Client client = new Client(nom, cognom1, cognom2, telf, email, dni);
                llista.add(new Reserva(id, idServei, client, loc, data_reserva,  qrcode, checkin));            
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Reserva> getReservesDni(String dni) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs;
        try {
            stm = conn.prepareStatement("SELECT "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.ID + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.QRCODE + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DATARESERVA + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.LOCALITZADOR + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.NOM + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM1 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM2 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.TELF + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.EMAIL + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaClient.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDCLIENT + " = " + TaulaClient.NOM_TAULA + "." + TaulaClient.ID
                    + " WHERE " + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI + " LIKE '" + dni + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaClient.NOM);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String cognom1 = rs.getString(TaulaClient.COGNOM1);
                String cognom2 = rs.getString(TaulaClient.COGNOM2);
                String telf = rs.getString(TaulaClient.TELF);
                String email = rs.getString(TaulaClient.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                Client client = new Client(nom, cognom1, cognom2, telf, email, dni);
                llista.add(new Reserva(id, idServei, client, loc, data_reserva,  qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Reserva> getReservesData(String data) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs;
        try {
            stm = conn.prepareStatement("SELECT "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.ID + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.QRCODE + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DATARESERVA + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.LOCALITZADOR + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.NOM + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM1 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM2 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.TELF + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.EMAIL + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaClient.NOM_TAULA 
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDCLIENT + " = " + TaulaClient.NOM_TAULA + "." + TaulaClient.ID
                    + " LEFT JOIN " + TaulaServeis.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + " = " + TaulaServeis.NOM_TAULA + "." + TaulaServeis.ID
                    + " WHERE " + TaulaServeis.NOM_TAULA + "." + TaulaServeis.DATASERVEI + " LIKE " + "'" + data + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaClient.NOM);
                String cognom1 = rs.getString(TaulaClient.COGNOM1);
                String cognom2 = rs.getString(TaulaClient.COGNOM2);
                String telf = rs.getString(TaulaClient.TELF);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String email = rs.getString(TaulaClient.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaClient.DNI);
                Client client = new Client(nom, cognom1, cognom2, telf, email, dni);
                llista.add(new Reserva(id, idServei, client, loc, data_reserva,  qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println("ERROR CODE: " + ex.getErrorCode() + ": " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    //
    public List<Reserva> getReservesDniData(String dni, String data) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs;
        try {
            stm = conn.prepareStatement("SELECT "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.ID + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.QRCODE + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DATARESERVA + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.LOCALITZADOR + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.NOM + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM1 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.COGNOM2 + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.TELF + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.EMAIL + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaClient.NOM_TAULA 
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDCLIENT + " = " + TaulaClient.NOM_TAULA + "." + TaulaClient.ID
                    + " LEFT JOIN " + TaulaServeis.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + " = " + TaulaServeis.NOM_TAULA + "." + TaulaServeis.ID
                    + " WHERE " + TaulaServeis.NOM_TAULA + "." + TaulaServeis.DATASERVEI + " = '" + data + "'"
                    + " AND " + TaulaClient.NOM_TAULA + "." + TaulaClient.DNI + " = '" + dni + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String nom = rs.getString(TaulaClient.NOM);
                String cognom1 = rs.getString(TaulaClient.COGNOM1);
                String cognom2 = rs.getString(TaulaClient.COGNOM2);
                String telf = rs.getString(TaulaClient.TELF);
                String email = rs.getString(TaulaClient.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                Client client = new Client(nom, cognom1, cognom2, telf, email, dni);
                llista.add(new Reserva(id, idServei, client, loc, data_reserva,  qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Servei> getServeis() {
        List<Servei> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaServeis.ID);
                int idTreballador = rs.getInt(TaulaServeis.ID_TREBALLADOR);
                String descripcio = rs.getString(TaulaServeis.DESCRIPCIO);
                String data_servei = rs.getString(TaulaServeis.DATASERVEI);
                String hora_inici = rs.getString(TaulaServeis.HORAINICI);
                String hora_final = rs.getString(TaulaServeis.HORAFINAL);
                llista.add(new Servei(id, descripcio, idTreballador, data_servei, hora_inici, hora_final, getReservesServei(id)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Servei> getServeisTreballador(int idTreballador) {
        List<Servei> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.ID_TREBALLADOR + "=" + idTreballador);
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
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Servei> getServeisData(String data) {
        List<Servei> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.DATASERVEI + " = '" + data + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaServeis.ID);
                int idTreballador = rs.getInt(TaulaServeis.ID_TREBALLADOR);
                String descripcio = rs.getString(TaulaServeis.DESCRIPCIO);
                String data_servei = rs.getString(TaulaServeis.DATASERVEI);
                String hora_inici = rs.getString(TaulaServeis.HORAINICI);
                String hora_final = rs.getString(TaulaServeis.HORAFINAL);
                llista.add(new Servei(id, descripcio, idTreballador, data_servei, hora_inici, hora_final, getReservesServei(id)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Servei> getServeisDataHora(String data, String hora) {
        List<Servei> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.DATASERVEI + "= '" + data + "' AND " + TaulaServeis.HORAINICI + " = '" + hora + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaServeis.ID);
                int idTreballador = rs.getInt(TaulaServeis.ID_TREBALLADOR);
                String descripcio = rs.getString(TaulaServeis.DESCRIPCIO);
                String data_servei = rs.getString(TaulaServeis.DATASERVEI);
                String hora_inici = rs.getString(TaulaServeis.HORAINICI);
                String hora_final = rs.getString(TaulaServeis.HORAFINAL);
                llista.add(new Servei(id, descripcio, idTreballador, data_servei, hora_inici, hora_final, getReservesServei(id)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Servei> getServeisTreballadorData(int idTreballador, String data) {
        List<Servei> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.DATASERVEI + "='" + data + "' AND " + TaulaServeis.ID_TREBALLADOR + " = " + idTreballador);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaServeis.ID);
                String descripcio = rs.getString(TaulaServeis.DESCRIPCIO);
                String hora_inici = rs.getString(TaulaServeis.HORAINICI);
                String hora_final = rs.getString(TaulaServeis.HORAFINAL);
                llista.add(new Servei(id, descripcio, idTreballador, data, hora_inici, hora_final, getReservesServei(id)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Servei> getServeisTreballadorDataHora(int idTreballador, String data, String hora) {
        List<Servei> llista = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.DATASERVEI + "='" + data + "' AND " + TaulaServeis.HORAINICI + " = '" + hora + "' AND " + TaulaServeis.ID_TREBALLADOR + " = " + idTreballador);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaServeis.ID);
                String descripcio = rs.getString(TaulaServeis.DESCRIPCIO);
                String hora_final = rs.getString(TaulaServeis.HORAFINAL);
                llista.add(new Servei(id, descripcio, idTreballador, data, hora, hora_final, getReservesServei(id)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public int insertTreballador(String nom, String cognom1, String cognom2, String dni, String admin, String login, String password) {
        open();
        System.out.println("INSERINT TREBaLLADOR " + nom);
        int rowsUpdated = 0;
        Statement stm = null;
        String insertQuery = "INSERT INTO " + TaulaTreballador.NOM_TAULA + "("
                + TaulaTreballador.NOM + ", "
                + TaulaTreballador.COGNOM1 + ", "
                + TaulaTreballador.COGNOM2 + ", "
                + TaulaTreballador.DNI + ", "
                + TaulaTreballador.ADMIN + ", "
                + TaulaTreballador.LOGIN + ", "
                + TaulaTreballador.PASSWORD
                + ") VALUES ('" + nom + "', '" + cognom1 + "', '" + cognom2 + "', '" + dni + "', " + admin + ", '" + login + "', '" + password + "')";

        try {
            stm = conn.createStatement();
            System.out.println(insertQuery);
            rowsUpdated = stm.executeUpdate(insertQuery);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                }
            }
            close();
            System.out.println("Treballadors inserits: " + rowsUpdated);
            return rowsUpdated;
        }
    }

    public int updateTreballador(String id, String nom, String cognom1, String cognom2, String dni, String admin, String login) {
        open();
        int rowsUpdated = 0;
        Statement stm = null;
        String updateSQL = "UPDATE " + TaulaTreballador.NOM_TAULA + " SET "
                + TaulaTreballador.NOM + "='" + nom + "', "
                + TaulaTreballador.COGNOM1 + "='" + cognom1 + "', "
                + TaulaTreballador.COGNOM2 + "='" + cognom2 + "', "
                + TaulaTreballador.DNI + "='" + dni + "', "
                + TaulaTreballador.ADMIN + "='" + admin + "', "
                + TaulaTreballador.LOGIN + "='" + login + "'"
                + " WHERE " + TaulaTreballador.ID + "=" + id;
        try {
            stm = conn.createStatement();
            rowsUpdated = stm.executeUpdate(updateSQL);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                }
            }
            close();
        }
        System.out.println("Treballadors actualitzats: " + rowsUpdated);
        return rowsUpdated;
    }

    public int assignarTreballador(String idServei, String idTreballador) {
        open();
        int rowsUpdated = 0;
        Statement stm = null;
        String updateSQL = "UPDATE " + TaulaServeis.NOM_TAULA + " SET "
                + TaulaServeis.ID_TREBALLADOR + "=" + idTreballador
                + " WHERE " + TaulaServeis.ID + "=" + idServei;
        try {
            stm = conn.createStatement();
            rowsUpdated = stm.executeUpdate(updateSQL);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                }
            }
            close();
        }
        System.out.println("Assignant treballador " + idTreballador + " a servei " + idServei + " -> " + (rowsUpdated >= 1 ? " Correcte! " : " Error!"));
        return rowsUpdated;
    }

    public int borrarTreballador(String idTreballador) {
        open();
        int rowsUpdated = 0;
        Statement stm = null;
        String updateSQL = "DELETE FROM " + TaulaTreballador.NOM_TAULA
                + " WHERE " + TaulaTreballador.ID + "=" + idTreballador;
        try {
            stm = conn.createStatement();
            rowsUpdated = stm.executeUpdate(updateSQL);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                }
            }
            close();
        }
        System.out.println("Borrant treballador: " + idTreballador + " -> " + (rowsUpdated >= 1 ? " Correcte! " : " Error!"));
        return rowsUpdated;
    }

    public int insertServei(String descripcio, String dataServei, String horaInici, String horaFinal, String idTreballador) {
        open();
        System.out.println("INSERINT SERVEI " + descripcio);
        int rowsUpdated = 0;
        Statement stm = null;
        String insertQuery = "INSERT INTO " + TaulaServeis.NOM_TAULA + "("
                + TaulaServeis.DESCRIPCIO + ", "
                + TaulaServeis.DATASERVEI + ", "
                + TaulaServeis.HORAINICI + ", "
                + TaulaServeis.HORAFINAL + ", "
                + TaulaServeis.ID_TREBALLADOR
                + ") VALUES ('" + descripcio + "', '" + dataServei + "', '" + horaInici + "', '" + horaFinal + "', " + idTreballador + ")";

        try {
            stm = conn.createStatement();
            System.out.println(insertQuery);
            rowsUpdated = stm.executeUpdate(insertQuery);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                }
            }
            close();
            System.out.println("Serveis inserits: " + rowsUpdated);
            return rowsUpdated;
        }
    }

    public int updateServei(String id, String descripcio, String dataServei, String horaInici, String horaFinal, String idTreballador) {
        open();
        int rowsUpdated = 0;
        Statement stm = null;
        String updateSQL = "UPDATE " + TaulaServeis.NOM_TAULA + " SET "
                + TaulaServeis.DESCRIPCIO + "='" + descripcio + "', "
                + TaulaServeis.DATASERVEI + "='" + dataServei + "', "
                + TaulaServeis.HORAINICI + "='" + horaInici + "', "
                + TaulaServeis.HORAFINAL + "='" + horaFinal + "', "
                + TaulaServeis.ID_TREBALLADOR + "=" + idTreballador
                + " WHERE " + TaulaServeis.ID + "=" + id;
        try {
            stm = conn.createStatement();
            rowsUpdated = stm.executeUpdate(updateSQL);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                }
            }
            close();
        }
        System.out.println("Serveis actualitzats: " + rowsUpdated);
        return rowsUpdated;
    }

    public int borrarServei(String idServei) {
        open();
        int rowsUpdated = 0;
        Statement stm = null;
        String updateSQL = "DELETE FROM " + TaulaServeis.NOM_TAULA
                + " WHERE " + TaulaServeis.ID + "=" + idServei;
        try {
            stm = conn.createStatement();
            rowsUpdated = stm.executeUpdate(updateSQL);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                }
            }
            close();
        }
        System.out.println("Borrant servei: " + idServei + " -> " + (rowsUpdated >= 1 ? " Correcte! " : " Error!"));
        return rowsUpdated;
    }

    public String login(String user, String password) {
        String response = "0";
        List<Treballador> llista = getTreballadors();
        for (Treballador treb : llista) {
            if (treb.getLogin().equals(user)) {
                response = "1";
                if (treb.getPassword().equals(password)) {
                    response = "2";
                    break;
                }
            }
        }
        return response;
    }

}
