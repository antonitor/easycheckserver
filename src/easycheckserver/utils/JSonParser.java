/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import easycheckserver.model.Reserva;
import easycheckserver.model.Servei;
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

    public JSonParser() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gestor = new GestorPersistencia();
    }

    public String getReserves() {
        gestor.open();
        List<Reserva> llista = gestor.getReserves();
        gestor.close();
        return gson.toJson(llista);
    }

    public String getReservesServei(String idServei) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesServei(stringToInt(idServei));
        gestor.close();
        return gson.toJson(llista);
    }

    public String getReservesQRCode(String qrcode) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesQRCode(qrcode);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getReservesLoc(String loc) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesLoc(loc);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getReservesDni(String dni) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesDni(dni);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getReservesData(String data) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesData(data);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getReservesDniData(String dni, String data) {
        gestor.open();
        List<Reserva> llista = gestor.getReservesDniData(dni, data);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getServeis() {
        gestor.open();
        List<Servei> llista = gestor.getServeis();
        gestor.close();
        return gson.toJson(llista);
    }

    public String getServeisTreballador(String treballadorID) {
        gestor.open();
        List<Servei> llista = gestor.getServeisTreballador(stringToInt(treballadorID));
        gestor.close();
        return gson.toJson(llista);
    }

    public String getServeisTreballadorData(String treballadorID, String data) {
        gestor.open();
        List<Servei> llista = gestor.getServeisTreballadorData(stringToInt(treballadorID), data);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getServeisTreballadorDataHora(String treballadorID, String data, String hora) {
        gestor.open();
        List<Servei> llista = gestor.getServeisTreballadorDataHora(stringToInt(treballadorID), data, hora);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getServeisData(String data) {
        gestor.open();
        List<Servei> llista = gestor.getServeisData(data);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getServeisDataHora(String data, String hora) {
        gestor.open();
        List<Servei> llista = gestor.getServeisDataHora(data, hora);
        gestor.close();
        return gson.toJson(llista);
    }

    public String getTreballadorId(String id) {
        gestor.open();
        Treballador treballador = gestor.getTreballadorId(stringToInt(id));
        gestor.close();
        return gson.toJson(treballador);
    }

    public String getTreballadors() {
        gestor.open();
        List<Treballador> llista = gestor.getTreballadors();
        gestor.close();
        return gson.toJson(llista);
    }

    private int stringToInt(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
