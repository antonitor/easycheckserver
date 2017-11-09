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
 * inseerir les dades d'exemple proporcionades amb l'script easycheck_examples.sql, 
 * o almenys les següents:
 * 
 * INSERT INTO treballador (nom, cognom1, cognom2, dni, esAdmin, login, password ) VALUES ('Antoni', 'Torres','Mari','44444444k','1','toni','xxx');
 * INSERT INTO treballador (nom, cognom1, cognom2, dni, esAdmin, login, password ) VALUES ('Maria','Ortega','Cobo','55555555k','1','mari','xxx');
 * INSERT INTO treballador (nom, cognom1, cognom2, dni, esAdmin, login, password ) VALUES ('Carlos Alberto','Castro','Cañabate','66666666k','1','carlos','xxx');
 * INSERT INTO serveis (descripcio, id_treballador, data_servei, hora_inici, hora_final) VALUES ('Tarragona - Reus', 4, '29/10/2017','10:00','11:00');
 * INSERT INTO client (nom_titular, cognom1_titular, cognom2_titular, telefon_titular, email_titular, dni_titular) VALUES ('Maria', 'Ortega', 'Cobos', '12345678', 'maria@gmail.com','41471860P');
 * INSERT INTO reserva (id_servei, id_client, localitzador,  data_reserva, qr_code, checkin) VALUES (1, 1, '123456', '16/1/2017', '45R545WE45', '0');
 *  
 */
public class TestGet extends TestCase {
    
    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    private static final String NOM = "Administrador";
    private static final String DESCRIPCIO = "Tarragona - Reus";
    private static final String QR_CODE = "45R545WE45";
    private Gson gson = new Gson();
    
        
    @Test
    public void testGetTreballadors(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        final Type tipusLlista = new TypeToken<List<Treballador>>(){}.getType();
        String json = doGetRequest(url);
        ArrayList<Treballador> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getNom(),NOM);
    }   
    
    @Test
    public void testGetTreballadorId(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", "id=1");
        System.out.println(url);
        String json = doGetRequest(url);
        Treballador treballador = gson.fromJson(json, Treballador.class);
        assertEquals(treballador.getNom(),NOM);
    }
    
    @Test
    public void testGetServeis(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    @Test
    public void testGetServeisTreballador(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "treballador=4");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    @Test
    public void testGetServeisTreballadorData(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "treballador=4&data=29/10/2017");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    @Test
    public void testGetServeisTreballadorDataHora(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "treballador=4&data=29/10/2017&hora=10:00");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    @Test
    public void testGetServeisData(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "data=29/10/2017");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    @Test
    public void testGetServeisDataHora(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", "data=29/10/2017&hora=10:00");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Servei>>(){}.getType();
        ArrayList<Servei> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getDescripcio(),DESCRIPCIO);
    }
    
    @Test
    public void testGetReserves(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", null);
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    @Test
    public void testGetReservesLoc(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "loc=123456");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    @Test
    public void testGetReservesQRCode(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "qrcode=45R545WE45");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    @Test
    public void testGetReservesData(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "data=29/10/2017");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
     @Test
    public void testGetReservesDni(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "dni=41471860P");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
    @Test
    public void testGetReservesDataHora(){
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserva", "data=29/10/2017&hora=10:00");
        System.out.println(url);
        String json = doGetRequest(url);
        final Type tipusLlista = new TypeToken<List<Reserva>>(){}.getType();
        ArrayList<Reserva> llista = gson.fromJson(json, tipusLlista);
        assertEquals(llista.get(0).getQr_code(),QR_CODE);
    }
    
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
