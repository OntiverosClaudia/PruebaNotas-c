package com.example.pruebanotas.ActivitiFragment;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pruebanotas.Basededatos.BDNotas;
import com.example.pruebanotas.Basededatos.Notas;
import com.example.pruebanotas.Opcional.TtsManager;
import com.example.pruebanotas.R;

import java.util.Arrays;

public class CrearNotaFragment extends Fragment {
    // Creacion de objetos
    private EditText mETTitulo, mETContenido;
    private TextView mTVId, mTVEncode;
    // Boton audio,
    private Button mbtnLeerAudio;

    private BDNotas bdNotas; // Objeto para usar la BD
    private TtsManager ttsManager = null;  //Objeto que convierte el texto a voz
    private int stopTtsManager = 0;  // Variable para parar la lectura del texto

    /*//Inicializa objetos
    private void init() {
        bdNotas = new BDNotas(this);
        mETTitulo = (EditText) findViewById(R.id.EtxtTitulo);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_crear_nota);
        // Inflar o cargar el layout para el Fragment

        //init();
        capturarNota();
        comprobarContenido();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar o cargar el layout para el Fragment
        View view = inflater.inflate(R.layout.fragment_crear_nota, container, false);
        //Inicializa objetos
        bdNotas = new BDNotas(getContext());// this gg
        mETTitulo = view.findViewById(R.id.EtxtTitulo);
        mETContenido = view.findViewById(R.id.EtxtContenido);
        mTVId = view.findViewById(R.id.txtVIdNota);
        mTVEncode = view.findViewById(R.id.txtVEncodeNota);
        mbtnLeerAudio = view.findViewById(R.id.btnLeer);
        return view;
    }

    // Comprueba que no este vacio el campo de contenido y muestra opcion de audio
    private void comprobarContenido() {
        if (!mETContenido.getText().toString().isEmpty()) {

            //Se inicializa el obejeto ttsManager y se llama al metodo init() para inicializar los atributos de la clase
            ttsManager = new TtsManager();
            ttsManager.init(this);
            mbtnLeerAudio.setVisibility(View.VISIBLE);
        } else {
            mbtnLeerAudio.setVisibility(View.INVISIBLE);
        }
    }

    // Capturamos una nota mandada desde el main y muestra los valores
    private void capturarNota() {
        Bundle bundle = getIntent().getExtras(); // ahhhhh
        if (bundle != null) {

            //Se obtiene el la nota del objeto Bundle y se agregan los valores a la interfaz
            Notas notas = (Notas) bundle.getSerializable("Notas");
            mETTitulo.setText(notas .getTitulo());
            mETContenido.setText(notas .getContenido());
        }
    }

    // Creamos una nota con el valor obtenido y se hace una actualizacion o insercion segun el caso
    public void confirmarNota(View view) {
        Notas nota = new Notas(Integer.parseInt(mTVId.getText().toString()), mETTitulo.getText().toString(),
                mETContenido.getText().toString(), Integer.parseInt(mTVEncode.getText().toString()));

        if (MainActivity.isUpdate) {
            BDNotas.actualizarNota(nota.getId_notas(), nota);
        } else {
            BDNotas.insertarNota(nota);
        }
        goMain();
    }

    // Nos lleva al main
    private void goMain() {
        Intent intent = new Intent(CrearNotaFragment.this, MainActivity.class); // lel
        startActivity(intent);
        finish();
    }

    // Se inicia o detiene la lectura de voz
    public void readingDescription(View view) {
        switch (stopTtsManager) {
            case 0:
                stopTtsManager = 1;
                //le pasamos el string que queremos que convierta a voz el Objeto ttsManager
                ttsManager.initQueue(mETContenido.getText().toString());
                break;
            case 1:
                stopTtsManager = 0;
                ttsManager.stop();
                break;
        }
    }

    // Se destruye el lector de voz para que no consuma recursos del sistema
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ttsManager != null) {
            ttsManager.shutDown();
        }
    }

}