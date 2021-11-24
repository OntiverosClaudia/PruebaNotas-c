package com.example.pruebanotas.ActivitiFragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pruebanotas.Basededatos.BDNotas;
import com.example.pruebanotas.Basededatos.Notas;
import com.example.pruebanotas.R;
import com.example.pruebanotas.Adaptador.listAdapterNotas;

import java.util.ArrayList;

//import es.dmoral.toasty.Toasty;


// Pantalla para visualizar las notas existentes
public class MainActivity extends AppCompatActivity {
    //Variables miembro
    private ListView mListViewNotas;

    private BDNotas mBDNotas; // Objeto para manipular BD
    private ArrayList<Notas> listNotas; // Lista con todas las notas

    public static boolean isPermission; // Controla los permisos
    public static boolean isUpdate; // Controla cambios, actualizaciones


    // Creacion vista notas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //LlenarListView();

        // Mostramos una ventana de dialogo cuando se selecciona un item del ListView
        mListViewNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Notas nota = (Notas) mListViewNotas.getItemAtPosition(i);
                mostrarAlerta(nota);
            }
        });

        if (validacionPermisos()) {
            isPermission = true;
        } else {
            isPermission = false;
        }
    }

    // Iniciamos los objetos de la vista, de la BD y de isUpdate
    private void init() {
        isUpdate = false;

        mBDNotas = new BDNotas(this);

        mListViewNotas = (ListView) findViewById(R.id.listViewNotas);
    }

    // Pedimos permisos al usuario
    private boolean validacionPermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            permisosDenegados();
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 100);
        }
        return false;
    }

    // Comprobamos si el usuario acepto los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermission = true;
            } else {
                isPermission = false;
            }
        }
    }

    // Mostramos advertencia si el usuario niega los permisos
    private void permisosDenegados() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Permisos Desactivados");
        alertDialogBuilder.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la aplicación");
        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 100);
            }
        });
        alertDialogBuilder.show();
    }

    // Llamamos al fragment CrearNota
    public void agregarNota(View view) {
        Intent intent = new Intent(MainActivity.this, Fragment.class);
        startActivity(intent);
    }

    // Mostramos opciones de la nota segun encode
    private void mostrarAlerta(final Notas nota) {
        switch (nota.getEncode()) {
            case 0:
                CharSequence[] opciones= {"Ver o Modificar", "Ocultar contenido", "Eliminar"};
                defaultAlerta(nota, opciones);
                break;
        }
    }

    // Ventana de dialogo con las opciones de las notas
    private void defaultAlerta(final Notas nota, final CharSequence[] opciones) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Seleccione una opción");
        alertDialogBuilder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Ver o Modificar")) {
                    isUpdate = true;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", nota);
                    Intent intent = new Intent(MainActivity.this, CrearNotaFragment.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (opciones[i].equals("Ocultar contenido")) {
                    Notas noteUpdate = mBDNotas.leerNota(nota.getId_notas());
                    noteUpdate.setEncode(1);
                    if (mBDNotas.actualizarNota(nota.getId_notas(), noteUpdate) != 0) {
                        fillListView();

                        //Toasty.sucess(getApplicationContext(), "Contenido ocultado", Toast.LENGTH_SHORT).show();
                    }
                } else if (opciones[i].equals("Mostrar contenido")) {
                    Notas noteUpdate = mBDNotas.leerNota(nota.getId_notas());
                    noteUpdate.setEncode(0);
                    if (mBDNotas.actualizarNota(nota.getId_notas(), noteUpdate) != 0) {
                        fillListView();
                    }
                } else if (opciones[i].equals("Eliminar")) {
                    if (mBDNotas.eliminarNota(nota.getId_notas()) != 0) {
                        fillListView();
                        //Toasty.sucess(getApplicationContext(), "Nota eliminada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alertDialogBuilder.show();
    }

    // Consultamos en la BD todas las notas y se agregan al listView
    private void fillListView() {
        listNotas = mBDNotas.leerNotas();
        if (listNotas != null) {
            listAdapterNotas LANotas = new listAdapterNotas(this,
                    R.layout.activity_main, listNotas);
            mListViewNotas.setAdapter(LANotas);
        } else {
            mListViewNotas.setAdapter(null);
        }
    }

    //Se agregan las notas del ArrayList al listView
    private void fillListView(ArrayList<Notas> notas) {
        listAdapterNotas notesListAdapter = new listAdapterNotas(this,
                R.layout.activity_main, notas);
        mListViewNotas.setAdapter(notesListAdapter);
    }
}