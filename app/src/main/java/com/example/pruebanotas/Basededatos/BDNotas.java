package com.example.pruebanotas.Basededatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

// Clase para manipular la tabla Notas de la BD
public class BDNotas {
    private SQLiteDatabase mNotas;
    private BDNotasHelper mBDNotasHelper;

    // Constructor para llamar al Helper y crear BD
    public BDNotas(Context context) {
        mBDNotasHelper = new BDNotasHelper(context, "Notas", null, 1);
    }
    // Abre conexion con la BD
    public void openForWrite() {
        mNotas = mBDNotasHelper.getWritableDatabase();
    }
    // Cierra la BD
    public void close() {
        mNotas.close();
    }

    // Insertamos una nota en la BD
    public long insertarNota(Notas nota){
        openForWrite();
        long registros = 0;
        ContentValues content = new ContentValues();
        content.put("Titulo", nota.getTitulo());
        content.put("Contenido", nota.getContenido());
        content.put("Encode", nota.getEncode());
        // Usuairo id
        registros  = mNotas.insert("Notas", null, content);
        close();
        return registros ;
    }

    // Eliminamos una nota de la BD por su id
    public long eliminarNota(Integer IDnota) {
        openForWrite();
        long registros = 0;
        registros = mNotas.delete("Notas", "ID_Nota = " + IDnota, null);
        close();
        return registros;
    }

    // Actualizamos una nota en la BD por su id
    public long actualizarNota(Integer IDnota, Notas nota) {
        openForWrite();
        long registros = 0;
        ContentValues content = new ContentValues();
        content.put("Titulo", nota.getTitulo());
        content.put("Contenido", nota.getContenido());
        content.put("Encode", nota.getEncode());
        //Usuario id
        registros = mNotas.update("Notas", content, "ID_Nota = " + IDnota, null);
        close();
        return registros;
    }

    // Leemos una nota de la BD por su id
    public Notas leerNota(Integer IDnota) {
        openForWrite();
        Cursor cursor = mNotas.rawQuery("select ID_Nota, Titulo, Contenido, Encode" +
                " from NOTE where ID_Nota = " + IDnota, new String[]{});
        if (cursor.getCount() == 0) {
            cursor.close();
            close();
            return null;
        }
        Notas nota = null;
        if (cursor.moveToFirst()) {
            nota = new Notas(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3));
        }
        cursor.close();
        close();
        return nota;
    }

    // Leemos todas las notas de la BD
    public ArrayList<Notas> leerNotas() {
        openForWrite();
        Cursor cursor = mNotas.rawQuery("select ID_Nota, Titulo, Contenido, Encode from Notas", null);
        if (cursor.getCount() == 0) {
            cursor.close();
            close();
            return null;
        }
        ArrayList<Notas> listNotas = new ArrayList<>();
        while (cursor.moveToNext()) {
            listNotas.add(new Notas(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3)));
        }
        cursor.close();
        close();
        return listNotas;
    }
}
