/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
 */
package easycheckserver.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import easycheckserver.model.Reserva;
import easycheckserver.model.Servei;
import easycheckserver.model.Treballador;
import easycheckserver.persistencia.GestorPersistencia;
import static easycheckserver.utils.NetUtils.stringToInt;
import java.util.List;

/**
 * Conté els mètodes que converteixen objectes del model a format Json
 *
 * @author Toni
 */
public class JSonParser {

    private final Gson gson;
    private GestorPersistencia gestor;

    /**
     * Al construir una instància d'aquesta classe obtè una instància de
     * GestorPersistencia i una altre de la llibreria Gson.
     */
    public JSonParser() {
        //amb setPrettyPrinting obtindrem codi Json indentat 
        gson = new GsonBuilder().setPrettyPrinting().create();
        gestor = new GestorPersistencia();
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Reserva,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getReserves() {
        gestor.open();
        List<Reserva> llista = gestor.getReserves();
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Reserva,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getReservesServei(String idServei) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesServei(stringToInt(idServei));
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Reserva,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getReservesQRCode(String qrcode) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesQRCode(qrcode);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Reserva,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getReservesLoc(String loc) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesLoc(loc);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Reserva,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getReservesDni(String dni) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesDni(dni);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Reserva,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getReservesData(String data) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesData(data);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Reserva,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getReservesDniData(String dni, String data) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesDniData(dni, data);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Servei,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getServeis() {
        gestor.open();
        List<Servei> llista = gestor.getServeis();
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Servei,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getServeisTreballador(String treballadorID) {
        gestor.open();
        List<Servei> llista = gestor.getServeisTreballador(stringToInt(treballadorID));
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Servei,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getServeisTreballadorData(String treballadorID, String data) {
        gestor.open();
        List<Servei> llista = gestor.getServeisTreballadorData(stringToInt(treballadorID), data);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Servei,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getServeisTreballadorDataHora(String treballadorID, String data, String hora) {
        gestor.open();
        List<Servei> llista = gestor.getServeisTreballadorDataHora(stringToInt(treballadorID), data, hora);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Servei,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getServeisData(String data) {
        gestor.open();
        List<Servei> llista = gestor.getServeisData(data);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes Servei,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getServeisDataHora(String data, String hora) {
        gestor.open();
        List<Servei> llista = gestor.getServeisDataHora(data, hora);
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades,un objecte Treballador, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getTreballadorId(String id) {
        gestor.open();
        Treballador treballador = gestor.getTreballadorId(stringToInt(id));
        gestor.close();
        return gson.toJson(treballador);
    }

    /**
     * Obre connexió amb la base de dades, obté la llista de objectes
     * Treballador, tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String getTreballadors() {
        gestor.open();
        List<Treballador> llista = gestor.getTreballadors();
        gestor.close();
        return gson.toJson(llista);
    }

    /**
     * Obre connexió amb la base de dades, truca el mètode per esborrar un
     * treballador, n'obté el resultat en forma d'objecte PostResponse, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String esborrarTreballador(String id) {
        gestor.open();
        PostResponse response = gestor.borrarTreballador(id);
        gestor.close();
        return gson.toJson(response);
    }

    /**
     * Obre connexió amb la base de dades, truca el mètode per inserir un
     * treballador, n'obté el resultat en forma d'objecte PostResponse, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String inserirTreballador(String nom, String cognom1, String cognom2, String dni, String admin, String login, String password) {
        gestor.open();
        PostResponse response = gestor.insertTreballador(nom, cognom1, cognom2, dni, admin, login, password);
        gestor.close();
        return gson.toJson(response);
    }

    /**
     * Obre connexió amb la base de dades, truca el mètode per modificar un
     * treballador, n'obté el resultat en forma d'objecte PostResponse, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String actualitzarTreballador(String id, String nom, String cognom1, String cognom2, String dni, String admin, String login, String pass) {
        gestor.open();
        PostResponse response = gestor.updateTreballador(id, nom, cognom1, cognom2, dni, admin, login, pass);
        gestor.close();
        return gson.toJson(response);
    }

    /**
     * Obre connexió amb la base de dades, truca el mètode per assignar un
     * treballador, n'obté el resultat en forma d'objecte PostResponse, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String assignarTreballador(String idServei, String idTreballador) {
        gestor.open();
        PostResponse response = gestor.assignarTreballador(idServei, idTreballador);
        gestor.close();
        return gson.toJson(response);
    }

    /**
     * Obre connexió amb la base de dades, truca el mètode per inserir un
     * servei, n'obté el resultat en forma d'objecte PostResponse, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String inserirServei(String descripcio, String dataServei, String horaInici, String horaFinal, String idTreballador) {
        gestor.open();
        PostResponse response = gestor.insertServei(descripcio, dataServei, horaInici, horaFinal, idTreballador);
        gestor.close();
        return gson.toJson(response);
    }

    /**
     * Obre connexió amb la base de dades, truca el mètode per modificar un
     * servei, n'obté el resultat en forma d'objecte PostResponse, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String actualitzarServei(String id, String descripcio, String dataServei, String horaInici, String horaFinal, String idTreballador) {
        gestor.open();
        PostResponse response = gestor.updateServei(id, descripcio, dataServei, horaInici, horaFinal, idTreballador);
        gestor.close();
        return gson.toJson(response);
    }

    /**
     * Obre connexió amb la base de dades, truca el mètode per esborrar un
     * servei, n'obté el resultat en forma d'objecte PostResponse, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String esborrarServei(String id) {
        gestor.open();
        PostResponse response = gestor.esborrarServei(id);
        gestor.close();
        return gson.toJson(response);
    }

    /**
     * Obre connexió amb la base de dades, truca el mètode per tal de fer login,
     * n'obté el resultat en forma d'objecte PostResponse, tanca la
     * connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
    public String Login(String user, String password) {
        gestor.open();
        PostResponse response = gestor.login(user, password);
        gestor.close();
        return gson.toJson(response);
    }
    
    /**
     * Obre connexió amb la base de dades, truca el mètode per fer el check-in 
     * d'una reserva, n'obté el resultat en forma d'objecte PostResponse,
     * tanca la connexió i retorna les dades en forma de Json
     *
     * @return dades formatades en forma de Json
     */
     public String checkIn(String idReserva) {
        gestor.open();
        PostResponse response = gestor.checkIn(idReserva);
        gestor.close();
        return gson.toJson(response);
    }
    
}
