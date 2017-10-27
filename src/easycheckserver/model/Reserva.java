/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Reserva implements Serializable {
    
   
    private int id;
    private int id_servei;
    private String localitzador;
    private String data_reserva;
    private String nom_titular;
    private String cognom1_titular;
    private String cognom2_titular;
    private String telefon_titular;
    private String email_titular;
    private String qr_code;
    private String dni_titular;
    private boolean checkin;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the localitzador
     */
    public String getLocalitzador() {
        return localitzador;
    }

    /**
     * @param localitzador the localitzador to set
     */
    public void setLocalitzador(String localitzador) {
        this.localitzador = localitzador;
    }

    /**
     * @return the data_reserva
     */
    public String getData_reserva() {
        return data_reserva;
    }

    /**
     * @param data_reserva the data_reserva to set
     */
    public void setData_reserva(String data_reserva) {
        this.data_reserva = data_reserva;
    }

    /**
     * @return the nom_titular
     */
    public String getNom_titular() {
        return nom_titular;
    }

    /**
     * @param nom_titular the nom_titular to set
     */
    public void setNom_titular(String nom_titular) {
        this.nom_titular = nom_titular;
    }

    /**
     * @return the cognom1_titular
     */
    public String getCognom1_titular() {
        return cognom1_titular;
    }

    /**
     * @param cognom1_titular the cognom1_titular to set
     */
    public void setCognom1_titular(String cognom1_titular) {
        this.cognom1_titular = cognom1_titular;
    }

    /**
     * @return the cognom2_titular
     */
    public String getCognom2_titular() {
        return cognom2_titular;
    }

    /**
     * @param cognom2_titular the cognom2_titular to set
     */
    public void setCognom2_titular(String cognom2_titular) {
        this.cognom2_titular = cognom2_titular;
    }

    /**
     * @return the telefon_titular
     */
    public String getTelefon_titular() {
        return telefon_titular;
    }

    /**
     * @param telefon_titular the telefon_titular to set
     */
    public void setTelefon_titular(String telefon_titular) {
        this.telefon_titular = telefon_titular;
    }

    /**
     * @return the email_titular
     */
    public String getEmail_titular() {
        return email_titular;
    }

    /**
     * @param email_titular the email_titular to set
     */
    public void setEmail_titular(String email_titular) {
        this.email_titular = email_titular;
    }

    /**
     * @return the qr_code
     */
    public String getQr_code() {
        return qr_code;
    }

    /**
     * @param qr_code the qr_code to set
     */
    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    /**
     * @return the dni_titular
     */
    public String getDni_titular() {
        return dni_titular;
    }

    /**
     * @param dni_titular the dni_titular to set
     */
    public void setDni_titular(String dni_titular) {
        this.dni_titular = dni_titular;
    }

    /**
     * @return the checkin
     */
    public boolean getCheckin() {
        return checkin;
    }

    /**
     * @param checkin the checkin to set
     */
    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }
    
}
