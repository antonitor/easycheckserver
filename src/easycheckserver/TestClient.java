/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver;

import static easycheckserver.NetUtils.buildQuery;
import static easycheckserver.NetUtils.buildUrl;
import static easycheckserver.NetUtils.connectToUrl;
import easycheckserver.utils.JSonParser;
import java.net.URL;


/**
 *
 * @author Toni
 */
public final class TestClient {

    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    private JSonParser parser;
    

    public static void main(String[] args) {
        TestClient test = new TestClient();
    }

    public TestClient() {
        parser = new JSonParser();
        
        System.out.println(parser.getReservesServei("3"));
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
