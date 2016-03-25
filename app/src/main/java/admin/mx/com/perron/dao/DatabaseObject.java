package admin.mx.com.perron.dao;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.R;
import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.entities.NegociosImage;
import admin.mx.com.perron.entities.NegociosImage2;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/22/2016.
 */
public class DatabaseObject extends AsyncTask {
    private Context ctx;
    ProgressDialog progressDialog;
    AgregarArticuloActivity agregarArticuloActivity;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000000;
    RequestQueue queue = null;
    int op;
    public DatabaseObject(Context ctx, AgregarArticuloActivity agregarArticuloActivity,
                           Articulo articulo, int op){
        this.agregarArticuloActivity = agregarArticuloActivity;
        setJsonObject(articulo);
        queue = Volley.newRequestQueue(ctx);
        Log.d("Save or update:", jsonObject.toString());
        this.op = op;
    }
    public DatabaseObject(Context ctx, AgregarArticuloActivity agregarArticuloActivity,
                          Images images, int op){
        this.agregarArticuloActivity = agregarArticuloActivity;
        setJsonObject(images);
        queue = Volley.newRequestQueue(ctx);
        Log.d("Save or update:", jsonObject.toString());
        this.op = op;
    }
    public DatabaseObject(Context ctx, AgregarArticuloActivity agregarArticuloActivity, int op){
        this.agregarArticuloActivity = agregarArticuloActivity;
        queue = Volley.newRequestQueue(ctx);
        this.op = op;
    }
    public DatabaseObject(Context ctx, int op){
        queue = Volley.newRequestQueue(ctx);
        Log.d("list items:", "option selected: "+op);
        this.op = op;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            try {

                if (op == Constants.GUARDAR_ARTICULO) {
                    String url = Constants.URL_BASE +Constants.SAVE_ITEM;
                    executeRequestJson2(url);
                }else if (op == Constants.GUARDAR_IMAGEN) {
                    String url =  Constants.URL_BASE +Constants.SAVE_IMAGE;
                    executeRequestJson2(url);
                }else if(op == Constants.LISTAR_ARTICULOS){
                    String url =  Constants.URL_BASE +Constants.GET_ARTICULOS;
                    getArticulos(url);
                }
            }catch (Exception e) {
                MainActivity.getStackTrace(e);
                e.printStackTrace();
            }
        }catch (Exception e) {
            StringBuffer errorMsg = new StringBuffer(Utils.getStackTrace(e));
            Utils.setGlobalMessage(errorMsg);
            return false;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog= ProgressDialog.show(agregarArticuloActivity, "Uploading image to the server","Uploading ....", true);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
    public void executeRequestJson2(String url) throws Exception{

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject.toString(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String input = response.toString();
                        //progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("ERROR ON onErrorResponse: "+Utils.getStackTrace(error));
                        //progressDialog.dismiss();
                    }
                });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                //MY_SOCKET_TIMEOUT_MS,
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }
    public void setJsonObject(Articulo object) {

            this.jsonObject = createJsonObject(object);

    }
    public void setJsonObject(Images object) {

        this.jsonObject = createJsonObject(object);

    }
    public JSONObject createJsonObject(Articulo articulo) {
        JSONObject json = new JSONObject();
        try {
            json.put("nombreArticulo", articulo.getNombreArticulo());
            json.put("precio", articulo.getPrecio());
            json.put("descripcion", articulo.getDescripcion());
            json.put("idNegocio", articulo.getIdNegocio());
            json.put("imagenString", articulo.getImageCode());
        } catch (JSONException e) {
            System.out.println("*******************createJsonObject(Articulo articulo " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        System.out.println("JSObject: " + json.toString());
        return json;
    }
    public JSONObject createJsonObject(Images images) {
        JSONObject json = new JSONObject();
        try {
            json.put("idarticulo", images.getIdArticulo());
            json.put("idimagen", images.getIdImagen());
            json.put("imagen", images.getImage());
        } catch (JSONException e) {
            System.out.println("*********************createJsonObject(Images images " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        System.out.println("JSObject: " + json.toString());
        return json;
    }
    /****************
     * Metodo para regresar los registros de los articulos guardados en la base de datos
     */
    public void getArticulos(String url) throws Exception{
        StringRequest stringReq = null;
        stringReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String input = response.toString();
                Log.d(Constants.appName, "response : "+input);
                getListaArticulos(input);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Constants.appName, "VolleyError : "+Utils.getStackTrace(error));
                System.out.println("ERROR ON onErrorResponse: "+Utils.getStackTrace(error));
            }
        });
        stringReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringReq);
    }

    public void getListaArticulos(String listaArticulos) {
        Gson gson = new Gson();
        List<Articulo> listArticulos = gson.fromJson(listaArticulos, new TypeToken<List<Articulo>>(){}.getType());

        List<Articulo> listaTemporal = new ArrayList<Articulo>();
        for(int i=0;i<listArticulos.size();i++){
            Articulo articulo = (Articulo)listArticulos.get(i);

            Bitmap bitmap = null;
            try {

                Log.d(Constants.appName, "articulo.getNombreArticulo() : "+articulo.getNombreArticulo());
                bitmap = BitmapFactory.decodeByteArray(articulo.getImagen(), 0, articulo.getImagen().length);
            } catch(Exception e){
                e.printStackTrace();
                bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher);
            }
            Articulo articuloImage;
            articuloImage = new Articulo();
            articuloImage.setIdNegocio(articulo.getIdNegocio());
            articuloImage.setImageBitmap(bitmap);
            articuloImage.setDescripcion(articulo.getDescripcion());
            articuloImage.setPrecio(articulo.getPrecio());
            articuloImage.setIdArticulo(articulo.getIdArticulo());
            articuloImage.setNombreArticulo(articulo.getNombreArticulo());
            articuloImage.setIdNegocio(articulo.getIdNegocio());
            listaTemporal.add(articuloImage);
            Log.d(Constants.appName, articuloImage.toString());
        }
        MyProperties.getInstance().listaArticulos =listaTemporal;
    }
}
