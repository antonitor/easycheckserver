/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toni
 */
public class TestPostMethod {

    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        TestPostMethod test = new TestPostMethod();
    }

    public TestPostMethod() {
        //inserirServei("Mallorca - Menorca", "11/11/2017","10:00","11:00",1);
        //actualitzarServei(10,"Mallorca - Menorca", "11/11/2017","10:00","21:00",1);
        //borrarServei(10);
        //borrarTreballador("1");
        //assignarTreballador("7", "3");
        //inserirTreballador("Carles", "Puig", "Puigdemont", "44444444K", "Puchi", "xxx", 0);
        //actualitzarTreballador(4,"Carles", "TEST DE MODIFICACIó", "Puigdemont", "44444444K", "Puchi", "xxx", 0);
    }

  
    public String borrarServei(int idServei) {
        String response = "";
        String query = buildQueryBorrarServei(idServei);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        response = doPostRequest(url, query);
        if (response.charAt(0)!=('0')) {
            System.out.println("Borrat servei " + idServei);
        }
        return response;
    }

    public String inserirServei(String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        String response = "";
        String query = buildQueryInserirServei(descripcio, dataservei, horaInici, horaFinal, idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        response = doPostRequest(url, query);
        if (!response.equals("0")) {
            System.out.println("Inserit servei " + descripcio);
        }
        return response;
    }

    public String actualitzarServei(int id, String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        String response = "";
        String query = buildQueryActualitzarServei(id, descripcio, dataservei, horaInici, horaFinal, idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/servei", null);
        response = doPostRequest(url, query);
        if (!response.equals("0")) {
            System.out.println("Actualitzat servei " + descripcio + " amb id " + id);
        }
        return response;
    }

    public String borrarTreballador(String idTreballador) {
        String response = "";
        String query = buildQueryBorrarTreballador(idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        response = doPostRequest(url, query);
        if (response.charAt(0)!=('0')) {
            System.out.println("Esborrat treballador " + idTreballador);
        } else {
            System.out.println("No s'ha pogut esborrar el treballador");
        }
        return response;
    }

    public String assignarTreballador(String idServei, String idTreballador) {
        String response = "";
        String query = buildQueryAssignarTreballador(idServei, idTreballador);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        response = doPostRequest(url, query);
        if (!response.equals("0")) {
            System.out.println("Servei " + idServei + " assignat al treballador " + idTreballador);
        }
        return response;
    }

    public String inserirTreballador(String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        String response = "";
        String query = buildQueryInserirTreballador(nom, cognom1, cognom2, dni, login, password, esadmin);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        response = doPostRequest(url, query);
        if (!response.equals("0")) {
            System.out.println("Inserit treballador " + nom);
        }
        return response;
    }

    public String actualitzarTreballador(int id, String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        String response = "";
        String query = buildQueryActualitzarTreballador(id, nom, cognom1, cognom2, dni, login, password, esadmin);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        response = doPostRequest(url, query);
        if (!response.equals("0")) {
            System.out.println("Actualitzat treballador " + nom + " amb id " + id);
        }
        return response;
    }

    public static String doPostRequest(URL url, String parameters) {
        byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println(response);
            return response.toString();

        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public URL buildUrl(String host, int port, String path, String query) {
        try {
            return new URI("http", null, host, port, path, query, null).toURL();
        } catch (URISyntaxException ex) {
            Logger.getLogger(TestDescargaTodo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TestDescargaTodo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String buildQueryAssignarTreballador(String idServei, String idTreballador) {
        return "idservei=" + idServei + "&idtreballador=" + idTreballador;
    }

    public String buildQueryInserirTreballador(String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        return "nom=" + nom + "&cognom1=" + cognom1 + "&cognom2=" + cognom2 + "&dni=" + dni + "&esadmin=" + esadmin + "&login=" + login + "&password=" + password;
    }

    public String buildQueryActualitzarTreballador(int id, String nom, String cognom1, String cognom2, String dni, String login, String password, int esadmin) {
        return "id=" + id + "&nom=" + nom + "&cognom1=" + cognom1 + "&cognom2=" + cognom2 + "&dni=" + dni + "&esadmin=" + esadmin + "&login=" + login + "&password=" + password;
    }

    private String buildQueryBorrarTreballador(String idTreballador) {
        return "borrarid=" + idTreballador;
    }

    private String buildQueryBorrarServei(int idServei) {
        return "borrarid=" + idServei;
    }

    public String buildQueryActualitzarServei(int id, String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        return "id=" + id + "&descripcio=" + descripcio + "&dataservei=" + dataservei + "&horainici=" + horaInici + "&horafinal=" + horaFinal + "&idtreballador=" + idTreballador;
    }

    public String buildQueryInserirServei(String descripcio, String dataservei, String horaInici, String horaFinal, int idTreballador) {
        return "descripcio=" + descripcio + "&dataservei=" + dataservei + "&horainici=" + horaInici + "&horafinal=" + horaFinal + "&idtreballador=" + idTreballador;
    }
    
}
