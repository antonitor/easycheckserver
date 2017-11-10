/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
 */
package easycheckserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import static easycheckserver.utils.NetUtils.queryToMap;
import easycheckserver.utils.JSonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toni
 * 
 * 
 */
public class EasyCheckServer {

    private static JSonParser parser;

    private EasyCheckServer() {
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        parser = new JSonParser();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/easycheckapi/reserva", new ReservesHandler());
        server.createContext("/easycheckapi/servei", new ServeisHandler());
        server.createContext("/easycheckapi/treballador", new TreballadorsHandler());
        server.createContext("/easycheckapi/login", new LoginHandler());
        server.setExecutor(null); // crea un executor per defecte
        server.start();
    }

    static class ReservesHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = handleReservesRequest(t);
            t.getResponseHeaders().add("Content-Type", "application/json");
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class ServeisHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = handleServeisRequest(t);
            t.getResponseHeaders().add("Content-Type", "application/json");
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class TreballadorsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = handleTreballadorRequest(t);
            t.getResponseHeaders().add("Content-Type", "application/json");
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class LoginHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = handleLoginRequest(t);
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
                } else if (query.containsKey("dni")) {
                    response = parser.getReservesDni(query.get("dni"));
                } else if (query.containsKey("data")) {
                    response = parser.getReservesData(query.get("data"));
                } else if (query.containsKey("servei")) {
                    response = parser.getReservesServei(query.get("servei"));
                }
            } else {
                response = parser.getReserves();
            }
        }
        return response;
    }

    private static String handleServeisRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        URI uri = t.getRequestURI();
        Map<String, String> query = queryToMap(uri.getQuery());
        if (requestMethod.equals("GET")) {
            System.out.println("HTTP GET REQUEST: " + uri);
            if (!query.isEmpty()) {
                if (query.containsKey("treballador") && query.containsKey("data") && query.containsKey("hora")) {
                    response = parser.getServeisTreballadorDataHora(query.get("treballador"), query.get("data"), query.get("hora"));
                } else if (query.containsKey("treballador") && query.containsKey("data")) {
                    response = parser.getServeisTreballadorData(query.get("treballador"), query.get("data"));
                } else if (query.containsKey("treballador")) {
                    response = parser.getServeisTreballador(query.get("treballador"));
                } else if (query.containsKey("data") && query.containsKey("hora")) {
                    response = parser.getServeisDataHora(query.get("data"), query.get("hora"));
                } else if (query.containsKey("data")) {
                    response = parser.getServeisData(query.get("data"));
                }
            } else {
                response = parser.getServeis();
            }
        } else if (requestMethod.equals("POST")) {
            query = getPostQuery(t);
            System.out.println("HTTP POST REQUEST: " + uri);
            System.out.println("POST Query: " + query);
            if (query.containsKey("id") && query.containsKey("descripcio") && query.containsKey("dataservei") && query.containsKey("horainici") && query.containsKey("horafinal") && query.containsKey("idtreballador")) {
                response = parser.actualitzarServei(query.get("id"), query.get("descripcio"), query.get("dataservei"), query.get("horainici"), query.get("horafinal"), query.get("idtreballador"));
            } else if (query.containsKey("descripcio") && query.containsKey("dataservei") && query.containsKey("horainici") && query.containsKey("horafinal") && query.containsKey("idtreballador")) {
                response = parser.inserirServei(query.get("descripcio"), query.get("dataservei"), query.get("horainici"), query.get("horafinal"), query.get("idtreballador"));
            } else if (query.containsKey("borrarid")) {
                response = parser.esborrarServei(query.get("borrarid"));
            } else {
                response = "{\n"
                        + "  \"requestCode\": 0,\n"
                        + "  \"message\": \"Query invalid.\"\n"
                        + "}";
            }
        }
        return response;
    }

    private static String handleTreballadorRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        URI uri = t.getRequestURI();
        Map<String, String> query = queryToMap(uri.getQuery());
        if (requestMethod.equals("GET")) {
            System.out.println("HTTP GET REQUEST: " + uri);
            if (query.containsKey("id")) {
                String id = query.get("id");
                response = parser.getTreballadorId(query.get("id"));
            } else {
                response = parser.getTreballadors();
            }
        } else if (requestMethod.equals("POST")) {
            System.out.println("HTTP POST REQUEST: " + uri);
            query = getPostQuery(t);
            System.out.println("POST Query: " + query);
            if (query.containsKey("borrarid")) {
                response = parser.esborrarTreballador(query.get("borrarid"));
            } else if (query.containsKey("id") && query.containsKey("nom") && query.containsKey("cognom1") && query.containsKey("cognom2") && query.containsKey("dni") && query.containsKey("esadmin") && query.containsKey("login") && query.containsKey("password")) {
                response = parser.actualitzarTreballador(query.get("id"), query.get("nom"), query.get("cognom1"), query.get("cognom2"), query.get("dni"), query.get("esadmin"), query.get("login"), query.get("password"));
            } else if (query.containsKey("nom") && query.containsKey("cognom1") && query.containsKey("cognom2") && query.containsKey("dni") && query.containsKey("esadmin") && query.containsKey("login") && query.containsKey("password")) {
                response = parser.inserirTreballador(query.get("nom"), query.get("cognom1"), query.get("cognom2"), query.get("dni"), query.get("esadmin"), query.get("login"), query.get("password"));
            } else if (query.containsKey("idservei") && query.containsKey("idtreballador")) {
                response = parser.assignarTreballador(query.get("idservei"), query.get("idtreballador"));
            } else {
                response = "{\n"
                        + "  \"requestCode\": 0,\n"
                        + "  \"message\": \"Query invalid.\"\n"
                        + "}";
            }
        }
        return response;
    }

    private static String handleLoginRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        if (requestMethod.equals("POST")) {
            System.out.println("HTTP POST REQUEST: " + t.getRequestURI());
            Map<String, String> query = getPostQuery(t);
            System.out.println("POST Query: " + query);
            if (query.containsKey("user") && query.containsKey("pass")) {
                response = parser.Login(query.get("user"), query.get("pass"));
            }
        }
        return response;
    }

    private static Map<String, String> getPostQuery(HttpExchange t) {
        InputStreamReader isr;
        Map<String, String> query = new HashMap<>();
        try {
            isr = new InputStreamReader(t.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            query = queryToMap(br.readLine());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EasyCheckServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EasyCheckServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query;
    }

}
