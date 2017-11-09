package easycheckserver.persistencia;

import easycheckserver.model.Client;
import easycheckserver.utils.PostResponse;
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
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaTreballador.NOM_TAULA + " WHERE _id = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
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
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaTreballador.NOM_TAULA);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(TaulaTreballador.ID);
                String nom = rs.getString(TaulaTreballador.NOM);
                String cognom1 = rs.getString(TaulaTreballador.COGNOM1);
                String cognom2 = rs.getString(TaulaTreballador.COGNOM2);
                String dni = rs.getString(TaulaTreballador.DNI);
                String login = rs.getString(TaulaTreballador.LOGIN);
                String password = rs.getString(TaulaTreballador.PASSWORD);
                int esAdmin = rs.getInt(TaulaTreballador.ADMIN);
                llista.add(new Treballador(id, nom, cognom1, cognom2, dni, login, password, esAdmin, getServeisTreballador(id)));
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
                    + " WHERE " + TaulaReserva.NOM_TAULA + "." + TaulaReserva.IDSERVEI + " = ?");
            stm.setInt(1, idServei);
            ResultSet rs = stm.executeQuery();
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
                llista.add(new Reserva(id, idServei, client, loc, data_reserva, qrcode, checkin));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Reserva> getReserves() {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
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
            ResultSet rs = stm.executeQuery();
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
                llista.add(new Reserva(id, idServei, client, loc, data_reserva, qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }    

    public List<Reserva> getReservesQRCode(String qrcode) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
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
            ResultSet rs = stm.executeQuery();
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
                llista.add(new Reserva(id, idServei, client, loc, data_reserva, qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Reserva> getReservesLoc(String loc) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
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
            ResultSet rs = stm.executeQuery();
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
                llista.add(new Reserva(id, idServei, client, loc, data_reserva, qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Reserva> getReservesDni(String dni) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
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
            ResultSet rs = stm.executeQuery();
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
                llista.add(new Reserva(id, idServei, client, loc, data_reserva, qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Reserva> getReservesData(String data) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
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
            ResultSet rs = stm.executeQuery();
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
                llista.add(new Reserva(id, idServei, client, loc, data_reserva, qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println("ERROR CODE: " + ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    //
    public List<Reserva> getReservesDniData(String dni, String data) {
        List<Reserva> llista = new ArrayList<>();
        PreparedStatement stm = null;
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
            ResultSet rs = stm.executeQuery();
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
                llista.add(new Reserva(id, idServei, client, loc, data_reserva, qrcode, checkin));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return llista;
    }

    public List<Servei> getServeis() {
        List<Servei> llista = new ArrayList<>();
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA);
            ResultSet rs = stm.executeQuery();
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
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.ID_TREBALLADOR + " = ?");
            stm.setInt(1, idTreballador);
            ResultSet rs = stm.executeQuery();            
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
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.DATASERVEI + " = '" + data + "'");
            ResultSet rs = stm.executeQuery();
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
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.DATASERVEI + "= '" + data + "' AND " + TaulaServeis.HORAINICI + " = '" + hora + "'");
            ResultSet rs = stm.executeQuery();
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
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.DATASERVEI + "='" + data + "' AND " + TaulaServeis.ID_TREBALLADOR + " = " + idTreballador);
            ResultSet rs = stm.executeQuery();
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
        try {
            stm = conn.prepareStatement("SELECT * FROM " + TaulaServeis.NOM_TAULA + " WHERE " + TaulaServeis.DATASERVEI + "='" + data + "' AND " + TaulaServeis.HORAINICI + " = '" + hora + "' AND " + TaulaServeis.ID_TREBALLADOR + " = " + idTreballador);
            ResultSet rs = stm.executeQuery();
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

    public PostResponse insertTreballador(String nom, String cognom1, String cognom2, String dni, String admin, String login, String password) {
        PostResponse response = new PostResponse();
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
            int rowsUpdated = stm.executeUpdate(insertQuery);
            if (rowsUpdated == 1) {
                response.setRequestCode(1);
                response.setMessage("Treballador " + nom + " inserit correctament.");
            } else if (rowsUpdated == 0) {
                response.setRequestCode(0);
                response.setMessage("No s'ha pogut inserir el treballador " + nom);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            response.setRequestCode(0);
            if (ex.getSQLState().equals("23505")) {
                if (ex.getMessage().contains("login")) {
                    response.setMessage("Error al inserir treballador: aquest login ja existeix");
                } else if (ex.getMessage().contains("dni")) {
                    response.setMessage("Error al inserir treballador: ja existeix un treballador amb aquest dni");
                } else {
                    response.setMessage(ex.getMessage());
                }
            } else {
                response.setMessage(ex.getMessage());
            }
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    public PostResponse updateTreballador(String id, String nom, String cognom1, String cognom2, String dni, String admin, String login, String pass) {
        if (id.equals("1")) {
            return new PostResponse(0, "No es pot modificar el usuari Administrador");
        }
        PostResponse response = new PostResponse();
        Statement stm = null;
        String updateSQL = "UPDATE " + TaulaTreballador.NOM_TAULA + " SET "
                + TaulaTreballador.NOM + "='" + nom + "', "
                + TaulaTreballador.COGNOM1 + "='" + cognom1 + "', "
                + TaulaTreballador.COGNOM2 + "='" + cognom2 + "', "
                + TaulaTreballador.DNI + "='" + dni + "', "
                + TaulaTreballador.ADMIN + "='" + admin + "', "
                + TaulaTreballador.LOGIN + "='" + login + "', "
                + TaulaTreballador.PASSWORD + "='" + pass + "'"
                + " WHERE " + TaulaTreballador.ID + "=" + id;
        try {
            stm = conn.createStatement();
            int rowsUpdated = stm.executeUpdate(updateSQL);
            if (rowsUpdated == 1) {
                response.setRequestCode(1);
                response.setMessage("Treballador actualitzat correctament.");
            } else {
                response.setRequestCode(0);
                response.setMessage("No sha pogut actualitzar el treballador.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            response.setRequestCode(0);
            if (ex.getSQLState().equals("23505")) {
                if (ex.getMessage().contains("login")) {
                    response.setMessage("Error al actualitzar treballador: aquest login ja existeix");
                } else if (ex.getMessage().contains("dni")) {
                    response.setMessage("Error al actualitzar treballador: ja existeix un treballador amb aquest dni");
                } else {
                    response.setMessage(ex.getMessage());
                }
            } else {
                response.setMessage(ex.getMessage());
            }
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    public PostResponse assignarTreballador(String idServei, String idTreballador) {
        if (idTreballador.equals("1")) {
            return new PostResponse(0, "No es poden assignar Serveis al usuari Administrador");
        }
        PostResponse response = new PostResponse();
        Statement stm = null;
        String updateSQL = "UPDATE " + TaulaServeis.NOM_TAULA + " SET "
                + TaulaServeis.ID_TREBALLADOR + "=" + idTreballador
                + " WHERE " + TaulaServeis.ID + "=" + idServei;
        try {
            stm = conn.createStatement();
            int rowsUpdated = stm.executeUpdate(updateSQL);
            if (rowsUpdated == 1) {
                response.setRequestCode(1);
                response.setMessage("Treballador " + idTreballador + " assignat al servei " + idServei + " correctament.");
            } else {
                response.setRequestCode(0);
                response.setMessage("No sha pogut assignar el treballador al servei.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            response.setRequestCode(0);
            response.setMessage(ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    public PostResponse borrarTreballador(String idTreballador) {
        PostResponse response = new PostResponse();
        if (idTreballador.equals("1")) {
            response.setRequestCode(0);
            response.setMessage("No es pot esborrar el Administrador.");
            return response;
        }
        Statement stm = null;
        String updateSQL = "DELETE FROM " + TaulaTreballador.NOM_TAULA
                + " WHERE " + TaulaTreballador.ID + "=" + idTreballador;
        try {
            stm = conn.createStatement();
            int rowsUpdated = stm.executeUpdate(updateSQL);
            if (rowsUpdated == 1) {
                response.setRequestCode(1);
                response.setMessage("Treballador esborrat correctament");
            } else {
                response.setRequestCode(0);
                response.setMessage("Aquest treballador no existeix.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            response.setRequestCode(0);
            if (ex.getSQLState().equals("23503")) {
                response.setMessage("No es pot esborrar aquest treballador, té serveis assignats.");
            } else {
                response.setMessage(ex.getMessage());
            }
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    public PostResponse insertServei(String descripcio, String dataServei, String horaInici, String horaFinal, String idTreballador) {
        PostResponse response = new PostResponse();
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
            int rowsUpdated = stm.executeUpdate(insertQuery);
            if (rowsUpdated == 1) {
                response.setRequestCode(1);
                response.setMessage("Servei " + descripcio + " inserit correctament.");
            } else {
                response.setRequestCode(0);
                response.setMessage("Error al crear nou Servei.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            response.setRequestCode(0);
            response.setMessage(ex.getMessage());
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    public PostResponse updateServei(String id, String descripcio, String dataServei, String horaInici, String horaFinal, String idTreballador) {
        PostResponse response = new PostResponse();
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
            int rowsUpdated = stm.executeUpdate(updateSQL);
            if (rowsUpdated == 1) {
                response.setRequestCode(1);
                response.setMessage("Servei " + descripcio + " actualitzat correctament.");
            } else {
                response.setRequestCode(0);
                response.setMessage("Error al actualitzar el Servei.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            response.setRequestCode(0);
            response.setMessage("Error al crear nou Servei.");
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    public PostResponse esborrarServei(String idServei) {
        PostResponse response = new PostResponse();
        open();
        Statement stm = null;
        String updateSQL = "DELETE FROM " + TaulaServeis.NOM_TAULA
                + " WHERE " + TaulaServeis.ID + "=" + idServei;
        try {
            stm = conn.createStatement();
            int rowsUpdated = stm.executeUpdate(updateSQL);
            if (rowsUpdated == 1) {
                response.setRequestCode(1);
                response.setMessage("Servei " + idServei + " esborrat correctament.");
            } else {
                response.setRequestCode(0);
                response.setMessage("Error al borrar el Servei.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
            response.setRequestCode(0);
            if (ex.getSQLState().equals("23503")) {
                response.setMessage("Error al esborrar Servei: te reserves asociades.");
            } else {
                response.setMessage(ex.getMessage());
            }
        } finally {
            closeStatement(stm);
        }
        return response;
    }
    
    public void closeStatement(PreparedStatement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        }
    }
    
    public void closeStatement(Statement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        }
    }

    public PostResponse login(String user, String password) {
        PostResponse response = new PostResponse(0, "Usuari incorrecte.");
        List<Treballador> llista = getTreballadors();
        for (Treballador treb : llista) {
            if (treb.getLogin().equals(user)) {
                response.setMessage("Contrasenya incorrecta.");
                if (treb.getPassword().equals(password)) {
                    response.setRequestCode(1);
                    response.setMessage("" + treb.getId());
                    break;
                }
            }
        }
        return response;
    }

}
