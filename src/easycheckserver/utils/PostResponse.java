/*
 * EasyCheck Server v 1.6 - MarcarniApp
 * 
 * DAM_M13B0 Projecte de desenvolupament d'aplicacions multiplataforma
 * 
 * Semestre 1 - Curs 2017 - 2018
 */
package easycheckserver.utils;

import java.io.Serializable;

/**
 *
 * @author Toni
 */
public class PostResponse implements Serializable{
    private int requestCode; //0 -> Error, 1 -> Correcto
    private String message; 
    
    public PostResponse(){}
    
    public PostResponse(int code, String message) {
        this.requestCode = code;
        this.message = message;
    }

    /**
     * @return the requestCode
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     * @param requestCode the requestCode to set
     */
    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
