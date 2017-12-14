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
import static easycheckserver.utils.NetUtils.printSignature;
import static easycheckserver.utils.NetUtils.stringToInt;
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

/*      
 _______                     ______ __                __      _______                              
|    ___|.---.-.-----.--.--.|      |  |--.-----.----.|  |--. |     __|.-----.----.--.--.-----.----.
|    ___||  _  |__ --|  |  ||   ---|     |  -__|  __||    <  |__     ||  -__|   _|  |  |  -__|   _|
|_______||___._|_____|___  ||______|__|__|_____|____||__|__| |_______||_____|__|  \___/|_____|__|                      |_____|                                                                       

@Author: Toni
*/
public class EasyCheckServer {

    private static JSonParser parser;
    private final static int DEFAULT_PORT = 8080;

    private EasyCheckServer() {
    }

    /**
     * Posada en marxa del servidor http
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = serverArgs(args);
        parser = new JSonParser();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/easycheckapi/reserva", new ReservesHandler());
        server.createContext("/easycheckapi/servei", new ServeisHandler());
        server.createContext("/easycheckapi/treballador", new TreballadorsHandler());
        server.createContext("/easycheckapi/login", new LoginHandler());
        server.setExecutor(null); // crea un executor per defecte
        server.start();
    }

    /**
     * Accepta els arguments -h --help mostrant un petit comentari d'ajuta.
     * I els arguments -p --port engegant el servei amb el port indicat
     * 
     * @param args arguments
     * @return port indicat si és correcte
     */
    private static int serverArgs(String[] args) {
        printSignature();
        if (args.length == 0) {
            System.out.println("Runing EasyCheck Server listening on default Port 8080...");
            return DEFAULT_PORT;
        } else if (args.length > 2) {
            System.out.println("Argument invàlid " + args[0]);
        } else if (args[0].equals("-h") || args[0].equals("--help") || args[0].equals("?")) {
            System.out.println("\n EasyCheckServer [OPTIONS] \n");
            System.out.println("-p \t --port \t Port the server will listen to.\n");
        } else if (args[0].equals("-p") || args[0].equals("--port")) {
            int port = stringToInt(args[1]);
            if (port != 0) {
                System.out.println("Runing EasyCheck Server listening on Port " + port + "...");
                return port;
            } else {
                System.out.println("Argument invàlid " + args[1]);
            }
        } else {
            System.out.println("Argument invàlid " + args[0]);
        }
        System.exit(0);
        return -1;
    }

    /**
     * Una nova instància d'aquesta classe serà creada cada cop que es rebi una
     * petició http al directori reserva.
     *
     * Obté la resposta a la petició del mètode handleReservesRequest, formata
     * el header i envía la resposta.
     */
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

    /**
     * Una nova instància d'aquesta classe serà creada cada cop que es rebi una
     * petició http al directori servei.
     *
     * Obté la resposta a la petició del mètode handleServeisRequest, formata el
     * header i envía la resposta.
     */
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

    /**
     * Una nova instància d'aquesta classe serà creada cada cop que es rebi una
     * petició http al directori treballador.
     *
     * Obté la resposta a la petició del mètode handleTreballadorRequest,
     * formata el header i envía la resposta.
     */
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

    /**
     * Una nova instància d'aquesta classe serà creada cada cop que es rebi una
     * petició http al directori login.
     *
     * Obté la resposta a la petició del mètode handleLoginRequest, formata el
     * header i envía la resposta.
     */
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

    /**
     * Aquest mètode tan sols gestiona peticions http amb mètode GET: Extreu el
     * query de l'URI i en funció dels paràmetres d'aquest query truca un mètode
     * de la classe JsonParser del que obtindrà el resultat de la consulta en
     * formàt Json
     *
     * @param t objecte HttpExange
     * @return cadena de caràcters amb la resposta en format json
     */
    private static String handleReservesRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        URI uri = t.getRequestURI();
        Map<String, String> query = queryToMap(uri.getQuery());
        if (requestMethod.equals("GET")) {
            System.out.println("HTTP GET REQUEST: " + uri + " FROM " + t.getRemoteAddress());            
            if (!query.isEmpty()) {
                if (query.containsKey("qrcode")) {
                    response = parser.getReservesQRCode(query.get("qrcode"));
                    System.out.println("GET reserves amb QRCode " + query.get("qrcode"));
                } else if (query.containsKey("loc")) {
                    response = parser.getReservesLoc(query.get("loc"));
                    System.out.println("GET reserves amb Localitzador " + query.get("loc"));
                } else if (query.containsKey("dni") && query.containsKey("data")) {
                    response = parser.getReservesDniData(query.get("dni"), query.get("data"));
                    System.out.println("GET reserves amb dni " + query.get("dni") + " i data " + query.get("data"));
                } else if (query.containsKey("dni")) {
                    response = parser.getReservesDni(query.get("dni"));
                    System.out.println("GET reserves amb dni " + query.get("dni"));
                } else if (query.containsKey("data")) {
                    response = parser.getReservesData(query.get("data"));
                    System.out.println("GET reserves amb data " + query.get("data"));
                } else if (query.containsKey("servei")) {
                    response = parser.getReservesServei(query.get("servei"));
                    System.out.println("GET reserves del servei " + query.get("servei"));
                }
            } else {
                response = parser.getReserves();
            }
        } else if (requestMethod.equals("POST")) {
            query = getPostQuery(t);
            System.out.println("HTTP POST REQUEST: " + uri + " FROM " + t.getRemoteAddress());
            if (query.containsKey("checkin")) {
                System.out.println("Check-In de la reserva " + query.get("checkin"));
                response = parser.checkIn(query.get("checkin"));                
            } else {
                response = "{\n"
                        + "  \"requestCode\": 0,\n"
                        + "  \"message\": \"Query invalid.\"\n"
                        + "}";
            }
        }
        return response;
    }

    /**
     * Aquest mètode gestiona peticions http amb mètode GET o POST: Si és GET
     * extreu el query de l'URI i en funció dels paràmetres d'aquest query truca
     * un mètode de la classe JsonParser del que obtindrà el resultat de la
     * consulta en formàt Json
     *
     * Si és POST extreu el query del cos de la petició mitjançant el mètode
     * getPostQuery, truca el mètode de la classe JsonParser corresponent i
     * retorna el resultat en formàt Json.
     *
     * @param t objecte HttpExange
     * @return cadena de caràcters amb la resposta en format json
     */
    private static String handleServeisRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        URI uri = t.getRequestURI();
        Map<String, String> query = queryToMap(uri.getQuery());
        if (requestMethod.equals("GET")) {
            System.out.println("HTTP GET REQUEST: " + uri + " FROM " + t.getRemoteAddress());
            if (!query.isEmpty()) {
                if (query.containsKey("treballador") && query.containsKey("data") && query.containsKey("hora")) {
                    response = parser.getServeisTreballadorDataHora(query.get("treballador"), query.get("data"), query.get("hora"));
                    System.out.println("GET serveis del treballador: " + query.get("treballador") + ", data: " + query.get("data") + ", hora: " + query.get("hora"));
                } else if (query.containsKey("treballador") && query.containsKey("data")) {
                    response = parser.getServeisTreballadorData(query.get("treballador"), query.get("data"));
                    System.out.println("GET serveis del treballador: " + query.get("treballador") + ", data: " + query.get("data"));
                } else if (query.containsKey("treballador")) {
                    response = parser.getServeisTreballador(query.get("treballador"));
                    System.out.println("GET serveis del treballador: " + query.get("treballador"));
                } else if (query.containsKey("data") && query.containsKey("hora")) {
                    response = parser.getServeisDataHora(query.get("data"), query.get("hora"));
                    System.out.println("GET serveis amb data " + query.get("data") + " i hora " + query.get("hora"));
                } else if (query.containsKey("data")) {
                    response = parser.getServeisData(query.get("data"));
                    System.out.println("GET serveis amb data " + query.get("data"));
                }
            } else {
                response = parser.getServeis();
                System.out.println("GET tots els serveis");
            }
        } else if (requestMethod.equals("POST")) {
            query = getPostQuery(t);
            System.out.println("HTTP POST REQUEST: " + uri + " FROM " + t.getRemoteAddress());
            if (query.containsKey("id") && query.containsKey("descripcio") && query.containsKey("dataservei") && query.containsKey("horainici") && query.containsKey("horafinal") && query.containsKey("idtreballador")) {
                System.out.println("Actualitzar servei: " + query.get("descripcio"));
                response = parser.actualitzarServei(query.get("id"), query.get("descripcio"), query.get("dataservei"), query.get("horainici"), query.get("horafinal"), query.get("idtreballador"));
            } else if (query.containsKey("descripcio") && query.containsKey("dataservei") && query.containsKey("horainici") && query.containsKey("horafinal") && query.containsKey("idtreballador")) {
                System.out.println("Inserir servei: " + query.get("descripcio"));
                response = parser.inserirServei(query.get("descripcio"), query.get("dataservei"), query.get("horainici"), query.get("horafinal"), query.get("idtreballador"));
            } else if (query.containsKey("borrarid")) {
                System.out.println("Esborrar servei: " + query.get("borrarid"));
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

    /**
     * Aquest mètode gestiona peticions http amb mètode GET o POST: Si és GET
     * extreu el query de l'URI i en funció dels paràmetres d'aquest query truca
     * un mètode de la classe JsonParser del que obtindrà el resultat de la
     * consulta en formàt Json
     *
     * Si és POST extreu el query del cos de la petició mitjançant el mètode
     * getPostQuery, truca el mètode de la classe JsonParser corresponent i
     * retorna el resultat en formàt Json.
     *
     * @param t objecte HttpExange
     * @return cadena de caràcters amb la resposta en format json
     */
    private static String handleTreballadorRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        URI uri = t.getRequestURI();
        Map<String, String> query = queryToMap(uri.getQuery());
        if (requestMethod.equals("GET")) {
            System.out.println("HTTP GET REQUEST: " + uri + " FROM " + t.getRemoteAddress());
            if (query.containsKey("id")) {
                response = parser.getTreballadorId(query.get("id"));
                System.out.println("GET trebalador: " + query.get("id"));
            } else {
                response = parser.getTreballadors();
                System.out.println("GET tots els treballadors.");
            }
        } else if (requestMethod.equals("POST")) {
            System.out.println("HTTP POST REQUEST: " + uri + " FROM " + t.getRemoteAddress());
            query = getPostQuery(t);
            if (query.containsKey("borrarid")) {
                System.out.println("Borrar treballador: " + query.get("borrarid"));
                response = parser.esborrarTreballador(query.get("borrarid"));
            } else if (query.containsKey("id") && query.containsKey("nom") && query.containsKey("cognom1") && query.containsKey("cognom2") && query.containsKey("dni") && query.containsKey("esadmin") && query.containsKey("login") && query.containsKey("password")) {
                System.out.println("Actualitzar trebalador: " + query.get("nom") + " " + query.get("cognom1"));
                response = parser.actualitzarTreballador(query.get("id"), query.get("nom"), query.get("cognom1"), query.get("cognom2"), query.get("dni"), query.get("esadmin"), query.get("login"), query.get("password"));
            } else if (query.containsKey("nom") && query.containsKey("cognom1") && query.containsKey("cognom2") && query.containsKey("dni") && query.containsKey("esadmin") && query.containsKey("login") && query.containsKey("password")) {
                System.out.println("Inserir trebalador: " + query.get("nom") + " " + query.get("cognom1"));
                response = parser.inserirTreballador(query.get("nom"), query.get("cognom1"), query.get("cognom2"), query.get("dni"), query.get("esadmin"), query.get("login"), query.get("password"));
            } else if (query.containsKey("idservei") && query.containsKey("idtreballador")) {
                System.out.println("Assignar treballador " + query.get("idtreballador") + " al servei " + query.get("idservei"));
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

    /**
     * Aquest mètode gestiona peticions http amb mètode POST: Extreu el query
     * del cos de la petició mitjançant el mètode getPostQuery, truca el mètode
     * de la classe JsonParser corresponent i retorna el resultat en formàt
     * Json.
     *
     * @param t objecte HttpExange
     * @return cadena de caràcters amb la resposta en format json
     */
    private static String handleLoginRequest(HttpExchange t) {
        String response = "";
        String requestMethod = t.getRequestMethod();
        if (requestMethod.equals("POST")) {
            System.out.println("HTTP POST REQUEST: " + t.getRequestURI() + " FROM " + t.getRemoteAddress());
            Map<String, String> query = getPostQuery(t);
            if (query.containsKey("user") && query.containsKey("pass")) {
                System.out.println("Login: " + query.get("user"));
                response = parser.Login(query.get("user"), query.get("pass"));
            }
        }
        return response;
    }

    /**
     * Obté el query del body de la petició post i la retorna com un objecte
     * Map<String, String>
     *
     * @param t objecte HttpExange
     * @return objecte Map<String, String>
     */
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
