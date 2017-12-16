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
import org.junit.Test;

/**
 *
 * @author Toni
 */
public class TestSubstitucio_i_CheckIn extends TestCase  {
    
    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    private static final int ID_ADMINISTRADOR = 1;
    private static Gson gson = new Gson();
    
     
    
    /**
     * Prova d'assignar un servei al treballador administrador
     * Ha de fallar
     */ 
    @Test
    public void testAssignarTreballadorAdmin(){
        String query = this.buildQueryAssignarTreballador(1, ID_ADMINISTRADOR);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        System.out.println("Test Assignar Treballador Admin: " + response.getMessage());
        assertEquals(response.getRequestCode(), 0);
    }
        
       
    
    /**
     * Prova d'assignar un servei a un treballador.
     * Ha de finalitzar amb exit
     */ 
    @Test
    public void testAssignarTreballadorSuccess(){
        String query = this.buildQueryAssignarTreballador(1, 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        System.out.println("Test Assignar Treballador Success: " + response.getMessage());
        assertEquals(response.getRequestCode(), 1);
    }
    
    
       /**
     * Prova d'assignar un servei a un treballador que ja te un altre servei
     * assignat per aquest periode de temps: ha de fallar
     */ 
    @Test
    public void testAssignarTreballadorFailure(){
        String query = this.buildQueryAssignarTreballador(7, 2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        System.out.println("Test Assignar Treballador Failure: " + response.getMessage());
        assertEquals(response.getRequestCode(), 0);
    }
    
     /**
     * Prova de fer el check-in d'una reserva que ja te el check-in fet:
     * ha de fallar.
     */ 
    @Test
    public void testCheckInFailure(){
        String query = this.buildQueryCheckIn(2);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        System.out.println("Test Check-in Failure: " + response.getMessage());
        assertEquals(response.getRequestCode(), 0);
    }
    
     /**
     * Prova de fer el check-in d'una reserva que no te check-in fet:
     * ha de finalitzar amb exit
     */ 
    @Test
    public void testCheckInSucces(){
        String query = this.buildQueryCheckIn(1);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", null);
        String json = doPostRequest(url, query);
        PostResponse response = gson.fromJson(json, PostResponse.class);
        System.out.println("Test Check-in Success: " + response.getMessage());
        assertEquals(response.getRequestCode(), 1);
    }
    
    /**
     * Genera el query per fer check-in a una reserva
     */
    public static String buildQueryCheckIn (int idReserva) {
        return "checkin=" + idReserva;
    }
    
    /**
     * Genera el query per assignar un treballador a un servei
     */
    public static String buildQueryAssignarTreballador(int idServei, int idTreballador) {
        return "idservei=" + idServei + "&idtreballador=" + idTreballador;
    }
    
}
