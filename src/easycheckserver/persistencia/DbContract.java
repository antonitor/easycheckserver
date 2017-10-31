/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easycheckserver.persistencia;

/**
 *
 * @author Toni
 */
public class DbContract {

    private DbContract() {
    }

    public static class TaulaTreballador {
        public final static String NOM_TAULA = "treballador";
        public final static String ID = "_id";
        public final static String NOM = "nom";
        public final static String COGNOM1 = "cognom1";
        public final static String COGNOM2 = "cognom2";
        public final static String DNI = "dni";
        public final static String ADMIN = "esAdmin";
        public final static String LOGIN = "login";
        public final static String PASSWORD = "password";  
    }
    
        public static class TaulaServeis {
        public final static String NOM_TAULA = "serveis";
        public final static String ID = "_id"; 
        public final static String ID_TREBALLADOR = "id_treballador";
        public final static String DESCRIPCIO = "descripcio";
        public final static String DATASERVEI = "data_servei";
        public final static String HORAINICI = "hora_inici";
        public final static String HORAFINAL = "hora_final";
    }
        
    public static class TaulaReserva {
        public final static String NOM_TAULA = "reserva";
        public final static String ID = "_id"; 
        public final static String IDSERVEI = "id_servei";
        public final static String LOCALITZADOR = "localitzador";
        public final static String DATARESERVA = "data_reserva";
        public final static String NOM = "nom_titular";
        public final static String COGNOM1 = "cognom1_titular";
        public final static String COGNOM2 = "cognom2_titular";
        public final static String TELF = "telefon_titular";
        public final static String EMAIL = "email_titular";
        public final static String QRCODE = "qr_code";
        public final static String CHECKIN = "checkin";
        public final static String DNI = "dni_titular";
    }

}
