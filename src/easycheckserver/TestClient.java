/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver;

import static easycheckserver.utils.NetUtils.buildQuery;
import static easycheckserver.utils.NetUtils.buildUrl;
import easycheckserver.utils.JSonParser;
import static easycheckserver.utils.NetUtils.doPostRequest;
import static easycheckserver.utils.NetUtils.goGetRequest;
import java.net.URL;

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
        parser = new JSonParser();
        inserirTreballador("toni", "torres", "mari", "1", "jacdemanec", "xxx" );
        inserirTreballador("toni", "torres", "mari", "1", "jacdemanec", "xxx" );
        actualitzarTreballador(1,"pep", "we", "dssd", "0", "sddsdsd");
        System.out.println(obtenirTreballadors());
        System.out.println(obtenirTreballadorId("1"));
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
        return goGetRequest(url);
    }

    public String obtenirTreballadorId(String id) {
        String query = buildQuery("id", id);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/treballador", query);
        return goGetRequest(url);
    }

    public String obtenirReserves() {
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves", null);
        return goGetRequest(url);
    }

    public String obtenirReservesDni(String dni) {
        String query = buildQuery("dni", dni);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves", query);
        return goGetRequest(url);
    }

    public String obtenirReservesData(String data) {
        String query = buildQuery("data", data);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves", query);
        return goGetRequest(url);
    }

    public String obtenirReservesDniData(String dni, String data) {
        String query = buildQuery("dni", dni, "data", data);
        URL url = buildUrl(BASE_URL, PORT, "/easycheckapi/reserves", query);
        return goGetRequest(url);
    }

}
