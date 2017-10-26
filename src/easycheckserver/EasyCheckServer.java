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
import java.util.HashMap;
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
        URI uri = t.getRequestURI();
        System.out.println("URL: " + uri);
        if (uri.getPath().contains("reserves")) {
            response = handleReserva(queryToMap(uri.getQuery()));
        } else if (uri.getPath().contains("serveis")) {
            response = handleServeis(queryToMap(uri.getQuery()));
        }
        return response;
    }


    private static String handleReserva(Map<String, String> query) {
        String response = "";
        if (!query.isEmpty()) {
            if (query.containsKey("qrcode")) {
                String qrcode = (String) query.get("qrcode");
                response = "Reserves amb QRCode: " + qrcode;
            } else if (query.containsKey("loc")) {
                String loc = (String) query.get("loc");
                response = "Reserves amb Localitzador: " + loc;
            } else if (query.containsKey("dni")) {
                String dni = (String) query.get("dni");
                response = "Reserves amb DNI: " + dni;
                if (query.containsKey("data")) {
                    String data = (String) query.get("data");
                    response = response + " " + "i data: " + data;
                }
            } else if (query.containsKey("data")) {
                String data = (String) query.get("data");
                response = "Reserves amb data: " + data;
            }
        } else {
            response = "TOTES LES RESERVES";
        }
        return response;
    }

    private static String handleServeis(Map<String, String> query) {
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

}
