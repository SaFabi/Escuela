package com.example.fabi.escuelaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fabi.escuelaapp.DAO.DAO;
import com.example.fabi.escuelaapp.DAO.MateriaDAO;
import com.example.fabi.escuelaapp.Entidades.Materia;

public class agregarMaterias extends AppCompatActivity {
    EditText edtClave,edtNombre;
    Button btnAgregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_materias);
        edtClave = (EditText)findViewById(R.id.ClaveMateria);
        edtNombre = (EditText)findViewById(R.id.edtNombreMateria);
        btnAgregar = (Button)findViewById(R.id.btnAgregarMateria);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtClave.getText().length()>0 && edtNombre.getText().length() > 0){
                    MateriaDAO materiaDAO = MateriaDAO.getInstance();
                    materiaDAO.registrarMateria(agregarMaterias.this, edtClave.getText().toString(), edtNombre.getText().toString(), new DAO.OnResultadoConsulta<Materia>() {
                        @Override
                        public void consultaSuccess(Materia materia) {
                            edtNombre.setText("");
                            edtClave.setText("");
                            finish();
                            Toast.makeText(agregarMaterias.this, "Se inserto correctamente", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void consultaFailed(String error, int codigo) {
                            Toast.makeText(agregarMaterias.this, "No se pudo insertar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(agregarMaterias.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
