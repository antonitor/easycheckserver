/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import static easycheckserver.NetUtils.queryToMap;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

/**
 *
 * @author Toni
 */
public class EasyCheckServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/easycheckapi", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = handleRequest(t);
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static String handleRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        if (requestMethod.equals("GET")) {
            URI uri = t.getRequestURI();
            System.out.println("URL: " + uri);
            if (uri.getPath().contains("reserves")) {
                response = handleGetReserva(queryToMap(uri.getQuery()));
            } else if (uri.getPath().contains("serveis")) {
                response = handleGetServeis(queryToMap(uri.getQuery()));
            }
        } else if (requestMethod.equals("POST")) {
            
        } 
        return response;
    }

    private static String handleGetReserva(Map<String, String> query) {
        String response = "";
        if (!query.isEmpty()) {
            if (query.containsKey("qrcode")) {
                response = getReservesQRCode(query.get("qrcode"));
            } else if (query.containsKey("loc")) {
                response = getReservesLoc(query.get("loc"));
            } else if (query.containsKey("dni")) {
                response = getReservesDni(query.get("dni"));
                if (query.containsKey("data")) {
                    response = getReservesDniData(query.get("dni"),query.get("data"));
                }
            } else if (query.containsKey("data")) {
                response = getReservesDni(query.get("data"));
            }
        } else {
            response = getReserves();
        }
        return response;
    }

    private static String handleGetServeis(Map<String, String> query) {
        String response = "";
        if (!query.isEmpty()) {
            if (query.containsKey("treballador")) {
                String treballador = (String) query.get("treballador");
                response = "Serveis del treballador: " + treballador;
            }
        } else {
            response = "TOTS ELS SERVEIS";
        }

        return response;
    }
    
    private static String getReserves(){
        return "";
    }
    
    private static String getReservesQRCode(String qrcode) {
        return "";
    }
    
    private static String getReservesLoc(String loc) {
        return "";
    }
    
    private static String getReservesDni(String dni) {
        return "";
    }
    
    private static String getReservesData(String data) {
        return "";
    }
    
    private static String getReservesDniData(String dni, String data) {
        return "";
    }

}
