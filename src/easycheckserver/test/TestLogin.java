/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.test;

import static easycheckserver.utils.NetUtils.buildUrl;
import static easycheckserver.utils.NetUtils.doPostRequest;
import java.net.URL;

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
    

    public String buildQueryLogin(String user, String password) {
        return "user=" + user + "&pass=" + password;
    }
}
