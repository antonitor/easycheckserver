/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver;

import easycheckserver.model.Treballador;
import easycheckserver.persistencia.GestorPersistencia;

/**
 *
 * @author Toni
 */
public class InsertTest {
    public static void main(String[] args) throws Exception {
        GestorPersistencia gestor = new GestorPersistencia();
        gestor.obrir();
        Treballador t = new Treballador("test","torres","mari","toni","pass",true);
        gestor.inserirTreballador(t);
        gestor.tancar();
    }
}
