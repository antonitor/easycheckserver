/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
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
 * Per tal d'executar amb èxit aquestos tests cal engegar l'aplicació servidor
 * EasyCheckServer a la màquina local (localhost) amb el port 8080, i a més cal 
 * executar l'script easycheck.sql per tal de esborrar totes les dades i crear
 * tan sols un el usuari Administrador.
 * 
 * @author Toni
 */
public class TestLogin extends TestCase {
    
    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;    
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";
    private Gson gson = new Gson();
    
    /**
     * Prova de fer una petició de login correcta
     */    
    @Test
    public void testLoginCorrecte(){
        String query = buildQueryLogin(USER,PASSWORD);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/login", null);
        String json = doPostRequest(url, query);        
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),1);
    }
    
    /**
     * Prova de fer una petició de login amb contrasenya incorrecta
     */
    @Test
    public void testLoginPasswordIncorrecte(){
        String query = buildQueryLogin(USER,"x");
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/login", null);
        String json = doPostRequest(url, query);        
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),0);
    } 
    
    /**
     * Prova de fer una petició de login amb usuari incorrecte
     */
    @Test
    public void testLoginUsuariIncorrecte(){
        String query = buildQueryLogin("x",PASSWORD);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/login", null);
        String json = doPostRequest(url, query);        
        PostResponse response = gson.fromJson(json, PostResponse.class);
        assertEquals(response.getRequestCode(),0);
    } 
    

    /**
     * Genera el query per fer una petició de login
     */
    public String buildQueryLogin(String user, String password) {
        return "user=" + user + "&pass=" + password;
    }
}
