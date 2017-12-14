/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
 */
package easycheckserver.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Grup de mètodes static per desenvolupar tasques símples
 * 
 * @author Toni
 */
public class NetUtils {

    /**
     * Mètode utilitzat als jocs de proves per tal d'executar una petició
     * http amb el mètode GET amb la URL que obtè per paràmetre.
     * 
     * @param url URL de la petició GET
     * @return cadena de caràcters amb la resposta a la petició
     */
    public static String doGetRequest(URL url) {
        String responseBody = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();            
            System.out.println("Response: " + connection.getResponseMessage() + " " + responseCode);
            System.out.println("Content-Type: " + connection.getContentType());
            System.out.println();
            if (responseCode == 200) {
                InputStream response = connection.getInputStream();
                Scanner scanner = new Scanner(response);
                responseBody = scanner.useDelimiter("\\A").next();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseBody;
    }

    /**
     * Mètode utilitzat als jocs de proves per tal d'executar una petició
     * http amb el mètode POST amb la URL i el query que obtè per paràmetre
     * 
     * @param url URL de la petició GET
     * @param parameters query de la petició POST
     * @return cadena de caràcters amb la resposta a la petició
     */
    public static String doPostRequest(URL url, String parameters) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            //Construeix el Header de la petició:
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");            
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Envia la petició POST:
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            //Recull la resposta de la petició POST:
            String responseBody = "";
            if (connection.getResponseCode() == 200) {
                InputStream response = connection.getInputStream();
                Scanner scanner = new Scanner(response);
                responseBody = scanner.useDelimiter("\\A").next();
            }

            return responseBody;

        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Construeix i torna un nou objecte URL amb les dades que obtè per paràmetre.
     * 
     * @param host host de l'url
     * @param port port de l'url
     * @param path path de l'url
     * @param query query de l'url
     * @return objecte URL
     */
    public static URL buildUrl(String host, int port, String path, String query) {
        try {
            return new URI("http", null, host, port, path, query, null).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Transforma el query obtingut per paràmetre en un objecte Map
     * 
     * @param query cadena de caràcters amb el query
     * @return objecte Map
     */
    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                } else {
                    result.put(pair[0], "");
                }
            }
        }
        return result;
    }
    
    
    /**
     * Prova de transformar un String en int, si falla torna un 0
     * 
     * @param integer cadena de caràcters amb l'enter
     * @return  int
     */
    public static int stringToInt(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Imprimeix per consola el títol de l'aplicació servei
     */
    public static void printSignature(){        
        System.out.println();
        System.out.println(" ___  __    ____   __ ____  _ ___ ____  __   __  ___ ___  _   _  ___ ___ ");
        System.out.println("| __|/  \\ /' _| `v' // _/ || | __/ _/ |/ / /' _/| __| _ \\| \\ / || __| _ \\");
        System.out.println("| _|| /\\ |`._`.`. .'| \\_| >< | _| \\_|   <  `._`.| _|| v /`\\ V /'| _|| v /");
        System.out.println("|___|_||_||___/ !_!  \\__/_||_|___\\__/_|\\_\\ |___/|___|_|_\\  \\_/  |___|_|_\\");
        System.out.println();
    } 
}
