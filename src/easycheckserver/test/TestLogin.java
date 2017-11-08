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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toni
 */
public class TestLogin {
    
    
    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    private static final String USER = "toni";
    private static final String PASSWORD = "xxx";

    public static void main(String[] args) {
        new TestLogin().login(USER, PASSWORD);
    }
 
    
    public String login(String user, String password){
        String query = buildQueryLogin(user,password);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/login", null);
        return doPostRequest(url, query);
    }
    
    public static String doPostRequest(URL url, String parameters) {
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
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            String responseBody = "";
            if (connection.getResponseCode() == 200) {
                InputStream response = connection.getInputStream();
                Scanner scanner = new Scanner(response);
                responseBody = scanner.useDelimiter("\\A").next();
            }
            
            System.out.println(responseBody);
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
