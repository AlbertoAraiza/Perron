package admin.mx.com.perron.dao;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.activities.ListArticulosActivity;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.otto.ImagesItems;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/22/2016.
 */
public class DatabaseObject extends AsyncTask {
    AgregarArticuloActivity agregarArticuloActivity;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000000;
    RequestQueue queue = null;
    int op;
    private Context ctx;
    private Articulo articulo;
    private int position;
    public DatabaseObject(Context ctx, AgregarArticuloActivity agregarArticuloActivity,
                           Articulo articulo, int op, int position ){
        //pruebas
        this.position = position;

        this.agregarArticuloActivity = agregarArticuloActivity;
        setJsonObject(articulo);
        queue = Volley.newRequestQueue(ctx);
        Log.d(Constants.appName, jsonObject.toString());
        this.op = op;
        this.ctx = ctx;
    }

    public DatabaseObject(Context ctx, Articulo articulo, int op, int position){
        this.position = position;
        if(op == Constants.LISTAR_IMAGENES){
            setJsonObject2(articulo);
        }else if(op == Constants.GUARDAR_ARTICULO){
            setJsonObject(articulo);
        }else if(op == Constants.ACTUALIZAR_ARTICULO || op== Constants.LISTAR_ARTICULOS){
            setJsonObject3(articulo);
        }
        queue = Volley.newRequestQueue(ctx);
        Log.d(Constants.appName, jsonObject.toString());
        this.op = op;
        this.ctx = ctx;
        this.articulo = articulo;
    }
    public DatabaseObject(Context ctx,
                          Images images, int op){
        setJsonObject(images);
        queue = Volley.newRequestQueue(ctx);
        Log.d(Constants.appName, jsonObject.toString());
        this.op = op;
        this.ctx = ctx;
    }

    public DatabaseObject(Context ctx, int op, JSONObject jsonImage){
        queue = Volley.newRequestQueue(ctx);
        Log.d(Constants.appName, "jsonImage"+jsonImage.toString());
        this.jsonObject = jsonImage;
        this.op = op;
        this.ctx = ctx;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            try {

                if (op == Constants.GUARDAR_ARTICULO) {
                    String url = Constants.URL_BASE +Constants.SAVE_ITEM;
                    executeRequestJson2(url);
                }else if (op == Constants.GUARDAR_IMAGEN) {//guarda la imagen de un articulo en la tabla de imagenes solamente
                    String url =  Constants.URL_BASE +Constants.SAVE_IMAGE;
                    executeRequestJson2(url);
                }else if(op == Constants.LISTAR_ARTICULOS){
                    String url =  Constants.URL_BASE +Constants.GET_ARTICULOS;
                    getArticulos(url);
                }else if(op == Constants.LISTAR_IMAGENES) {
                    String url = Constants.URL_BASE + Constants.GET_IMAGES;
                    getImages(url);
                }else if(op == Constants.ACTUALIZAR_ARTICULO) {
                    String url = Constants.URL_BASE + Constants.UPDATE_ITEMS;
                    executeRequestJson2(url);
                }
            }catch (Exception e) {
                MainActivity.getStackTrace(e);
                e.printStackTrace();
            }
        }catch (Exception e) {
            StringBuffer errorMsg = new StringBuffer(Utils.getStackTrace(e));
            Utils.setGlobalMessage(errorMsg);
            Log.d(Constants.appName, "ERROR ON onErrorResponse: " + Utils.getStackTrace(e));
            return false;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
                        if(op == Constants.ACTUALIZAR_ARTICULO){
                            //Actualizando RecyclerView
                            MyProperties.getInstance().listaArticulos.set(position,articulo);
                            MyProperties.getInstance().listItems.getAdapter().notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.appName, "ERROR ON onErrorResponse: "+Utils.getStackTrace(error));
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
        Log.d("save articulo: ", object.toString());
//            this.jsonObject = createJsonObject(object);
        this.jsonObject = new JSONObject();
        try {
            jsonObject.put("idNegocio", object.getIdNegocio());
        } catch (JSONException e){
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void setJsonObject2(Articulo object) {
        Log.d("save articulo: ", object.toString());
        this.jsonObject = createJsonObject2(object);
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
//            json.put("imagenString", articulo.getImageCode());
        } catch (JSONException e) {
            System.out.println("*******************createJsonObject(Articulo articulo " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        System.out.println("JSObject: " + json.toString());
        return json;
    }
    public JSONObject createJsonObject(Images images) {
        JSONObject json = new JSONObject();
        try {
            json.put("idArticulo", images.getIdArticulo());
            json.put("idImagen", images.getIdImagen());
            json.put("imagenString", images.getImagen());
        } catch (JSONException e) {
            System.out.println("*********************createJsonObject(Images images " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        Log.d(Constants.appName, "createJsonObject: " + json.toString());
        Toast.makeText(ctx, jsonObject.toString(), Toast.LENGTH_SHORT).show();
        return json;
    }
    public JSONObject createJsonObject2(Articulo articulo) {
        JSONObject json = new JSONObject();
        try {
            json.put("idNegocio", articulo.getIdNegocio());
            json.put("idArticulo", articulo.getIdArticulo());
            json.put("idImagen", articulo.getImagen());
            json.put("imagenString", articulo.getIdArticulo()+"");

        } catch (JSONException e) {
            System.out.println("*******************createJsonObject(Articulo articulo " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        Log.d(Constants.appName, "createJsonObject2: " + json.toString());
        return json;
    }
    public void setJsonObject3(Articulo object) {
        Log.d(Constants.appName, "setJsonObject3: " + object.toString());
        this.jsonObject = createJsonObject3(object);
    }
    public JSONObject createJsonObject3(Articulo articulo) {
        JSONObject json = new JSONObject();

        try {
            json.put("nombreArticulo", articulo.getNombreArticulo());
            json.put("precio", articulo.getPrecio());
            json.put("descripcion", articulo.getDescripcion());
            json.put("idNegocio", articulo.getIdNegocio()+"");
//            json.put("imagenString", articulo.getImageCode());
            json.put("imagen", articulo.getImagen());
            json.put("idArticulo", articulo.getIdArticulo());
        } catch (JSONException e) {
            Log.d(Constants.appName, "Error createJsonObject3: " + Utils.getStackTrace(e));
        }
        Log.d(Constants.appName, "createJsonObject3: " + json.toString());
        return json;
    }
    public JSONObject createJsonObject4(){
        JSONObject json = new JSONObject();
        try {
            json.put("idNegocio", String.valueOf(this.articulo.getIdNegocio()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    /****************
     * Metodo para regresar los registros de los articulos guardados en la base de datos
     */
    public void getArticulos(String url) throws Exception{
        JsonArrayRequest jsonObjReq =
                new JsonArrayRequest(Request.Method.POST,
                        url, jsonObject.toString(), new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
//                        String input = response.toString();
                        //progressDialog.dismiss();
                        getListaArticulos(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d(Constants.appName, "ERROR ON onErrorResponse: "+Utils.getStackTrace(error));
                        Toast.makeText(ctx, "Error on Volley Response: " + Utils.getStackTrace(error), Toast.LENGTH_LONG).show();
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

    public void getListaArticulos(JSONArray listaArticulos) {
        JSONArray jsonMainArr = listaArticulos;
        Log.d(Constants.appName, "listaArticulos : "+listaArticulos);
//        try {
//            JSONObject jsonObj = new JSONObject(listaArticulos);
//            jsonMainArr = jsonObj.getJSONArray("friends");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Gson gson = new Gson();
        Log.d(Constants.appName, "jsonMainArr.toString() : " + jsonMainArr.toString());

        List<Articulo> listaTemporal = new ArrayList<Articulo>();
        for(int i=0;i<jsonMainArr.length();i++){
            JSONObject  articulo = null;
            try {
                articulo = jsonMainArr.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Bitmap bitmap = null;
//            try {
//                byte[] imageString = Base64.decode(articulo.getString("imagenString"), Base64.DEFAULT);
//                Log.d(Constants.appName, "imagenString : " + articulo.getString("imagenString").getBytes());
//                bitmap = BitmapFactory.decodeByteArray(imageString, 0, imageString.length);
//            } catch (Exception e) {
//                e.printStackTrace();
//                bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher);
//            }
            Articulo articuloImage;
            articuloImage = new Articulo();
            try {
                articuloImage.setIdNegocio(articulo.getInt("idNegocio"));
//                articuloImage.setImageBitmap(bitmap);
                articuloImage.setDescripcion(articulo.getString("descripcion"));
                articuloImage.setPrecio(articulo.getDouble("precio"));
                articuloImage.setIdArticulo(articulo.getInt("idArticulo"));
                articuloImage.setNombreArticulo(articulo.getString("nombreArticulo"));
                articuloImage.setIdNegocio(articulo.getInt("idNegocio"));
                articuloImage.setImagen(articulo.getString("imagen"));
                listaTemporal.add(articuloImage);
                Log.d(Constants.appName, articuloImage.toString());
                if(articulo.getString("nombreArticulo").equals("empty")){
                    MyProperties.getInstance().listaValor = Constants.LISTA_VACIA;
                }else{
                    MyProperties.getInstance().listaValor = Constants.LISTA_LLENA;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MyProperties.getInstance().listaArticulos = listaTemporal;
//        Utils.getBus().post("created lista articulos");
        Intent i = new Intent(ctx, ListArticulosActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", position);
//        i.putExtra("position",articulo.getIdNegocio());
        ctx.startActivity(i);

    }

    /**
     * Metodos para mostrar las imagenes de los articulos guardados
     */
    public void getImages(String url) throws Exception{
        JsonArrayRequest jsonObjReq =
                new JsonArrayRequest(Request.Method.POST,
                        url, jsonObject.toString(), new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

//                        String input = response.toString();
                        //progressDialog.dismiss();
                        getListaImagenes(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.appName, "ERROR ON onErrorResponse: "+Utils.getStackTrace(error));
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

    public void getListaImagenes(JSONArray listaArticulos) {
        JSONArray jsonMainArr = listaArticulos;
//        Log.d(Constants.appName, "listaArticulos : "+listaArticulos);
//        try {
//            JSONObject jsonObj = new JSONObject(listaArticulos);
//            jsonMainArr = jsonObj.getJSONArray("friends");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Gson gson = new Gson();
        Log.d(Constants.appName, "jsonMainArr.toString() : " + jsonMainArr.toString());

        List<Images> listaTemporal = new ArrayList<Images>();
        for(int i=0;i<jsonMainArr.length();i++){
            JSONObject  imagen = null;
            try {
                imagen = jsonMainArr.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Bitmap bitmap = null;
//            try {
//                byte[] imageString = Base64.decode(imagen.getString("imagen").getBytes(), Base64.DEFAULT);
//                Log.d(Constants.appName, "imagen : " + imageString.toString());
//                bitmap = BitmapFactory.decodeByteArray(imageString, 0, imageString.length);
//            } catch (Exception e) {
//                e.printStackTrace();
//                bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher);
//            }
            Images itemImage;
            itemImage = new Images();
            try {
                itemImage.setImagen(imagen.getString("imagen"));
                itemImage.setIdArticulo(imagen.getInt("idArticulo"));
                itemImage.setIdImagen(imagen.getInt("idImagen"));
                listaTemporal.add(itemImage);
                Log.d(Constants.appName, itemImage.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MyProperties.getInstance().listaImagenes = listaTemporal;
        ImagesItems imgs = new ImagesItems();
        MyProperties.getInstance().articulo = this.articulo;
        callUpdateArticulo();
    }
    public void callUpdateArticulo(){
        Intent intent = new Intent(this.ctx, AgregarArticuloActivity.class);
        intent.putExtra("action", Constants.UPDATE_ITEM);
        intent.putExtra("position", position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.ctx.startActivity(intent);
    }



}
