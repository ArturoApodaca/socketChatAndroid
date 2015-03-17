package chat.chuperamigos.itesum.clientechat;

/**
 * Created by cristobal on 16/03/15.
 * Interface que sera implementada por la clase AsyncTask y sera utilizada para obtener el resultado
 * del proceso de conexion
 */
public interface AsyncResponse {
    void procesoTerminado(boolean resultado);
    void mensajeServidor(String mensajes);
}
