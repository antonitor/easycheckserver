/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.utils;

import easycheckserver.persistencia.GestorPersistencia;

/**
 *
 * @author Toni
 */
public class JSonParser {
    
    private GestorPersistencia gestor;
    
    public JSonParser(){
        gestor = new GestorPersistencia();
    }
    
    public String getReserves() {
        return "";
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
        return "";
    }
    
    public String getTreballadors() {
        return "";
    }
}
