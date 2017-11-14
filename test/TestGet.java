/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
 */
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import easycheckserver.model.Reserva;
import easycheckserver.model.Servei;
import easycheckserver.model.Treballador;
import static easycheckserver.utils.NetUtils.buildUrl;
import static easycheckserver.utils.NetUtils.doGetRequest;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/** *
 * @author Toni
 * 
 * Per tal d'executar amb èxit aquestos tests cal engegar l'aplicació servidor
 * EasyCheckServer a la màquina local (localhost) amb el port 8080, i a més cal 
 * inseerir les dades d'exemple proporcionades amb l'script easycheck_examples.sql.
 * 
 */
public class TestGet extends TestCase {
    
    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    private static final String NOM = "Administrador";
    private static final String DESCRIPCIO = "Mallorca - Menorca";
    private static final String QR_CODE = "45R545WE45";
    private Gson gson = new Gson();
    
    /**
     * Prova d'obtenir tots els treballadors
     */    
    @Test
    public void testGetTreballadors(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        final Type tipusLlista = new TypeToken<List<Treballador>>(){}.getType();
        String json = doGetRequest(url);
        ArrayList<Treballador> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getNom(),NOM);
    }   
    
    /**
     * Prova d'obtenir el treballador corresponent a l'id
     */
    @Test
    public void testGetTreballadorId(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", "id=1");
        System.out.println(url);
        String json = doGetRequest(url);
        Treballador treballador = gson.fromJson(json, Treballador.class);
        assertEquals(treballador.getNom(),NOM);
    }
    
    /**
     * Prova d'obtenir tots els serveis
     */
    @Test
    public void testGetServeis(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    /**
     * Prova d'opbenir els serveis assignats a un treballador
     */
    @Test
    public void testGetServeisTreballador(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "treballador=4");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    /**
     * Prova d'obtenir els serveis amb una data específica assignats a un treballador
     */
    @Test
    public void testGetServeisTreballadorData(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "treballador=4&data=19/12/2017");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    /**
     * Prova d'obtenir els serveis amb una data i hora específiques assignats
     * a un treballador
     */
    @Test
    public void testGetServeisTreballadorDataHora(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "treballador=4&data=19/12/2017&hora=10:23");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    /**
     * Prova d'obtenir els serveis amb una data específica
     */
    @Test
    public void testGetServeisData(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "data=19/12/2017");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    /**
     * Prova d'obtenir els serveis amb una data i hora específiques
     */
    @Test
    public void testGetServeisDataHora(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "data=19/12/2017&hora=10:23");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    /**
     * Prova d'obtenir totes les Reserves
     */
    @Test
    public void testGetReserves(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", null);
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    /**
     * Prova d'obtenir les reserves amb un localitzador específic
     */
    @Test
    public void testGetReservesLoc(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "loc=123456");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    /**
     * Prova d'obtenir les reserves amb un QRcode específic
     */
    @Test
    public void testGetReservesQRCode(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "qrcode=45R545WE45");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    /**
     * Prova d'obtenir les reserves amb una data específica
     */
    @Test
    public void testGetReservesData(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "data=19/12/2017");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    /**
     * Prova d'obtenir les reserves amb un dni específic
     */
     @Test
    public void testGetReservesDni(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "dni=41471860P");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    /**
     * Prova d'obtenir les reserves amb una data i hora específiques
     */
    @Test
    public void testGetReservesDataHora(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "data=19/12/2017&hora=10:23");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    /**
     * Prova d'obtenir les reserves assignades a un servei
     */
    @Test
    public void testGetReservesServei(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "servei=1");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
}
