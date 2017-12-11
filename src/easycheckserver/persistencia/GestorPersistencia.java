/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
 */
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
import static easycheckserver.utils.NetUtils.stringToInt;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aquesta classe gestiona les crides a la base de dades per tal d'obtenir
 * registres, així como insertar, modificar o eliminar-los.
 *
 * @author Toni
 */
public class GestorPersistencia {

    private Connection conn = null;
    private Driver driver = null;
    private final String URL = "jdbc:postgresql://localhost:5432/easycheck";
    private final String USER = "marcarniuser";
    private final String PASS = "marcarnipass";

    /**
     * Al construir el Gestor de Persistencia comprova que existeix el Driver
     */
    public GestorPersistencia() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Controller Not Found!!");
        }
    }

    /**
     * Obre connexió amb la base de dades
     */
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

    /**
     * Tanca la connexió amb la base de dades
     */
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        }
    }

    /**
     * Busca a la base de dades el registre a la taula treballador amb l'id que
     * li pasem per paràmetre i torna un objecte de la classe Treballador
     *
     * @param id corresponent al camp _id de la taula treballador
     * @return objecte de la classe Treballador
     */
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

    /**
     * Consulta tots els registres de la taula treballador i els torna com una
     * llista d'objectes de la classe Treballador.
     *
     * @return llista d'objectes de la classe Treballador
     */
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

    /**
     * Busca a la taula reserva els registres que corresponen amb el servei del
     * qual li passem l'id per paràmetre, i torna una llista d'objectes de la
     * clase Reserva
     *
     * @param idServei corresponent al camp id_servei de la taula reserva
     * @return llista d'objectes de la classe Reserva
     */
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

    /**
     * Consulta tots els registres de la taula reserva i els torna en forma de
     * llista d'objectes de la clase Reserva
     *
     * @return llista d'objectes de la classe Reserva
     */
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

    /**
     * Busca a la taula reserva els registres amb qrcode corresponent al String
     * que li passem per paràmetre, i els torna en forma d'objectes Reserva
     *
     * @param qrcode corresponent a la columna qrcode de la taula reserva
     * @return llista d'objectes Reserva
     */
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

    /**
     * Busca a la taula reserva els registres amb localitzador corresponent al
     * String que li passem per paràmetre, i els torna en forma d'objectes
     * Reserva
     *
     * @param loc corresponent a la columna localitzador de la taula reserva
     * @return llista d'objectes Reserva
     */
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

    /**
     * Busca a la taula reserva els registres amb dni corresponent al String que
     * li passem per paràmetre, i els torna en forma d'objectes Reserva
     *
     * @param dni corresponent a la columna dni de la taula reserva
     * @return llista d'objectes Reserva
     */
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

    /**
     * Busca a la taula reserva els registres amb data corresponent al String
     * que li passem per paràmetre, i els torna en forma d'objectes Reserva
     *
     * @param data corresponent a la columna data_servei de la taula serveis
     * @return llista d'objectes Reserva
     */
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

    /**
     * Busca a la taula reserva els registres amb dni i data corresponent als
     * Strings que li passem per paràmetre, i els torna en forma d'objectes
     * Reserva
     *
     * @param dni corresponent a la columna dni de la taula reserva
     * @param data corresponent a la columna data_servei de la taula servei
     * @return llista d'objectes Reserva
     */
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

    /**
     * Consulta tots els registres de la taula serveis i els retorna en forma de
     * llista d'objectes Servei.
     *
     * @return llista d'objectes Servei
     */
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

    /**
     * Busca a la taula servei els registres amb id_treballador corresponent al
     * String que li passem per paràmetre, i els torna en forma d'objectes
     * Servei
     *
     * @param idTreballador corresponent amb la columna id_treballador de la
     * taula serveis
     * @return llista d'objectes Servei
     */
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

    /**
     * Busca a la taula servei els registres amb data_servei corresponent al
     * String que li passem per paràmetre, i els torna en forma d'objectes
     * Servei
     *
     * @param data corresponent amb la columna data_servei de la taula serveis
     * @return llista d'objectes Servei
     */
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

    /**
     * Busca a la taula servei els registres amb data_servei i hora_inici
     * corresponent als Strings que li passem per paràmetre, i els torna en
     * forma de llista d'objectes Servei
     *
     * @param data corresponent amb el camp data_servei de la taula serveis
     * @param hora corresponent amb el camp hora_inici de la taula serveis
     * @return llista d'objectes Servei
     */
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

    /**
     * Busca a la taula servei els registres amb data_servei i treballador_id
     * corresponent als Strings que li passem per paràmetre, i els torna en
     * forma de llista d'objectes Servei
     *
     * @param idTreballador corresponent amb la columna id_treballador de la
     * taula serveis
     * @param data corresponent amb la columna data_servei de la taula serveis
     * @return llista d'objectes Servei
     */
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

    /**
     * Busca a la taula servei els registres amb data_servei, treballador_id i
     * hora_inici corresponent als Strings que li passem per paràmetre, i els
     * torna en forma de llista d'objectes Servei
     *
     * @param idTreballador corresponent amb la columna id_treballador de la
     * taula serveis
     * @param data corresponent amb la columna data_servei de la taula serveis
     * @param hora corresponent amb la columna hora_inici de la taula serveis
     * @return llista d'objectes Servei
     */
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

    /**
     * Intenta inserir un nou registre a la taula treballador amb els paràmetres
     * String, i torna un objecte PostResponse amb els resultats de la consulta.
     *
     * @param nom corresponent amb la columna nom de la taula treballadoor
     * @param cognom1 corresponent amb la columna cognom1 de la taula
     * treballadoor
     * @param cognom2 corresponent amb la columna cognom2 de la taula
     * treballadoor
     * @param dni corresponent amb la columna dni de la taula treballadoor
     * @param admin corresponent amb la columna esadmin de la taula treballadoor
     * @param login corresponent amb la columna login de la taula treballadoor
     * @param password corresponent amb la columna password de la taula
     * treballadoor
     * @return objecte PostResponse
     */
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

    /**
     * Intenta actualitzar el registre amb _id corresponent al paràmetre id a la
     * taula treballador amb els paràmetres String, i torna un objecte
     * PostResponse amb els resultats de la consulta.
     *
     * @param id corresponent amb la columna _id de la taula treballadoor
     * @param nom corresponent amb la columna nom de la taula treballadoor
     * @param cognom1 corresponent amb la columna cognom1 de la taula
     * treballadoor
     * @param cognom2 corresponent amb la columna cognom2 de la taula
     * treballadoor
     * @param dni corresponent amb la columna dni de la taula treballadoor
     * @param admin corresponent amb la columna esadmin de la taula treballadoor
     * @param login corresponent amb la columna login de la taula treballadoor
     * @param pass corresponent amb la columna password de la taula treballadoor
     * @return objecte PostResponse
     */
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

    /**
     * Intenta modificar el camp id_treballador amb el paràmetre idTreballador
     * al registre de la taula serveis amb _id corresponent al paràmetre
     * idServei. Retorna un objecte PostResponse amb els resultats de la
     * consulta.
     *
     * @param idServei corresponent amb la columna _id de la taula serveis
     * @param idTreballador corresponent amb la columna id_treballador de la
     * taula serveis
     * @return objecte PostResponse
     */
    public PostResponse assignarTreballador(String idServei, String idTreballador) {
        if (idTreballador.equals("1")) {
            return new PostResponse(0, "No es poden assignar Serveis al usuari Administrador");
        }
        if (dateOverlaps(idServei, idTreballador)) {
            return new PostResponse(0, "Error al assignar treballador: ja te un altre servei assignat durant aquest horari.");
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
            if (ex.getSQLState().equals("23505")) {
                response.setMessage("Error al assignar treballador: ja te un altre servei assignat el mateix dia i hora, i se l'hi ha concedit el do de la ubiqüitat.");
            } else {
                response.setMessage(ex.getMessage());
            }
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    /**
     * Intenta esborrar el registre de la taula treballador amb _id corresponent
     * al paràmetre idTreballador. Torna un objecte PostResponse amb els
     * resultats de la consulta.
     *
     * @param idTreballador corresponent amb la columna _id de la taula
     * treballador
     * @return objecte PostResponse
     */
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

    /**
     * Intenta inserir un nou registre a la taula serveis amb les dades que reb
     * com a paràmetres. Torna un objecte PostResponse amb els resultats de la
     * consulta.
     *
     * @param descripcio corresponent amb la columna descripcio de la taula
     * serveis
     * @param dataServei corresponent amb la data_servei descripcio de la taula
     * serveis
     * @param horaInici corresponent amb la hora_inici descripcio de la taula
     * serveis
     * @param horaFinal corresponent amb la hora_final descripcio de la taula
     * serveis
     * @param idTreballador corresponent amb la id_treballador descripcio de la
     * taula serveis
     * @return objecte PostResponse
     */
    public PostResponse insertServei(String descripcio, String dataServei, String horaInici, String horaFinal, String idTreballador) {
        if (!validTime(dataServei, horaInici, horaFinal)){
            return new PostResponse(0, "La hora de inici no pot ser despres de la hora final.");
        }
        if (dateOverlaps(idTreballador, dataServei, horaInici, horaFinal)) {
            return new PostResponse(0, "No s'ha pogut crear el servei: El treballador ja te un servei assignat durant aquest horari.");
        }
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
            if (ex.getSQLState().equals("23505")) {
                response.setMessage("Error al crear servei: el treballador ja te un altre servei assignat el mateix dia i hora.");
            } else {
                response.setMessage(ex.getMessage());
            }
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    /**
     * Intenta actualitzar el registre a la taula serveis corresponent al _id
     * amb les dades que reb com a paràmetres. Torna un objecte PostResponse amb
     * els resultats de la consulta.
     *
     * @param id corresponent amb la columna _id de la taula serveis
     * @param descripcio corresponent amb la columna descripcio de la taula
     * serveis
     * @param dataServei corresponent amb la data_servei descripcio de la taula
     * serveis
     * @param horaInici corresponent amb la hora_inici descripcio de la taula
     * serveis
     * @param horaFinal corresponent amb la hora_final descripcio de la taula
     * serveis
     * @param idTreballador corresponent amb la id_treballador descripcio de la
     * taula serveis
     * @return objecte PostResponse
     */
    public PostResponse updateServei(String id, String descripcio, String dataServei, String horaInici, String horaFinal, String idTreballador) {
        if (!validTime(dataServei, horaInici, horaFinal)){
            return new PostResponse(0, "La hora de inici no pot ser despres de la hora final.");
        }
        if (dateOverlaps(idTreballador, dataServei, horaInici, horaFinal)) {
            return new PostResponse(0, "No s'ha pogut modificar el servei. El treballador ja te un servei assignat duarant aquest horari.");
        }
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
            if (ex.getSQLState().equals("23505")) {
                response.setMessage("Error al modificar servei: el treballador ja te un altre servei assignat el mateix dia i hora.");
            } else {
                response.setMessage(ex.getMessage());
            }
        } finally {
            closeStatement(stm);
        }
        return response;
    }

    /**
     * Intenta esborrar el registre de la taula serveis corresponent amb l'id
     * que reb per paràmetre. Retorna un objecte PostResponse amb els resultats
     * de la consulta.
     *
     * @param idServei corresponent amb la columna _id de la taula serveis
     * @return objecte PostResponse
     */
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

    /**
     * Tanca el Statement
     *
     * @param stm
     */
    public void closeStatement(Statement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ": " + ex.getMessage());
        }
    }

    /**
     * Comprova si l'usuari existeix a la taula treballador i si coincideix amb
     * el password que reb per paràmetre. Torna un objecte PostResponse amb els
     * resultats de la consulta. Si el login és correcte torna codi 1 i al
     * missatge l'id del treballador i un 1 si es administrador o un 0 si no ho
     * és.
     *
     * @param user corresponent a la columna nom de la taula treballador
     * @param password corresponent a la columna password de la taula
     * treballador
     * @return objecte PostResponse
     */
    public PostResponse login(String user, String password) {
        PostResponse response = new PostResponse(0, "Usuari incorrecte.");
        List<Treballador> llista = getTreballadors();
        for (Treballador treb : llista) {
            if (treb.getLogin().equals(user)) {
                response.setMessage("Contrasenya incorrecta.");
                if (treb.getPassword().equals(password)) {
                    response.setRequestCode(1);
                    response.setMessage(("" + treb.getId() + "Admin:" + treb.getEsAdmin()));
                    break;
                }
            }
        }
        return response;
    }
    
    
    public PostResponse checkIn(String idReserva) {
        PostResponse response = new PostResponse(0, "No s'ha pogut realitzar el Check-In");
        Statement stm = null;
        String updateSQL = "UPDATE " + TaulaReserva.NOM_TAULA + " SET "
                + TaulaReserva.CHECKIN + "= 1" 
                + " WHERE " + TaulaReserva.ID + "=" + idReserva;
        try {
            stm = conn.createStatement();
            int rowsUpdated = stm.executeUpdate(updateSQL);
            if (rowsUpdated == 1) {
                response.setRequestCode(1);
                response.setMessage("Check-in realitzat correctament.");
            } else {
                response.setRequestCode(0);
                response.setMessage("No s'ha pogut realitzar el Check-In.");
            }
        } catch (SQLException ex) {
            response.setRequestCode(0);
            response.setMessage("No s'ha pogut realitzar el Check-In.");
        } finally {
            closeStatement(stm);
        }
        return response;
    }
    

    /**
     * Comprova que l'hora d'inici del servei no sigui posterior a l'hora de
     * termini de la mateixa.
     * 
     * @param dataServei corresponent amb la columna data_servei de la taula serveis
     * @param horaInici corresponent amb la columna hora_inici de la taula serveis
     * @param horaFinal corresponent amb la columna hora_final de la taula serveis
     * @return false si l'hora d'inici es posterior a l'hora final, false si no ho és      * 
     */
    private boolean validTime(String dataServei, String horaInici, String horaFinal) {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm");        
        try {
            Date dataInici = parser.parse(dataServei + " " + horaInici);
            Date dataFinal = parser.parse(dataServei + " " + horaFinal);
            if (dataFinal.before(dataInici)){
                return false;
            } else {
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return false;
        }        
    }

    /**
     * Comprova si el servei que es vol assignar a un treballador coincideix en
     * horari amb algún dels serveis que ja tenia assignat.
     *
     * @param idTreballador id del treballador
     * @param dataServei data del servei que es vol assignar
     * @param horaInici hora d'inici del servei que es vol assignar
     * @param horaFinal hora final del servei que es vol assignar
     * @return true si l'horari coincideix amb algún d'un altre servei ja
     * assignat
     */
    private boolean dateOverlaps(String idTreballador, String dataServei, String horaInici, String horaFinal) {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dataInici;
        Date dataFinal;
        try {
            //Cream un objecte Date amb data i hora per la hora inici i un altre per la hora final del servei que es vol assignar
            Date dataActualInici = parser.parse(dataServei + " " + horaInici);
            Date dataActualFinal = parser.parse(dataServei + " " + horaFinal);
            List<Servei> serveisTreballador = this.getServeisTreballador(stringToInt(idTreballador));
            for (Servei servei : serveisTreballador) {
                //Es comprova si els horaris es sobreposen amb algun dels serveis que el treballador ja te assignats
                dataInici = parser.parse(servei.getData_servei() + " " + servei.getHora_inici());
                dataFinal = parser.parse(servei.getData_servei() + " " + servei.getHora_final());
                if (dataInici.before(dataActualInici) && dataFinal.after(dataActualInici)) {
                    System.out.println("dataActualInici after dataInici & before dataFinal");
                    return true; //Si la hora d'inici està entre l'hora d'inici i la final d'un servei ja assignat tornam true
                } else if (dataInici.before(dataActualFinal) && dataFinal.after(dataActualFinal)) {
                    System.out.println("dataActualFinal after dataInici & before dataFinal");
                    return true; //Si la hora d'inici està entre l'hora d'inici i la final d'un servei ja assignat tornam true
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false; //En cas d'error al transformar de String a Date tornem false
        }
        return false; //Si no s'ha trobat cap horari solapat tornem false
    }

    /**
     * Comprova si el servei que es vol assignar a un treballador coincideix en
     * horari amb algún dels serveis que ja tenia assignat.
     *
     * @param idServei id del servei que es vol assignar
     * @param idTreballador id del treballador al que se li vol assignar el
     * servei
     * @return true si l'horari coincideix amb algún d'un altre servei ja
     * assignat
     */
    private boolean dateOverlaps(String idServei, String idTreballador) {
        int intIdServei = stringToInt(idServei);
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dataInici;
        Date dataFinal;
        List<Servei> serveisTreballador = this.getServeisTreballador(stringToInt(idTreballador));
        //Si el treballador no te serveis no cal comprovar res mes
        if (serveisTreballador == null) {
            return false;
        }
        Servei serveiActual = null;
        for (Servei servei : serveisTreballador) {
            if (servei.getId() == intIdServei) {
                serveiActual = servei; //Obteni el servei que es vol assignar
            }
        }
        try {
            //Cream un objecte Date amb data i hora per la hora inici i un altre per la hora final del servei que es vol assignar
            Date dataActualInici = parser.parse(serveiActual.getData_servei() + " " + serveiActual.getHora_inici());
            Date dataActualFinal = parser.parse(serveiActual.getData_servei() + " " + serveiActual.getHora_final());
            //Es comprova si els horaris es sobreposen amb algun dels serveis que el treballador ja te assignats
            for (Servei servei : serveisTreballador) {
                if (servei.getId() != intIdServei) {
                    dataInici = parser.parse(servei.getData_servei() + " " + servei.getHora_inici());
                    dataFinal = parser.parse(servei.getData_servei() + " " + servei.getHora_final());
                    if (dataInici.before(dataActualInici) && dataFinal.after(dataActualInici)) {
                        return true; //Si la hora d'inici està entre l'hora d'inici i la final d'un servei ja assignat tornam true
                    } else if (dataInici.before(dataActualFinal) && dataFinal.after(dataActualFinal)) {
                        return true; //Si la hora final està entre l'hora d'inici i la final d'un servei ja assignat tornam true
                    }
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false; //En cas d'error al transformar de String a Date tornem false
        }
        return false; //Si no s'ha trobat cap horari solapat tornem false
    }

}
