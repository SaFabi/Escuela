package com.example.fabi.escuelaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fabi.escuelaapp.Adapters.CalificacionAdapter;
import com.example.fabi.escuelaapp.Adapters.MateriaAdapter;
import com.example.fabi.escuelaapp.DAO.DAO;
import com.example.fabi.escuelaapp.DAO.EvaluacionDAO;
import com.example.fabi.escuelaapp.DAO.MateriaDAO;
import com.example.fabi.escuelaapp.Entidades.Calificacion;
import com.example.fabi.escuelaapp.Entidades.Materia;
import com.example.fabi.escuelaapp.Entidades.Usuario;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView txtBienvenida;
    RecyclerView recycler;
     Usuario usuario;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        MenuItem menuEditar =menu.findItem(R.id.editar);
        MenuItem menuRegistrarUsuario = menu.findItem(R.id.agregar_alumno);
        MenuItem menuRegistrarMateria = menu.findItem(R.id.agregar_materia);
        MenuItem menuRegistrarCalificacion = menu.findItem(R.id.agregar_calificacion);

        if (usuario.getCategoria().getId() == 1){
            menuEditar.setVisible(true);
            menuRegistrarCalificacion.setVisible(false);
            menuRegistrarMateria.setVisible(false);
            menuRegistrarUsuario.setVisible(false);
        }else{
            menuEditar.setVisible(false);
            menuRegistrarCalificacion.setVisible(true);
            menuRegistrarMateria.setVisible(true);
            menuRegistrarUsuario.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.editar:
                Intent intent = new Intent(MainActivity.this,EditarUsuario.class);
                intent.putExtra("USUARIO",usuario);
                startActivity(intent);
                break;
            case R.id.agregar_alumno:
                Intent intent1 = new Intent(MainActivity.this,agregarAlumnos.class);
                startActivity(intent1);
                break;
            case R.id.agregar_calificacion:
                Intent intent2 = new Intent(MainActivity.this,agregarCalificaciones.class);
                startActivity(intent2);
                break;
            case R.id.agregar_materia:
                Intent intent3 =  new Intent(MainActivity.this,agregarMaterias.class);
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario= (Usuario)getIntent().getParcelableExtra("USUARIO");
        recycler = (RecyclerView)findViewById(R.id.recyclerView);
        txtBienvenida = (TextView)findViewById(R.id.txtBienvenida);
        txtBienvenida.setText("Bienvenido "+usuario.getCategoria().getNombre() + " "+usuario.getNombre()
                + ". Esta es la lista de materias que existen");

        if (usuario.getCategoria().getId() == 2){
            llenarProfesor();
        }else{
            llenarAlumnos();
        }
    }

    public void llenarProfesor(){
        //LLenamos el recyclerView
        MateriaDAO.getInstance().getListaMaterias(this, new DAO.OnResultadoListaConsulta<Materia>() {
            @Override
            public void consultaSuccess(List<Materia> t) {
                Toast.makeText(MainActivity.this,String.valueOf(t.size()), Toast.LENGTH_SHORT).show();
                if (t != null){
                    MateriaAdapter adapter = new MateriaAdapter(t);
                    recycler.setAdapter(adapter);
                    recycler.setHasFixedSize(true);
                    recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                }else{
                    Toast.makeText(MainActivity.this, "Error en MateriaDAO", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void consultaFailed(String error, int codigo) {
                String TAG = MainActivity.class.getSimpleName();
                Toast.makeText(MainActivity.this, "Error en la conexion", Toast.LENGTH_SHORT).show();
                Log.i(TAG,error+ " "+codigo);

            }
        });
    }

    public void llenarAlumnos(){

        EvaluacionDAO.getInstance().getListarMaterias(this,usuario, new DAO.OnResultadoListaConsulta<Calificacion>() {
            @Override
            public void consultaSuccess(List<Calificacion> t) {
                if (t != null) {
                    CalificacionAdapter adapter = new CalificacionAdapter(t);
                    recycler.setAdapter(adapter);
                    recycler.setHasFixedSize(true);
                    recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                }else{
                    Toast.makeText(MainActivity.this, "Error en EvaluacionDAO", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void consultaFailed(String error, int codigo) {
                String TAG = MainActivity.class.getSimpleName();
                Toast.makeText(MainActivity.this, "Error en la conexion", Toast.LENGTH_SHORT).show();
                Log.i(TAG,error+ " "+codigo);

            }
        });
    }


}
