/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.test;

import easycheckserver.test.TestClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import easycheckserver.model.Reserva;
import easycheckserver.model.Servei;
import easycheckserver.model.Treballador;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toni
 */
public class TestDescargaTodo {

    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        TestDescargaTodo test = new TestDescargaTodo();
    }

    public TestDescargaTodo() {
        obtenirTreballadorsDelServer();
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
        } catch (URISyntaxException ex) {
            Logger.getLogger(TestDescargaTodo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TestDescargaTodo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
            System.out.println("Treballador id: " + treb.getId());
            System.out.println(treb.getNom() + " " + treb.getCognom1() + " " + treb.getCognom2());
            System.out.println("Dni: " + treb.getDni());
            System.out.println("Login: " + treb.getLogin());
            System.out.println("Es admin? " + (treb.getEsAdmin() == 1 ? "sí" : "no"));
            for (Servei serv : treb.getLlistaServeis()) {
                System.out.println("\t- Servei id: " + serv.getId());
                System.out.println("\t" + serv.getDescripcio());
                System.out.println("\t" + serv.getData_servei());
                System.out.println("\t" + serv.getHora_inici() + " - "  + serv.getHora_final());
                for (Reserva res : serv.getLlistaReserves()) {
                    System.out.println("\t\t - Reserva id: " + res.getId());
                    System.out.println("\t\t " + res.getNom_titular() + " " + res.getCognom1_titular() + " " + res.getCognom2_titular());
                    System.out.println("\t\t Dni: " + res.getDni_titular());
                    System.out.println("\t\t email: " + res.getEmail_titular());
                    System.out.println("\t\t Telf: " + res.getTelefon_titular());
                    System.out.println("\t\t Loc: " + res.getLocalitzador());
                    System.out.println("\t\t QRCode: " + res.getQr_code());
                    System.out.println("\t\t Check-In: " + (res.getCheckin() == 1 ? "Realitzat" : "No realitzat"));
                }
            }
        }

        return llistaDeTreballadors;
    }
}
