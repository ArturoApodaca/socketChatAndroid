package chat.chuperamigos.itesum.clientechat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arturo on 18/03/2015.
 */
public class helper extends SQLiteOpenHelper {

    //Crear Tabla
    String crearBD = "create table PARAMETROS(USUARIO TEXT, SERVIDOR TEXT, PUERTO INTEGER);";
    //Borrar Tabla en caso de que exista.
    String borrarBD = "drop table if exists PARAMETROS;";


    public helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(crearBD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(borrarBD);
        db.execSQL(crearBD);

    }
}
