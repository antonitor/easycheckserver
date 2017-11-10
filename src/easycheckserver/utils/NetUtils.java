/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Toni
 */
public class NetUtils {

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

    public static URL buildUrl(String host, int port, String path, String query) {
        try {
            return new URI("http", null, host, port, path, query, null).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

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
            System.out.println(result.toString());
        }
        return result;
    }

}
