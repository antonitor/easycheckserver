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
import easycheckserver.model.Treballador;
import easycheckserver.persistencia.GestorPersistencia;
import easycheckserver.persistencia.UtilitatPersistenciaException;
import easycheckserver.utils.JSonParser;
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

    public static JSonParser parser;

    public static void main(String[] args) throws Exception {
        parser = new JSonParser();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/easycheckapi/reserves", new ReservesHandler());
        server.createContext("/easycheckapi/serveis", new ServeisHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }


    static class ReservesHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = handleReservesRequest(t);
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class ServeisHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = handleReservesRequest(t);
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class TreballadorsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = handleReservesRequest(t);
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static String handleReservesRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        URI uri = t.getRequestURI();
        if (requestMethod.equals("GET")) {
            System.out.println("HTTP GET REQUEST: " + uri);
            Map<String, String> query = queryToMap(uri.getQuery());
            if (!query.isEmpty()) {
                if (query.containsKey("qrcode")) {
                    response = parser.getReservesQRCode(query.get("qrcode"));
                } else if (query.containsKey("loc")) {
                    response = parser.getReservesLoc(query.get("loc"));
                } else if (query.containsKey("dni") && query.containsKey("data")) {
                    response = parser.getReservesDniData(query.get("dni"), query.get("data"));
                } else if (query.containsKey("data")) {
                    response = parser.getReservesDni(query.get("dni"));
                } else if (query.containsKey("data")) {
                    response = parser.getReservesDni(query.get("data"));
                }
            } else {
                response = parser.getReserves();
            }
        } else if (requestMethod.equals("POST")) {
            System.out.println("HTTP POST REQUEST: " + uri);
        }
        return response;
    }

    private static String handleServeisRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        URI uri = t.getRequestURI();
        Map<String, String> query = queryToMap(uri.getQuery());
        if (requestMethod.equals("GET")) {
            if (!query.isEmpty()) {
                if (query.containsKey("treballador")) {
                    String treballador = (String) query.get("treballador");
                    response = "Serveis del treballador: " + treballador;
                }
            } else {
                response = "TOTS ELS SERVEIS";
            }
        }
        return response;
    }
}