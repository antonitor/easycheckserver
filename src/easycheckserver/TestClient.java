/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver;

import static easycheckserver.NetUtils.buildQuery;
import static easycheckserver.NetUtils.buildUrl;
import static easycheckserver.NetUtils.connectToUrl;
import java.net.URL;


/**
 *
 * @author Toni
 */
public class TestClient {

    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    

    public static void main(String[] args) {
        TestClient test = new TestClient();       
    }

    public TestClient() {
        System.out.println(obtenirReserves());
        System.out.println(obtenirReservesDniData("47254878k","8-5-85"));
        System.out.println(obtenirReservesDni("47254878k"));
        System.out.println(obtenirReservesData("8-5-85"));
    }

    public String obtenirReserves() {
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves",null);
        return connectToUrl(url);
    }
    
    public String obtenirReservesDni(String dni) {
        String query = buildQuery("dni",dni);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves",query);
        return connectToUrl(url);
    }
    
    public String obtenirReservesData(String data){
        String query = buildQuery("data",data);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves",query);
        return connectToUrl(url);
    }
    
    public String obtenirReservesDniData(String dni, String data){
        String query = buildQuery("dni",dni,"data",data);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves",query);
        return connectToUrl(url);
    }
    






    


}
