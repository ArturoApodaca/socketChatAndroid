package chat.chuperamigos.itesum.clientechat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;


public class frm_Opciones extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_opciones);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_opciones, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onGuardar(View boton) {

        try {
            /*EditText usuario = (EditText) findViewById(R.id.edUsuario);
            EditText servidor = (EditText) findViewById(R.id.edServidor);
            EditText puerto = (EditText) findViewById(R.id.edPuerto);

            String user = usuario.getText().toString();
            String server = servidor.getText().toString();
            String port = puerto.getText().toString();*/

            //Nombre y Versión de Base de Datos
            String nombreBD = "CONEXION";
            int NoVersion = 1;


            helper helperBD = new helper(this, nombreBD, null, NoVersion);
            SQLiteDatabase bd = helperBD.getWritableDatabase();

            EditText edU = (EditText) findViewById(R.id.edUsuario);
            String sUsuario = edU.getText().toString();

            EditText edS = (EditText) findViewById(R.id.edServidor);
            String sServidor = edS.getText().toString();

            EditText edP = (EditText) findViewById(R.id.edPuerto);
            String sPuerto = edP.getText().toString();

           /* String sSQL = "INSERT INTO PARAMETROS VALUES (" + sUsuario + "," + '"' + sServidor + "," + '"' + sPuerto + '"' + ");";*/
            String sSQL = "INSERT INTO PARAMETROS(USUARIO, SERVIDOR, PUERTO) VALUES('" + sUsuario + "','" + sServidor + "','" + sPuerto + "')";
            /*String sSQL = "INSERT INTO USUARIOS VALUES(" + sClave + "," + '"' + sNombre + '"' + ");";*/

            /*Ejecutar Query y Cerrar conexión con la BD*/
            try {
                bd.execSQL(sSQL);
                bd.close();

                Toast.makeText(this, "Guardado Exitoso.", Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
                Toast.makeText(this, "Parece que el Query está mal.", Toast.LENGTH_LONG).show();
            }


        } catch (Exception ex) {
            Toast.makeText(this, "Ocurrió un problema al guardar los datos.", Toast.LENGTH_LONG).show();

        }



    }
}
