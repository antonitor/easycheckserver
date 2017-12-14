/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import easycheckserver.model.Servei;
import easycheckserver.model.Treballador;
import static easycheckserver.utils.NetUtils.buildUrl;
import static easycheckserver.utils.NetUtils.doGetRequest;
import static easycheckserver.utils.NetUtils.doPostRequest;
import easycheckserver.utils.PostResponse;
import java.net.URL;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/**
 * Per tal d'executar amb èxit aquestos tests cal engegar l'aplicació servidor
 * EasyCheckServer a la màquina local (localhost) amb el port 8080, i a més cal
 * executar l'script easycheck.sql per tal de esborrar totes les dades i crear
 * el usuari Administrador amb _id = 1.
 *
 * @author Toni
 */

public class TestPost extends TestCase {

    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    private static final int ID_ADMINISTRADOR = 1;
    private Gson gson = new Gson();

    /**
     * Prova d'inserir un treballador
     */
    @Test
    public void testInserirTreballador() {
        String randomDni = randomString();
        PostResponse response = inserirTreballador("test", "test", "test", randomDni, randomString(), "test", 0);
        borrarTreballador(getIdTreballadorDni(randomDni));
        assertEquals(response.getRequestCode(), 1);
    }
    
    /**
     * Prova d'esborrar un treballador
     */
    @Test
    public void testEsborrarTreballador() {
        String randomDni = randomString();
        inserirTreballador("test", "test", "test", randomDni, randomString(), "test", 0);
        PostResponse response = borrarTreballador(getIdTreballadorDni(randomDni));
        assertEquals(response.getRequestCode(), 1);
    }

    /**
     * Prova d'actualitzar un treballador
     */
    @Test
    public void testActualitzarTreballador() {
        String randomDni = randomString();
        inserirTreballador("test", "test", "test", randomDni, randomString(), "test", 0);
        String query = buildQueryActualitzarTreballador(getIdTreballadorDni(randomDni), "Test", "Test", "Test", randomDni, randomString(), "Test", 0);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        borrarTreballador(getIdTreballadorDni(randomDni));
        assertEquals(response.getRequestCode(), 1);
    }

    /**
     * Prova d'inserir un treballador amb el mateix DNI que un altre treballador
     */
    @Test
    public void testInserirTreballadorDniDuplicat() {
        String randomDni = randomString();
        inserirTreballador("test", "test", "test", randomDni, randomString(), "test", 0);
        String query = buildQueryInserirTreballador("Test", "Test", "Test", randomDni, "Test", "Test", 0);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        borrarTreballador(getIdTreballadorDni(randomDni));
        assertEquals(response.getRequestCode(), 0);
    }

    /**
     * Prova d'inserir un nou servei
     */
    @Test
    public void testInserirServei() {
        String descripcioAleatoria = randomString();
        PostResponse response = inserirServei(descripcioAleatoria, "11/10/2018", "10:30", "11:30", ID_ADMINISTRADOR);
        borrarServei(getIdServeiDesc(descripcioAleatoria));
        assertEquals(response.getRequestCode(), 1);
    }
    
    /**
     * Prova d'esbolrrar un servei
     */
    @Test
    public void testEsborrarServei() {
        String descripcioAleatoria = randomString();
        inserirServei(descripcioAleatoria, "9/10/2018", "15:30", "18:30", ID_ADMINISTRADOR);
        PostResponse response = borrarServei(getIdServeiDesc(descripcioAleatoria));
        assertEquals(response.getRequestCode(), 1);
    }
    
    /**
     * Prova d'inserir un nou servei amb l'hora de inici posterior a l'hora final
     */
    @Test
    public void testInserirServeiDataInvalida() {
        String descripcioAleatoria = randomString();
        PostResponse response = inserirServei(descripcioAleatoria, "8/10/2018", "13:30", "11:30", ID_ADMINISTRADOR);
        borrarServei(getIdServeiDesc(descripcioAleatoria));
        assertEquals(response.getRequestCode(), 0);
    }
    
    /**
     * Prova d'assignar un servei a un treballador que es solapa amb un altre
     * servei que ja te assignat
     */
    @Test
    public void testInserirServeiTempsSolapat() {
        String descripcioAleatoria = randomString();
        inserirServei(descripcioAleatoria, "7/10/2018", "10:30", "11:30", ID_ADMINISTRADOR);
        PostResponse response = inserirServei("test", "7/10/2018", "11:00", "12:00", ID_ADMINISTRADOR);
        borrarServei(getIdServeiDesc(descripcioAleatoria));
        assertEquals(response.getRequestCode(), 0);
    }
    

    /**
     * Prova d'actualitzar el servei inserit al test d
     */
    @Test
    public void testActualitzarServei() {        
        String descripcioAleatoria = randomString();
        inserirServei(descripcioAleatoria, "6/10/2018", "10:30", "11:30", 3);        
        String query = buildQueryActualitzarServei(getIdServeiDesc(descripcioAleatoria), "Test", "6/10/2018", "10:30", "11:30", 3);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        borrarServei(getIdServeiDesc(descripcioAleatoria));
        assertEquals(response.getRequestCode(), 1);
    }

    /**
     * Prova d'esborrar un treballador amb serveis assignats.
     */
    @Test
    public void testBorrarTreballadorAmbServeiAssignat() {
        String randomDni = randomString();
        inserirTreballador("test", "test", "test", randomDni, randomString(), "test", 0);
        String descripcioAleatoria = randomString();
        inserirServei(descripcioAleatoria, "5/10/2018", "10:30", "11:30", getIdTreballadorDni(randomDni)); 
        PostResponse response = borrarTreballador(getIdTreballadorDni(randomDni));
        borrarServei(getIdServeiDesc(descripcioAleatoria));
        borrarTreballador(getIdTreballadorDni(randomDni));
        assertEquals(response.getRequestCode(), 0);
    }
   
   /**
     * Prova d'assignar un servei a un treballador.
     */ 
    @Test
    public void testAssignarTreballadorAdmin(){
        String query = this.buildQueryAssignarTreballador(1, ID_ADMINISTRADOR);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(), 0);
    }
    
    
    /**
     * Prova d'assignar un servei a un treballador.
     */ 
    @Test
    public void testAssignarTreballador(){
        String query = this.buildQueryAssignarTreballador(1, 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(), 1);
    }
    
     /**
     * Prova de fer el check-in d'una reserva
     */ 
    @Test
    public void testCheckIn(){
        String query = this.buildQueryCheckInint(2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(), 1);
    }
      
    /**
     * Inserció de treballador per les proves
     * @return objecte PostResponse
     */
    private PostResponse inserirTreballador(String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        String query = buildQueryInserirTreballador(nom, cognom1, cognom2, dni, login, password, esadmin);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        return gson.fromJson(json, PostResponse.class);
    }
    
    /**
     * Esborrat de treballador per les proves
     * @return objecte PostResponse
     */
    private PostResponse borrarTreballador(int idTreballador) {
        String query = buildQueryBorrarTreballador(idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        return gson.fromJson(json, PostResponse.class);    
    }
    
    
    /**
     * Genera un String aleatori de 9 caràcters
     * @return String aleatori
     */
    private String randomString(){
        int dni = new Random().nextInt(999999999);
        return ""+dni;
    }
    
        
    /**
     * Obtè de la base de dades el _id del treballador amb aquest dni
     * @param dni dni del trebalador
     * @return objecte treballador
     */
    private int getIdTreballadorDni(String dni){
        List<Treballador> llista = obtenirTreballadors();
        for (Treballador treb : llista){
            if (treb.getDni().equals(dni)) {
                return treb.getId();
            }
        }
        return 0;
    }
    
    /**
     * Obté un llistat amb tots els treballadors del servidor
     * @return llista d'objectes Treballador
     */
    private List<Treballador> obtenirTreballadors() {
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doGetRequest(url);
        java.lang.reflect.Type tipusLlistaDeTreballadors = new TypeToken<List<Treballador>>() {}.getType();        
        return gson.fromJson(json, tipusLlistaDeTreballadors);
    }

    /**
     * Obtè de la base de dades el _id del servei amb aquesta descripció
     * @param desc descripcio del servei
     * @return objecte treballador
     */
    private int getIdServeiDesc(String desc) {
        for (Servei serv : obtenirServeis()){
            if (serv.getDescripcio().equals(desc)) {
                return serv.getId();
            }
        }
        return 0;
    }
    
    /**
     * Obtenció de llista amb tots els serveis
     * @return llista de serveis
     */
    private List<Servei> obtenirServeis(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        String json = doGetRequest(url);        
        java.lang.reflect.Type tipusLlistaDeServeis = new TypeToken<List<Servei>>() {}.getType();      
        return  gson.fromJson(json, tipusLlistaDeServeis);
    }
    
    /**
     * Esborrat de servei per les proves
     * @return objecte PostResponse
     */
    private PostResponse borrarServei(int idServei) {
        String query = buildQueryBorrarServei(idServei);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        String json = doPostRequest(url, query);
        return gson.fromJson(json, PostResponse.class);
    }
    
    /**
     * Inserció de servei per les proves
     * @return objecte PostResponse
     */
    private PostResponse inserirServei(String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        String query = buildQueryInserirServei(descripcio, dataservei, horaInici, horaFinal, idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        String json = doPostRequest(url, query);
        return gson.fromJson(json, PostResponse.class);
    }
    
    
    /**
     * Genera el query per fer check-in a una reserva
     */
    public String buildQueryCheckInint (int idReserva) {
        return "checkin=" + idReserva;
    }
    
    /**
     * Genera el query per assignar un treballador a un servei
     */
    public String buildQueryAssignarTreballador(int idServei, int idTreballador) {
        return "idservei=" + idServei + "&idtreballador=" + idTreballador;
    }

    /**
     * Genera el query per inserir un nou treballador
     */
    public String buildQueryInserirTreballador(String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        return "nom=" + nom + "&cognom1=" + cognom1 + "&cognom2=" + cognom2 + "&dni=" + dni + "&esadmin=" + esadmin + "&login=" + login + "&password=" + password;
    }

    /**
     * Genera el query per actualitzar un treballador
     */
    public String buildQueryActualitzarTreballador(int id, String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        return "id=" + id + "&nom=" + nom + "&cognom1=" + cognom1 + "&cognom2=" + cognom2 + "&dni=" + dni + "&esadmin=" + esadmin + "&login=" + login + "&password=" + password;
    }

    /**
     * Genera el query per esborrar un treballador
     */
    private String buildQueryBorrarTreballador(int idTreballador) {
        return "borrarid=" + idTreballador;
    }

    /**
     * Genera el query per esborrar un servei
     */
    private String buildQueryBorrarServei(int idServei) {
        return "borrarid=" + idServei;
    }

    /**
     * Genera el query per modificar un servei
     */
    public String buildQueryActualitzarServei(int id, String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        return "id=" + id + "&descripcio=" + descripcio + "&dataservei=" + dataservei + "&horainici=" + horaInici + "&horafinal=" + horaFinal + "&idtreballador=" + idTreballador;
    }

    /**
     * Genera el query per inserir un nou servei
     */
    public String buildQueryInserirServei(String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        return "descripcio=" + descripcio + "&dataservei=" + dataservei + "&horainici=" + horaInici + "&horafinal=" + horaFinal + "&idtreballador=" + idTreballador;
    }
}
