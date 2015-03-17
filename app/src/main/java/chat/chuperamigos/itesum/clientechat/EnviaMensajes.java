package chat.chuperamigos.itesum.clientechat;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
/**
 * Created by cristobal on 16/03/15.
 * Clase creada para enviar el mensaje
 */
public class EnviaMensajes {
    private Socket socket;
    private String mensaje;
    private String usuario;
    private DataOutputStream salidaDatos;
    //Constructor de la clase
    public EnviaMensajes(Socket soquete, String message, String user){
        this.socket = soquete;
        this.mensaje = message;
        this.usuario = user;
        try {
            this.salidaDatos = new DataOutputStream(socket.getOutputStream());
            salidaDatos.writeUTF(usuario+ " dice:  " + this.mensaje);
        } catch (IOException ex) {
            System.out.println("Error al crear el stream de salida : " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("El socket no se creo correctamente. ");
        }
    }
}
