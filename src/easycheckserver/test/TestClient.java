/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import easycheckserver.model.Reserva;
import easycheckserver.model.Servei;
import easycheckserver.model.Treballador;
import static easycheckserver.utils.NetUtils.buildQuery;
import easycheckserver.utils.JSonParser;
import static easycheckserver.utils.NetUtils.doPostRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toni
 */
public final class TestClient {

    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;
    private JSonParser parser;

    public static void main(String[] args) {
        TestClient test = new TestClient();
    }

    public TestClient() {

        obtenirTreballadorsDelServer();
        //parser = new JSonParser();
        //inserirTreballador("toni", "torres", "mari", "1", "jacdemanec", "xxx" );
        //inserirTreballador("toni", "torres", "mari", "1", "jacdemanec", "xxx" );
        //actualitzarTreballador(1,"pep", "we", "dssd", "0", "sddsdsd");
        //System.out.println(obtenirTreballadors());
        //System.out.println(obtenirTreballadorId("1"));
    }

    public void actualitzarTreballador(int id, String nom, String cognom1, String cognom2, String esadmin, String login) {
        String query = "id=" + id + "&nom=" + nom + "&cognom1=" + cognom1 + "&cognom2=" + cognom2 + "&esadmin=" + esadmin + "&login=" + login;
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        doPostRequest(url, query);
    }

    public void inserirTreballador(String nom, String cognom1, String cognom2, String esadmin, String login, String password) {
        String query = "nom=" + nom + "&cognom1=" + cognom1 + "&cognom2=" + cognom2 + "&esadmin=" + esadmin + "&login=" + login + "&password=" + password;
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        doPostRequest(url, query);
    }

    public String obtenirTreballadors() {
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        System.out.println(url.toString());
        return doGetRequest(url);
    }

    public String obtenirTreballadorId(String id) {
        String query = buildQuery("id", id);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", query);
        return doGetRequest(url);
    }

    public String obtenirReserves() {
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves", null);
        return doGetRequest(url);
    }

    public String obtenirReservesDni(String dni) {
        String query = buildQuery("dni", dni);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves", query);
        return doGetRequest(url);
    }

    public String obtenirReservesData(String data) {
        String query = buildQuery("data", data);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves", query);
        return doGetRequest(url);
    }

    public String obtenirReservesDniData(String dni, String data) {
        String query = buildQuery("dni", dni, "data", data);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves", query);
        return doGetRequest(url);
    }

    
    public String doGetRequest(URL url) {
        String responseBody = "";
        try {
            URLConnection connection = url.openConnection();
            InputStream response = connection.getInputStream();
            Scanner scanner = new Scanner(response);
            responseBody = scanner.useDelimiter("\\A").next();
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseBody;
    }
    
    public URL buildUrl(String host, int port, String path, String query) {
        try {
            return new URI("http", null, host, port, path, query, null).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Treballador> obtenirTreballadorsDelServer() {
        String json = "";
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", null);
        json = doGetRequest(url);
        Gson gson = new Gson();
        java.lang.reflect.Type tipusLlistaDeTreballadors = new TypeToken<List<Treballador>>() {
        }.getType();
        ArrayList<Treballador> llistaDeTreballadors = gson.fromJson(json, tipusLlistaDeTreballadors);

        for (Treballador treb : llistaDeTreballadors) {
            System.out.println(treb.getNom());
            System.out.println(treb.getCognom1());
            System.out.println(treb.getCognom2());
            System.out.println(treb.getLogin());
            ArrayList<Servei> llistaServeis = (ArrayList<Servei>) treb.getLlistaServeis();
            for (Servei serv : llistaServeis) {
                System.out.println("\t" + serv.getDescripcio());
                System.out.println("\t" + serv.getData_servei());
                System.out.println("\t" + serv.getHora_inici());
                ArrayList<Reserva> llistaReserves = (ArrayList<Reserva>) serv.getLlistaReserves();
                for (Reserva res : llistaReserves) {
                    System.out.println("\t\t" + res.getNom_titular());
                    System.out.println("\t\t" + res.getCognom1_titular());
                    System.out.println("\t\t" + res.getCognom2_titular());
                    System.out.println("\t\t" + res.getDni_titular());
                    System.out.println("\t\t" + res.getEmail_titular());
                }
            }
        }

        return llistaDeTreballadors;
    }

}
