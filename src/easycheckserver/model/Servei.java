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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Toni
 */
@Entity
@Table(name="serveis", schema="public")
public class Servei implements Serializable{
    
    @Id
    @SequenceGenerator(name="serveis_id_seq", sequenceName="serveis_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="serveis_id_seq")
    @Column(name = "_id", updatable=false)
    private int id;
    @Column(name = "descripcio")
    private String descripcio;    
    @ManyToOne(fetch = FetchType.LAZY)
    private Treballador treballador;
    @Column(name = "data_servei")
    private String data_servei;
    @Column(name = "hora_inici")
    private String hora_inici;
    @Column(name = "hora_final")
    private String hora_final;
    @OneToMany(fetch = FetchType.LAZY, targetEntity = Reserva.class, mappedBy = "servei")
    private List<Reserva> llistaReserves = new ArrayList();

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
     * @return the descripcio
     */
    public String getDescripcio() {
        return descripcio;
    }

    /**
     * @param descripcio the descripcio to set
     */
    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    /**
     * @return the treballador
     */
    public Treballador getTreballador() {
        return treballador;
    }

    /**
     * @param treballador the treballador to set
     */
    public void setTreballador(Treballador treballador) {
        this.treballador = treballador;
    }

    /**
     * @return the data_servei
     */
    public String getData_servei() {
        return data_servei;
    }

    /**
     * @param data_servei the data_servei to set
     */
    public void setData_servei(String data_servei) {
        this.data_servei = data_servei;
    }

    /**
     * @return the hora_inici
     */
    public String getHora_inici() {
        return hora_inici;
    }

    /**
     * @param hora_inici the hora_inici to set
     */
    public void setHora_inici(String hora_inici) {
        this.hora_inici = hora_inici;
    }

    /**
     * @return the hora_final
     */
    public String getHora_final() {
        return hora_final;
    }

    /**
     * @param hora_final the hora_final to set
     */
    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    /**
     * @return the llistaReserves
     */
    public List<Reserva> getLlistaReserves() {
        return llistaReserves;
    }

    /**
     * @param llistaReserves the llistaReserves to set
     */
    public void setLlistaReserves(List<Reserva> llistaReserves) {
        this.llistaReserves.clear();
        this.llistaReserves.addAll(llistaReserves);
    }
  
    
}
