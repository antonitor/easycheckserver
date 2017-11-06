/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import static easycheckserver.EasyCheckServer.gestor;
import easycheckserver.model.Treballador;
import easycheckserver.persistencia.GestorPersistencia;
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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toni
 */
public class EasyCheckServer {

    public static JSonParser parser;
    public static GestorPersistencia gestor;
    public static LoginAuthenticator authenticator;

    private EasyCheckServer() {
    }

    public static void main(String[] args) throws Exception {
        parser = new JSonParser();
        gestor = new GestorPersistencia();
        authenticator = new LoginAuthenticator("/easycheckapi");

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/easycheckapi/reserva", new ReservesHandler());
        server.createContext("/easycheckapi/servei", new ServeisHandler());
        server.createContext("/easycheckapi/treballador", new TreballadorsHandler());
        server.createContext("/easycheckapi/login", new LoginHandler());
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
            String response = handleServeisRequest(t);
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class TreballadorsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            System.out.println(t.getRequestHeaders());
            String response = handleTreballadorRequest(t);
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
                response = "" + gestor.updateServei(query.get("id"), query.get("descripcio"), query.get("dataservei"), query.get("horainici"), query.get("horafinal"), query.get("idtreballador"));
            } else if (query.containsKey("descripcio") && query.containsKey("dataservei") && query.containsKey("horainici") && query.containsKey("horafinal") && query.containsKey("idtreballador")) {
                response = "" + gestor.insertServei(query.get("descripcio"), query.get("dataservei"), query.get("horainici"), query.get("horafinal"), query.get("idtreballador"));
            } else if (query.containsKey("borrarid")) {
                response = "" + gestor.borrarServei(query.get("borrarid"));
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
                response = parser.getTreballadorId(query.get("id"));
            } else {
                response = parser.getTreballadors();
            }
        } else if (requestMethod.equals("POST")) {
            System.out.println("HTTP POST REQUEST: " + uri);
            query = getPostQuery(t);
            System.out.println("POST Query: " + query);
            if (query.containsKey("borrarid")) {
                response = "" + gestor.borrarTreballador(query.get("borrarid"));
            } else if (query.containsKey("id") && query.containsKey("nom") && query.containsKey("cognom1") && query.containsKey("cognom2") && query.containsKey("dni") && query.containsKey("esadmin") && query.containsKey("login")) {
                response = "" + gestor.updateTreballador(query.get("id"), query.get("nom"), query.get("cognom1"), query.get("cognom2"), query.get("dni"), query.get("esadmin"), query.get("login"));
            } else if (query.containsKey("nom") && query.containsKey("cognom1") && query.containsKey("cognom2") && query.containsKey("dni") && query.containsKey("esadmin") && query.containsKey("login") && query.containsKey("password")) {
                response = "" + gestor.insertTreballador(query.get("nom"), query.get("cognom1"), query.get("cognom2"), query.get("dni"), query.get("esadmin"), query.get("login"), query.get("password"));
            } else if (query.containsKey("idservei") && query.containsKey("idtreballador")) {
                response = "" + gestor.assignarTreballador(query.get("idservei"), query.get("idtreballador"));
            } else {
                response = "0";
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
                response = "" + gestor.login(query.get("user"), query.get("pass"));
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

class LoginAuthenticator extends BasicAuthenticator {

    public LoginAuthenticator(String string) {
        super(string);
    }

    @Override
    public boolean checkCredentials(String user, String pass) {
        boolean login = false;
        List<Treballador> llistaUsuaris = gestor.getTreballadors();
        for (Treballador treb : llistaUsuaris) {
            if (treb.getNom().equals(user)) {
                if (treb.getPassword().equals(pass)) {
                    login = true;
                }
            }
        }
        return login;
    }
}
