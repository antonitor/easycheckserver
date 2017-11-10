/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
 */
package easycheckserver.persistencia;

/**
 * Classe que emmagatzema tots els noms de les taules i les seves columnes de la
 * base de dades en constants.
 *
 * @author Toni
 */
public class DbContract {

    private DbContract() {
    }

    /**
     * Taula treballador
     */
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

    /**
     * Taula serveis
     */
    public static class TaulaServeis {

        public final static String NOM_TAULA = "serveis";
        public final static String ID = "_id";
        public final static String ID_TREBALLADOR = "id_treballador";
        public final static String DESCRIPCIO = "descripcio";
        public final static String DATASERVEI = "data_servei";
        public final static String HORAINICI = "hora_inici";
        public final static String HORAFINAL = "hora_final";
    }

    /**
     * Taula reserva
     */
    public static class TaulaReserva {

        public final static String NOM_TAULA = "reserva";
        public final static String ID = "_id";
        public final static String IDSERVEI = "id_servei";
        public final static String IDCLIENT = "id_client";
        public final static String LOCALITZADOR = "localitzador";
        public final static String DATARESERVA = "data_reserva";
        public final static String QRCODE = "qr_code";
        public final static String CHECKIN = "checkin";

    }

    /**
     * Taula client
     */
    public static class TaulaClient {

        public final static String NOM_TAULA = "client";
        public final static String ID = "_id";
        public final static String DNI = "dni_titular";
        public final static String NOM = "nom_titular";
        public final static String COGNOM1 = "cognom1_titular";
        public final static String COGNOM2 = "cognom2_titular";
        public final static String TELF = "telefon_titular";
        public final static String EMAIL = "email_titular";
    }

}
