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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                String login = rs.getString(TaulaTreballador.LOGIN);
                String password = rs.getString(TaulaTreballador.PASSWORD);
                int esAdmin = rs.getInt(TaulaTreballador.ADMIN);
                treballador = new Treballador(id, nom, cognom1, cognom2, login, password, esAdmin, getServeisTreballador(id));
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
                String login = rs.getString(TaulaTreballador.LOGIN);
                String password = rs.getString(TaulaTreballador.PASSWORD);
                int esAdmin = rs.getInt(TaulaTreballador.ADMIN);
                llista.add(new Treballador(id, nom, cognom1, cognom2, login, password, esAdmin, getServeisTreballador(id)));
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
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaReserva.DNI);
                llista.add(new Reserva(id, idServei, loc, data_reserva, nom, cognom1, cognom2, telf, email, qrcode, dni, checkin));
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
            stm = conn.prepareStatement("SELECT * FROM " + TaulaReserva.NOM_TAULA);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaReserva.NOM);
                String cognom1 = rs.getString(TaulaReserva.COGNOM1);
                String cognom2 = rs.getString(TaulaReserva.COGNOM2);
                String telf = rs.getString(TaulaReserva.TELF);
                String email = rs.getString(TaulaReserva.EMAIL);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaReserva.DNI);
                llista.add(new Reserva(id, idServei, loc, data_reserva, nom, cognom1, cognom2, telf, email, qrcode, dni, checkin));
            }

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
            stm = conn.prepareStatement("SELECT * FROM " + TaulaReserva.NOM_TAULA + " WHERE " + TaulaReserva.QRCODE + " LIKE '" + qrcode + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaReserva.NOM);
                String cognom1 = rs.getString(TaulaReserva.COGNOM1);
                String cognom2 = rs.getString(TaulaReserva.COGNOM2);
                String telf = rs.getString(TaulaReserva.TELF);
                String email = rs.getString(TaulaReserva.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaReserva.DNI);
                llista.add(new Reserva(id, idServei, loc, data_reserva, nom, cognom1, cognom2, telf, email, qrcode, dni, checkin));
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
            stm = conn.prepareStatement("SELECT * FROM " + TaulaReserva.NOM_TAULA + " WHERE " + TaulaReserva.LOCALITZADOR + " LIKE '" + loc + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaReserva.NOM);
                String cognom1 = rs.getString(TaulaReserva.COGNOM1);
                String cognom2 = rs.getString(TaulaReserva.COGNOM2);
                String telf = rs.getString(TaulaReserva.TELF);
                String email = rs.getString(TaulaReserva.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaReserva.DNI);
                llista.add(new Reserva(id, idServei, loc, data_reserva, nom, cognom1, cognom2, telf, email, qrcode, dni, checkin));
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
            stm = conn.prepareStatement("SELECT * FROM " + TaulaReserva.NOM_TAULA + " WHERE " + TaulaReserva.DNI + " LIKE '" + dni + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaReserva.NOM);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String cognom1 = rs.getString(TaulaReserva.COGNOM1);
                String cognom2 = rs.getString(TaulaReserva.COGNOM2);
                String telf = rs.getString(TaulaReserva.TELF);
                String email = rs.getString(TaulaReserva.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                llista.add(new Reserva(id, idServei, loc, data_reserva, nom, cognom1, cognom2, telf, email, qrcode, dni, checkin));
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
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.NOM + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.COGNOM1 + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.COGNOM2 + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.TELF + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.EMAIL + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DNI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaServeis.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + " = " + TaulaServeis.NOM_TAULA + "." + TaulaServeis.ID
                    + " WHERE " + TaulaServeis.NOM_TAULA + "." + TaulaServeis.DATASERVEI + " LIKE " + "'" + data + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String nom = rs.getString(TaulaReserva.NOM);
                String cognom1 = rs.getString(TaulaReserva.COGNOM1);
                String cognom2 = rs.getString(TaulaReserva.COGNOM2);
                String telf = rs.getString(TaulaReserva.TELF);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String email = rs.getString(TaulaReserva.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                String dni = rs.getString(TaulaReserva.DNI);
                llista.add(new Reserva(id, idServei, loc, data_reserva, nom, cognom1, cognom2, telf, email, qrcode, dni, checkin));
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
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.NOM + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.COGNOM1 + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.COGNOM2 + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.TELF + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.EMAIL + ", "
                    + TaulaReserva.NOM_TAULA + "." + TaulaReserva.CHECKIN + ", "
                    + TaulaServeis.NOM_TAULA + "." + TaulaServeis.DATASERVEI
                    + " FROM " + TaulaReserva.NOM_TAULA + " LEFT JOIN " + TaulaServeis.NOM_TAULA
                    + " ON " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + " = " + TaulaServeis.NOM_TAULA + "." + TaulaServeis.ID
                    + " WHERE " + TaulaServeis.NOM_TAULA + "." + TaulaServeis.DATASERVEI + " = '" + data + "'"
                    + " AND " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.DNI + " = '" + dni + "'");
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaReserva.ID);
                int idServei = rs.getInt(TaulaReserva.IDSERVEI);
                String qrcode = rs.getString(TaulaReserva.QRCODE);
                String data_reserva = rs.getString(TaulaReserva.DATARESERVA);
                String loc = rs.getString(TaulaReserva.LOCALITZADOR);
                String nom = rs.getString(TaulaReserva.NOM);
                String cognom1 = rs.getString(TaulaReserva.COGNOM1);
                String cognom2 = rs.getString(TaulaReserva.COGNOM2);
                String telf = rs.getString(TaulaReserva.TELF);
                String email = rs.getString(TaulaReserva.EMAIL);
                int checkin = rs.getInt(TaulaReserva.CHECKIN);
                llista.add(new Reserva(id, idServei, loc, data_reserva, nom, cognom1, cognom2, telf, email, qrcode, dni, checkin));
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

    public int insertTreballador(String nom, String cognom1, String cognom2, String admin, String login, String password) {
        open();
        System.out.println("INSERINT TREBaLLADOR " + nom);
        int rowsUpdated = 0;
        Statement stm = null;
        String insertQuery = "INSERT INTO " + TaulaTreballador.NOM_TAULA + "("
                + TaulaTreballador.NOM + ", "
                + TaulaTreballador.COGNOM1 + ", "
                + TaulaTreballador.COGNOM2 + ", "
                + TaulaTreballador.ADMIN + ", "
                + TaulaTreballador.LOGIN + ", "
                + TaulaTreballador.PASSWORD
                + ") VALUES ('" + nom + "', '" + cognom1 + "', '" + cognom2 + "', " + admin + ", '" + login + "', '" + password + "')";

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

    public int updateTreballador(String id, String nom, String cognom1, String cognom2, String admin, String login, String password) {
        open();
        int rowsUpdated = 0;
        Statement stm = null;
        String updateSQL = "UPDATE " + TaulaTreballador.NOM_TAULA + " SET "
                    + TaulaTreballador.NOM + "='" + nom + "'" + ", "
                    + TaulaTreballador.COGNOM1 + "='" + cognom1 + "'" + ", "
                    + TaulaTreballador.COGNOM2 + "='" + cognom2 + "'" + ", "
                    + TaulaTreballador.ADMIN + "='" + admin + "'" + ", "
                    + TaulaTreballador.LOGIN + "='" + login + "'" + ", "
                    + TaulaTreballador.PASSWORD + "='" + password + "'"
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
        return rowsUpdated;    }
}
