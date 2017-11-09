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
    
    @Test
    public void test_a_InserirTreballador() {
        System.out.println("Test a:");
        String query = buildQueryInserirTreballador("Test", "Test", "Test", "Test", "Test", "Test", 0);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);        
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
   
    @Test
    public void test_b_ActualitzarTreballador() {
        System.out.println("Test b:");
        String query = buildQueryActualitzarTreballador(2, "Test", "Test", "Test", "Test", "Test", "Test", 0);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    @Test
    public void test_c_InserirTreballadorDniDuplicat() {      
        System.out.println("Test c:");
        String query = buildQueryInserirTreballador("Test", "Test", "Test", "Test", "Test", "Test", 0);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),0);
    }
   
    @Test
    public void test_d_InserirServei() {
        System.out.println("Test d:");
        String query = buildQueryInserirServei("Test", "Test", "Test", "Test", 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);        
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }

    @Test
    public void test_e_ActualitzarServei() {
        System.out.println("Test e:");
        String query = buildQueryActualitzarServei(1, "Test", "Test", "Test", "Test", 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    @Test
    public void test_f_BorrarTreballadorAmbServeiAssignat() {
        System.out.println("Test f:");
        String query = buildQueryBorrarTreballador(2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),0);
    }
    
    @Test
    public void test_g_AssignarTreballador() {
        System.out.println("Test g:");
        String query = buildQueryAssignarTreballador(1, 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);         
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    @Test
    public void test_h_BorrarServei() {
        System.out.println("Test h:");
        String query = buildQueryBorrarServei(1);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    @Test
    public void test_i_BorrarTreballador() {
        System.out.println("Test i:");
        String query = buildQueryBorrarTreballador(2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
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
