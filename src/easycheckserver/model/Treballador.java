/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Toni
 */
@Entity
@Table(name="treballador", schema="public")
public class Treballador implements Serializable{
    @Id
    @SequenceGenerator(name="treballador_id_seq", sequenceName="treballador_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="treballador_id_seq")
    @Column(name = "_id", updatable=false)
    private int id;
    @Column(name = "nom")
    private String nom;
    @Column(name = "cognom1")
    private String cognom1;
    @Column(name = "cognom2")
    private String cognom2;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "esAdmin")
    private boolean esAdmin;
    @OneToMany(fetch = FetchType.LAZY, targetEntity = Servei.class, mappedBy = "treballador")
    private final List<Servei> llistaServeis = new ArrayList();
    
    public Treballador(){
        
    }

    public Treballador(String nom, String cognom1, String cognom2, String login, String password, boolean esAdmin) {
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.login = login;
        this.password = password;
        this.esAdmin = esAdmin;
    }
    
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
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the cognom1
     */
    public String getCognom1() {
        return cognom1;
    }

    /**
     * @param cognom1 the cognom1 to set
     */
    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    /**
     * @return the cognom2
     */
    public String getCognom2() {
        return cognom2;
    }

    /**
     * @param cognom2 the cognom2 to set
     */
    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the esAdmin
     */
    public boolean getEsAdmin() {
        return esAdmin;
    }

    /**
     * @param esAdmin the esAdmin to set
     */
    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    /**
     * @return the llistaServeis
     */
    public List<Servei> getLlistaServeis() {
        return llistaServeis;
    }

    /**
     * @param llistaServeis the llistaServeis to set
     */
    public void setLlistaServeis(List<Servei> llistaServeis) {
        this.llistaServeis.clear();
        this.llistaServeis.addAll(llistaServeis);
    }
    
    @Override
    public String toString() {
        return nom + " " + cognom1 + " " + cognom2;
    }
}
