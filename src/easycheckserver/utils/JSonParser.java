/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import easycheckserver.model.Reserva;
import easycheckserver.model.Treballador;
import easycheckserver.persistencia.GestorPersistencia;
import java.util.List;

/**
 *
 * @author Toni
 */
public class JSonParser {
    
    private final Gson gson;
    private GestorPersistencia gestor;
    
    public JSonParser(){
        gson = new GsonBuilder().setPrettyPrinting().create();
        gestor = new GestorPersistencia();
    }
    
    public String getReserves() {
        return "";
    }
    
    public String getReservesServei(String idServei) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesServei(stringToInt(idServei));
        gestor.close();
        return gson.toJson(llista);
    }

    public String getReservesQRCode(String qrcode) {
        return "";
    }

    public String getReservesLoc(String loc) {
        return "";
    }

    public String getReservesDni(String dni) {
        return "";
    }

    public String getReservesData(String data) {
        return "";
    }

    public String getReservesDniData(String dni, String data) {
        return "";
    }
    
    public String getServeis() {
        return "";
    }
    
    public String getServeisTreballador(String treballadorID) {
        return "";
    }
    
    public String getServeisTreballadorData(String treballadorID, String data) {
        return "";
    }
    
    public String getServeisTreballadorDataHora(String treballadorID, String data, String hora) {
        return "";
    }
    
    public String getServeisData(String data) {
        return "";
    }
    
    public String getServeisDataHora(String data, String hora) {
        return "";
    }
    
    public String getTreballadorId(String id) {
        gestor.open();
        Treballador treballador = gestor.getTreballadorId(stringToInt(id));
        gestor.close();
        return gson.toJson(treballador);
    }
    
    public String getTreballadors() {
        return "";
    }
    
    private int stringToInt(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
