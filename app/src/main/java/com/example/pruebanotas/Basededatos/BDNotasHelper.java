package com.example.pruebanotas.Basededatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// Clase que crea la BD
public class BDNotasHelper extends SQLiteOpenHelper {

    // Constructor
    public BDNotasHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Crea la tabla de la BD
    @Override
    public void onCreate(SQLiteDatabase bdNotas) {
        bdNotas.execSQL("create table Notas(ID_Nota Integer primary key autoincrement, Titulo text, Contenido text," +
                " Encode Integer default 0)"); // USER_ID Integer, FOREIGN KEY(USER_ID) REFERENCES USER(USER_ID))
    }

    // Actualiza la BD
    @Override
    public void onUpgrade(SQLiteDatabase bdNotas, int i, int i1) {
        // Borra tablas antiguas si existen
        bdNotas.execSQL("drop table if exists Notas");
        // Y se crean nuevamente
        onCreate(bdNotas);
    }
}
