/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.persistencia;

import easycheckserver.model.Reserva;
import easycheckserver.model.Servei;
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
            e.printStackTrace();
        }
    }

    public void inserirTreballador(Treballador treballador) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.persist(treballador);
            em.getTransaction().commit();
            tancar();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            tancar();
        }
    }

    public Treballador obtenirTreballador(int id) throws UtilitatPersistenciaException {        
        Treballador treballador = null;
        System.out.println(id);
        try {
            obrir();
            treballador = em.find(Treballador.class, id);
            if (treballador == null) {
                throw new UtilitatPersistenciaException("Clau inexistent");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            tancar();
        }
        return treballador;
    }

    public void modificarTreballador(Treballador treballador) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.merge(treballador);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("Impossible actualitzar treballador " + e.toString());
        }finally {
            tancar();
        }
    }

    public void modificarTreballador(int id) throws UtilitatPersistenciaException {
        modificarTreballador(obtenirTreballador(id));
    }

    public void esborrarTreballador(Treballador treballador) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.remove(treballador);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("Impossible eliminar treballador " + e.toString());
        }finally {
            tancar();
        }
    }
    
    public void esborrarTreballador(int id) throws UtilitatPersistenciaException {
        esborrarTreballador(obtenirTreballador(id));
    }

        public void inserirServei(Servei servei) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.persist(servei);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            tancar();
        }
    }

    public Servei obtenirServei(int id) throws UtilitatPersistenciaException {        
        Servei servei = null;
        System.out.println(id);
        try {
            obrir();
            servei = em.find(Servei.class, id);
            if (servei == null) {
                throw new UtilitatPersistenciaException("Clau inexistent");
            }
        } catch (Exception e) {
            //throw new UtilitatPersistenciaException(" " + e.toString());
            e.printStackTrace();
        }finally {
            tancar();
        }
        return servei;
    }

    public void modificarTreballador(Servei servei) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.merge(servei);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("Impossible actualitzar treballador " + e.toString());
        }finally {
            tancar();
        }
    }

    public void modificarServei(int id) throws UtilitatPersistenciaException {
        modificarTreballador(obtenirServei(id));
    }

    public void esborrarServei(Servei servei) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.remove(servei);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("Impossible eliminar treballador " + e.toString());
        }finally {
            tancar();
        }
    }
    
    public void esborrarServei(int id) throws UtilitatPersistenciaException {
        esborrarServei(obtenirServei(id));
    }
    
    public void inserirReserva(Reserva reserva) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.persist(reserva);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            //throw new UtilitatPersistenciaException("");
        }finally {
            tancar();
        }
    }

    public Reserva obtenirReserva(int id) throws UtilitatPersistenciaException {        
        Reserva reserva = null;
        System.out.println(id);
        try {
            obrir();
            reserva = em.find(Reserva.class, id);
            if (reserva == null) {
                throw new UtilitatPersistenciaException("Clau inexistent");
            }
        } catch (Exception e) {
            //throw new UtilitatPersistenciaException(" " + e.toString());
            e.printStackTrace();
        }finally {
            tancar();
        }
        return reserva;
    }

    public void modificarReserva(Reserva reserva) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.merge(reserva);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("Impossible actualitzar treballador " + e.toString());
        }finally {
            tancar();
        }
    }

    public void modificarReserva(int id) throws UtilitatPersistenciaException {
        modificarReserva(obtenirReserva(id));
    }

    public void esborrarReserva(Reserva reserva) throws UtilitatPersistenciaException {
        try {
            obrir();
            em.getTransaction().begin();
            em.remove(reserva);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new UtilitatPersistenciaException("Impossible eliminar treballador " + e.toString());
        } finally {
            tancar();
        }
    }
    
    public void esborrarReserva(int id) throws UtilitatPersistenciaException {
        esborrarReserva(obtenirReserva(id));
    }
}
