/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import static easycheckserver.utils.NetUtils.buildUrl;
import static easycheckserver.utils.NetUtils.doPostRequest;
import easycheckserver.utils.PostResponse;
import java.net.URL;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Per tal d'executar amb èxit aquestos tests cal engegar l'aplicació servidor
 * EasyCheckServer a la màquina local (localhost) amb el port 8080, i a més cal 
 * executar l'script easycheck.sql per tal de esborrar totes les dades i crear
 * tan sols el usuari Administrador.
 * 
 * @author Toni
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPost extends TestCase {
    
    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    private Gson gson = new Gson();
    
    /**
     * Prova d'inserir un treballador
     */
    @Test
    public void test_a_InserirTreballador() {
        System.out.println("Test a:");
        String query = buildQueryInserirTreballador("Test", "Test", "Test", "Test", "Test", "Test", 0);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);        
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
   
    /**
     * Prova d'actualitzar el treballador del test a
     */
    @Test
    public void test_b_ActualitzarTreballador() {
        System.out.println("Test b:");
        String query = buildQueryActualitzarTreballador(2, "Test", "Test", "Test", "Test", "Test", "Test", 0);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    /**
     * Prova d'inserir un treballador amb el mateix DNI que el del test a
     * La inserció ha de fallar
     */
    @Test
    public void test_c_InserirTreballadorDniDuplicat() {      
        System.out.println("Test c:");
        String query = buildQueryInserirTreballador("Test", "Test", "Test", "Test", "Test", "Test", 0);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),0);
    }
   
    /**
     * Prova d'inserir un nou servei
     */
    @Test
    public void test_d_InserirServei() {
        System.out.println("Test d:");
        String query = buildQueryInserirServei("Test", "Test", "Test", "Test", 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);        
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }

    /**
     * Prova d'actualitzar el servei inserit al test d
     */
    @Test
    public void test_e_ActualitzarServei() {
        System.out.println("Test e:");
        String query = buildQueryActualitzarServei(1, "Test", "Test", "Test", "Test", 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    /**
     * Prova d'esborrar el treballador inserit al test a
     * Com que el servei creat al test d s'ha assignat a aquest treballador
     * l'esborrar ha de fallar
     */
    @Test
    public void test_f_BorrarTreballadorAmbServeiAssignat() {
        System.out.println("Test f:");
        String query = buildQueryBorrarTreballador(2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),0);
    }
    
    /**
     * Prova d'assignar el treballador creat al test a al servei creat al test d
     */
    @Test
    public void test_g_AssignarTreballador() {
        System.out.println("Test g:");
        String query = buildQueryAssignarTreballador(1, 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);         
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    /**
     * Prova d'esborrar el servei creat al test d
     */
    @Test
    public void test_h_BorrarServei() {
        System.out.println("Test h:");
        String query = buildQueryBorrarServei(1);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    /**
     * Prova d'esborrar el treballador creat al test a
     */
    @Test
    public void test_i_BorrarTreballador() {
        System.out.println("Test i:");
        String query = buildQueryBorrarTreballador(2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
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
