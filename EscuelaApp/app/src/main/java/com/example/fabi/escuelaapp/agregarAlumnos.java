package com.example.fabi.escuelaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fabi.escuelaapp.DAO.DAO;
import com.example.fabi.escuelaapp.DAO.UsuarioDAO;
import com.example.fabi.escuelaapp.Entidades.Usuario;

public class agregarAlumnos extends AppCompatActivity {

    EditText edtNick, edtPass, edtNombre;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        edtNick = (EditText)findViewById(R.id.edtUsuario);
        edtPass = (EditText)findViewById(R.id.edtPass);
        edtNombre = (EditText)findViewById(R.id.edtNombre);
        btnAgregar = (Button)findViewById(R.id.btnEditar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNick.getText().length() >0 && edtPass.getText().length() >0 && edtNombre.getText().length() >0){
                    UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
                    usuarioDAO.registarUsuario(agregarAlumnos.this, edtNombre.getText().toString(), edtNick.getText().toString(), edtPass.getText().toString(), new DAO.OnResultadoConsulta<Usuario>() {
                        @Override
                        public void consultaSuccess(Usuario usuario) {

                            edtNombre.setText("");
                            edtPass.setText("");
                            edtNick.setText("");
                            finish();
                            Toast.makeText(agregarAlumnos.this, "Se inserto correctamente", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void consultaFailed(String error, int codigo) {
                            Toast.makeText(agregarAlumnos.this, "No se inserto", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    Toast.makeText(agregarAlumnos.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
