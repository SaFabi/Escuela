package com.example.fabi.escuelaapp.DAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.fabi.escuelaapp.Entidades.Categoria;
import com.example.fabi.escuelaapp.Entidades.Credencial;
import com.example.fabi.escuelaapp.Entidades.Usuario;
import com.example.fabi.escuelaapp.Recursos.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabi on 13/06/2018.
 */

public class UsuarioDAO {
    private static UsuarioDAO usuarioDAO;
    private ProgressDialog progressDialog;

    private UsuarioDAO() {
    }

    public static UsuarioDAO getInstance(){
        if (usuarioDAO == null){
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

    public void obtenerDatosLogin(Context context, final String nick, final String pass, final DAO.OnResultadoConsulta<Usuario> listener){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Espere por favor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = Constantes.HOST+Constantes.CARPETA_DAO+"login/acceso.php";
        Map<String,String> params = new HashMap<>();
        params.put("nick",nick);
        params.put("pass",pass);

        PeticionHTTP.POST post = PeticionHTTP.POST.getInstance(context,url,params);
        post.getResponse(new PeticionHTTP.OnConsultaListener<String>() {
            @Override
            public void onSuccess(String respuesta) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                try {
                    JSONArray array = new JSONArray(respuesta);
                    JSONObject object;

                    if (array.length() > 0){
                       object = array.getJSONObject(0);
                        Credencial credencial = new Credencial(nick,pass);
                        Categoria categoria = new Categoria(Integer.parseInt(object.getString("categoriaid")),object.getString("categoria"),true);
                        Usuario usuario = new Usuario(Integer.parseInt(object.getString("usuarioid")),object.getString("nombre"),credencial,Boolean.parseBoolean(object.getString("activo"))
                                ,categoria);
                        listener.consultaSuccess(usuario);
                    }else{
                        listener.consultaSuccess(null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String error, int respuestaHTTP) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                listener.consultaFailed(error,respuestaHTTP);
            }
        });
    }

    public void registarUsuario(Context context, final String nombre, final String nick, final String pass, final DAO.OnResultadoConsulta<Usuario>listener){
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setMessage("Espere por favor...");
        progressDialog.setCancelable(false);

        String url = Constantes.HOST+Constantes.CARPETA_DAO+"Main/agregarUsuario.php";
        Map<String,String> params = new HashMap<>();
        params.put("nombre",nombre);
        params.put("nick",nick);
        params.put("pass",pass);

        PeticionHTTP.POST post = PeticionHTTP.POST.getInstance(context,url,params);
        post.getResponse(new PeticionHTTP.OnConsultaListener<String>() {
            @Override
            public void onSuccess(String respuesta) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Credencial credencial = new Credencial(nick,pass);
                Categoria categoria = new Categoria("Alumno");
                Usuario usuario = new Usuario(nombre,credencial,categoria);
                listener.consultaSuccess(usuario);
            }
            @Override
            public void onFailed(String error, int respuestaHTTP) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                listener.consultaFailed(error,respuestaHTTP);

            }
        });

    }

    public void getlistaUsuarios(Context context, final DAO.OnResultadoListaConsulta listaConsulta){
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setMessage("Espere por favor...");
        progressDialog.setCancelable(false);

        String url = Constantes.HOST+Constantes.CARPETA_DAO+"Main/listaAlumnos.php";
        PeticionHTTP.GET get =new PeticionHTTP.GET(context,url);
        get.getResponseString(new PeticionHTTP.OnConsultaListener<String>() {
            @Override
            public void onSuccess(String respuesta) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                try {
                    JSONArray jsonArray = new JSONArray(respuesta);
                    Log.i("tamano",String.valueOf(jsonArray.length()));
                    if (jsonArray.length() >0){
                        List<Usuario>list = new ArrayList<Usuario>();
                        for (int i = 0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Categoria categoria = new Categoria(Integer.parseInt(jsonObject.getString("categoria_id")),
                                    jsonObject.getString("categoria_nombre"),Boolean.parseBoolean(jsonObject.getString("activo")));

                            Credencial credencial = new Credencial(jsonObject.getString("nick"),jsonObject.getString("pass"));

                            Usuario usuario = new Usuario(Integer.parseInt(jsonObject.getString("usuario_id")),jsonObject.getString("usuario_nombre"),
                                    credencial,true,categoria);
                            list.add(usuario);
                        }
                        listaConsulta.consultaSuccess(list);
                    }else{
                        listaConsulta.consultaSuccess(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    listaConsulta.consultaSuccess(null);
                }
            }

            @Override
            public void onFailed(String error, int respuestaHTTP) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                listaConsulta.consultaFailed(error,respuestaHTTP);
            }
        });

    }

    public void actualizarUsuario(Context context, final Integer id, String nick, String pass, final String nombre,final DAO.OnResultadoConsulta listener){
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setMessage("Espere por favor...");
        progressDialog.setCancelable(false);

        String url = Constantes.HOST+Constantes.CARPETA_DAO+"Main/editarUsuario.php";
        Map<String,String> params = new HashMap<>();
        params.put("id",String.valueOf(id));
        params.put("nombre",nombre);
        params.put("nick",nick);
        params.put("pass",pass);

        PeticionHTTP.POST post = PeticionHTTP.POST.getInstance(context,url,params);
        post.getResponse(new PeticionHTTP.OnConsultaListener<String>() {
            @Override
            public void onSuccess(String respuesta) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                listener.consultaSuccess(null);

            }

            @Override
            public void onFailed(String error, int respuestaHTTP) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                listener.consultaFailed(error,respuestaHTTP);
            }
        });
    }
}
