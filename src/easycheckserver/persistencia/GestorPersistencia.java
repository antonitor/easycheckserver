/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.persistencia;

import easycheckserver.model.Treballador;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Toni
 */
public class GestorPersistencia {

    EntityManagerFactory emf;
    EntityManager em;

    public void obrir() throws UtilitatPersistenciaException {
        try {
            emf = Persistence.createEntityManagerFactory("EasyCheckServerPU");
            em = emf.createEntityManager();
        } catch (Exception e) {
            throw e;
        }
    }

    public void tancar() {
        try {
            em.close();
            emf.close();
        } catch (Exception e) {

        }
    }

    public void inserirTreballador(Treballador treballador) throws UtilitatPersistenciaException {
        try {
            em.getTransaction().begin();
            em.persist(treballador);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("");
        }
    }

    public Treballador obtenirTreballador(int id) throws UtilitatPersistenciaException {
        Treballador treballador;
        try {
            treballador = em.find(Treballador.class, id);
            if (treballador == null) {
                throw new UtilitatPersistenciaException("Clau inexistent");
            }
        } catch (Exception e) {
            throw new UtilitatPersistenciaException(" " + e.toString());
        }
        return treballador;
    }

    public void modificarTreballador(Treballador treballador) throws UtilitatPersistenciaException {
        try {
            em.getTransaction().begin();
            em.merge(treballador);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("Impossible actualitzar treballador " + e.toString());
        }
    }

    public void modificarTreballador(int id) throws UtilitatPersistenciaException {
        modificarTreballador(obtenirTreballador(id));
    }

    public void esborrarTreballador(Treballador treballador) throws UtilitatPersistenciaException {
        try {
            em.getTransaction().begin();
            em.remove(treballador);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("Impossible eliminar treballador " + e.toString());
        }
    }
    
    public void esborrarTreballador(int id) throws UtilitatPersistenciaException {
        esborrarTreballador(obtenirTreballador(id));
    }

}
