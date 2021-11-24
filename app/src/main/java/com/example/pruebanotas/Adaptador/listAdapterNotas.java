package com.example.pruebanotas.Adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pruebanotas.Basededatos.Notas;
import com.example.pruebanotas.R;

import java.util.List;

public class listAdapterNotas  extends ArrayAdapter<Notas> {
    private Context context;

    // Constructor
    public listAdapterNotas(@NonNull Context context, int resource, @NonNull List<Notas> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    // Metodo que lee y agrega atributos de la clase Notas a la listView
    @SuppressLint("SetTextI18n")
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        Notas nota = (Notas) getItem(i);
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(android.R.layout.activity_list_item, null);
        }

        //Creamos los objetos de la interfaz
        TextView textViewId = (TextView) v.findViewById(R.id.txtVIdNota);
        //usuario?
        TextView textViewEncode = (TextView) v.findViewById(R.id.txtVEncodeNota);
        TextView textViewTitulo = (TextView) v.findViewById(R.id.txtVTituloNotas);

        //Dependiendo del valor de encode mostramos la descripcion de la nota
        if (nota.getEncode() == 0) {
            textViewId.setText(nota.getId_notas().toString());
            textViewEncode.setText(nota.getEncode().toString());
            textViewTitulo.setText(nota.getTitulo());
        }
        return v;
    }
}
