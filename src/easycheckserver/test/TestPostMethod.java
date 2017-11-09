/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.test;

import static easycheckserver.utils.NetUtils.buildUrl;
import static easycheckserver.utils.NetUtils.doPostRequest;
import java.net.URL;
/**
 *
 * @author Toni
 */
public class TestPostMethod {

    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        TestPostMethod test = new TestPostMethod();
    }

    public TestPostMethod() {
        //inserirServei("TESTING INSERIR SERVEI", "11/11/2017","10:00","11:00",23);
        //actualitzarServei(12,"Mallorca - Menorca", "11/11/2017","10:00","21:00",2);
        //borrarServei(13);
        borrarTreballador(23);
        //assignarTreballador(2, 3);
        //inserirTreballador("Carles", "Puig", "Puigdemont", "4444444k", "tonis", "xxx", 0);
        //actualitzarTreballador(23,"Carles", "Puig", "Puigdemont", "5555555k", "Puchi", "xxx", 0);
    }

  
    public String borrarServei(int idServei) {
        String response = "";
        String query = buildQueryBorrarServei(idServei);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        response = doPostRequest(url, query);
        return response;
    }

    public String inserirServei(String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        String query = buildQueryInserirServei(descripcio, dataservei, horaInici, horaFinal, idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);        
        return doPostRequest(url, query);
    }

    public String actualitzarServei(int id, String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        String query = buildQueryActualitzarServei(id, descripcio, dataservei, horaInici, horaFinal, idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        return doPostRequest(url, query);
    }

    public String borrarTreballador(int idTreballador) {
        String query = buildQueryBorrarTreballador(idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        return doPostRequest(url, query);
    }

    public String assignarTreballador(int idServei, int idTreballador) {
        String query = buildQueryAssignarTreballador(idServei, idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);         
        return doPostRequest(url, query);
    }

    public String inserirTreballador(String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {        
        String query = buildQueryInserirTreballador(nom, cognom1, cognom2, dni, login, password, esadmin);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        return doPostRequest(url, query);
    }

    public String actualitzarTreballador(int id, String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        String query = buildQueryActualitzarTreballador(id, nom, cognom1, cognom2, dni, login, password, esadmin);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        return doPostRequest(url, query);
    }

   
    public String buildQueryAssignarTreballador(int idServei, int idTreballador) {
        return "idservei=" + idServei + "&idtreballador=" + idTreballador;
    }

    public String buildQueryInserirTreballador(String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        return "nom=" + nom + "&cognom1=" + cognom1 + "&cognom2=" + cognom2 + "&dni=" + dni + "&esadmin=" + esadmin + "&login=" + login + "&password=" + password;
    }

    public String buildQueryActualitzarTreballador(int id, String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        return "id=" + id + "&nom=" + nom + "&cognom1=" + cognom1 + "&cognom2=" + cognom2 + "&dni=" + dni + "&esadmin=" + esadmin + "&login=" + login + "&password=" + password;
    }

    private String buildQueryBorrarTreballador(int idTreballador) {
        return "borrarid=" + idTreballador;
    }

    private String buildQueryBorrarServei(int idServei) {
        return "borrarid=" + idServei;
    }

    public String buildQueryActualitzarServei(int id, String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        return "id=" + id + "&descripcio=" + descripcio + "&dataservei=" + dataservei + "&horainici=" + horaInici + "&horafinal=" + horaFinal + "&idtreballador=" + idTreballador;
    }

    public String buildQueryInserirServei(String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        return "descripcio=" + descripcio + "&dataservei=" + dataservei + "&horainici=" + horaInici + "&horafinal=" + horaFinal + "&idtreballador=" + idTreballador;
    }
    
}
