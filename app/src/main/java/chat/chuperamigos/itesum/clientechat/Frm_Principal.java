package chat.chuperamigos.itesum.clientechat;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Frm_Principal extends ActionBarActivity implements AsyncResponse {
    EditText txtIp;
    EditText txtPuerto;
    EditText txtUsuario;
    EditText status;
    EditText txtMensaje;
    Button btnEnviar;
    Socket socket = null;
    String Usuario = "";
    String Servidor = "";
    String Puerto = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_principal);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm__principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                /*Toast.makeText(getApplicationContext(),"Opciones",Toast.LENGTH_LONG).show();*/
                /*Método que manda a llamar la Activity de Opciones*/
                MenuOpciones();


                return true;
            case R.id.submenu_conectar:
                /*Conectar al Chat*/



                try {
                    Toast.makeText(getApplicationContext(), "Conectando", Toast.LENGTH_LONG).show();

                    String nombreBD = "CONEXION";
                    int NoVersion = 1;

                    helper helperBD = new helper(this, nombreBD, null, NoVersion);
                    SQLiteDatabase bd = helperBD.getWritableDatabase();

                    //String sSQL = "SELECT * FROM PARAMETROS";

                    Cursor c = bd.rawQuery("SELECT * FROM PARAMETROS", null);



                    try {
                        if (c.moveToFirst() == true) {
                            do {
                                Usuario = c.getString(0);
                                Servidor = c.getString(1);
                                Puerto = c.getString(2);

                            } while (c.moveToNext());

                            Toast.makeText(this,"Usuario: " + Usuario + ' ' + "Servidor: " + Servidor + "Puerto: " + Puerto, Toast.LENGTH_LONG).show();



                        }
                    } catch (Exception ex) {
                        Toast.makeText(this, "No se pudo leer la base de datos", Toast.LENGTH_LONG).show();
                    }

                    int PuertoInt = Integer.parseInt(Puerto.toString());


                    conectarSocket(Servidor,PuertoInt);

                    Toast.makeText(this, "Conexión Exitosa", Toast.LENGTH_LONG).show();



                    /*getConexion();*/

                } catch (Exception ex) {
                    Toast.makeText(this, "No se pudo conectar con el Servidor.", Toast.LENGTH_LONG).show();
                }





                return true;
            case R.id.submenu_desconectar:
                if(socket.isConnected()){
                    desconectar(socket);
                    btnEnviar = (Button)findViewById(R.id.btnEnviar);
                    btnEnviar.setEnabled(false);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getConexion() {
        txtIp = (EditText)findViewById(R.id.txtIP);
        txtPuerto = (EditText)findViewById(R.id.txtPuerto);
        String ip = txtIp.getText().toString();
        int puerto = Integer.parseInt(txtPuerto.getText().toString());
        conectarSocket(ip,puerto);

    }
    private boolean conectarSocket(String ip, int puerto){
        ConectarHilos hilo = new ConectarHilos();
        hilo.execute(ip,Integer.toString(puerto));
        hilo.delegado = this;
        //Hilo del chat
        RecibirMensajesServidorex server = new RecibirMensajesServidorex();
        server.execute();
        server.delegado = this;
        return true;
    }

    @Override
    public void procesoTerminado(boolean resultado) {
        status = (EditText)findViewById(R.id.txtStatus);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        if(resultado){
            status.setText("Conexion exitosa");
            btnEnviar.setEnabled(true);
        }
        else{
            status.setText("Conexion fallida");
            if(btnEnviar.isEnabled()){
                btnEnviar.setEnabled(false);
            }
        }
    }

    @Override
    public void mensajeServidor(String mensajes) {
        try{
            System.out.println("Esto dijo el servidor: " + mensajes);
            EditText chat = (EditText)findViewById(R.id.txtChat);
            chat.append(mensajes + "\n");
        }
        catch (Exception ex){
            System.out.println("Paso esto: " + ex.getMessage());
        }
    }

    /*Manda a Llamar la Actividad del Menú de Opciones*/
    public void MenuOpciones() {
        Intent Menu_opciones = new Intent(this, frm_Opciones.class);
        startActivity(Menu_opciones);


    }

    private class ConectarHilos extends AsyncTask<String,Void,Boolean>{
        public AsyncResponse delegado = null;
        @Override
        protected Boolean doInBackground(String... params) {
            boolean resultado;
            try {
                socket = new Socket(params[0],Integer.parseInt(params[1]));
                resultado = socket.isConnected();
            } catch (IOException e) {
                resultado = false;
                e.printStackTrace();
            }
            return resultado;
        }
        protected void onProgressUpdate(Void... progress) {
            System.out.println("Conectando con el servidor");
        }
        protected void onPostExecute(Boolean result) {
            try{
                if(result){
                    System.out.println("Conexion exitosa");
                    delegado.procesoTerminado(true);
                }
                else{
                    System.out.println("Fallo el metodo de conexion");
                    delegado.procesoTerminado(false);
                }
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        }
    }
    public void onEnviaMensaje(View v){
        txtMensaje = (EditText)findViewById(R.id.txtMensaje);
        String msj = txtMensaje.getText().toString();
        EnviaMensajes mensaje = new EnviaMensajes(socket,msj,Usuario);
        txtMensaje.setText("");
    }
    private void desconectar(Socket soquete){
        try{
            soquete.close();
            Toast.makeText(this,"Desconectado del servidor", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Toast.makeText(this,"Error al desconectar", Toast.LENGTH_LONG).show();
            System.out.println("Ocurrio lo siguiente al desconectar: \n" + ex.getMessage());
        }
    }

    /**
     * Recibe los mensajes del chat reenviados por el servidor
     */
    private class RecibirMensajesServidorex extends AsyncTask<Void,Void,String>{
        // Obtiene el flujo de entrada del socket
        public AsyncResponse delegado = null;
        DataInputStream entradaDatos = null;
        String mensaje ="..";
        @Override
        protected String doInBackground(Void... params) {
            try {
                entradaDatos = new DataInputStream(socket.getInputStream());
            } catch (IOException ex) {
                System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
            } catch (NullPointerException ex) {
                System.out.println("El socket no se creo correctamente.");
            }

            // Bucle infinito que recibe mensajes del servidor
            boolean conectado = true;
            while (conectado) {
                try {
                    mensaje = entradaDatos.readUTF();
                    onPostExecute(mensaje);
                } catch (IOException ex) {
                    System.out.println("Error al leer del stream de entrada: " + ex.getMessage());
                    conectado = false;
                } catch (NullPointerException ex) {
                    System.out.println("El socket no se creo correctamente.");
                    conectado = false;
                }
            }
            return mensaje;
        }
        protected void onPostExecute(String resultado) {
            try{
                delegado.mensajeServidor(resultado);
                System.out.println("Mensaje obtenido del servidor: " + resultado);
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        }
    }
}
