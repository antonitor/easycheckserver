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
public class TestLogin {
    
    
    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        new TestLogin();
    }
    
    public TestLogin(){
        String user = "Antoni";
        String password = "xxx";
        String response = "";
        String query = buildQueryLogin(user,password);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/login", null);
        response = doPostRequest(url, query);
        if (response.equals("0")) {
            response = "Usuari desconegut.";
        } else if (response.equals("1")){
            response = "Contrasenya incorrecta.";
        } else if (response.equals("2")) {
            response = "Login succesful! Hola " + user + "!";
        } else {
            response = "WAT THE FAK!!??";
        }
        System.out.println(response);
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

    public String buildQueryLogin(String user, String password) {
        return "user=" + user + "&pass=" + password;
    }
}
