package admin.mx.com.perron.dao;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.R;
import admin.mx.com.perron.activities.AdministracionMain;
import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.otto.ImagesItems;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;

/**
 * Created by jorge on 3/19/2016.
 */
public class DaoImages extends AsyncTask {
    AgregarArticuloActivity agregarArticuloActivity;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000000;
    RequestQueue queue = null;
    int op;
    private Context ctx;

    public DaoImages(Context ctx, AgregarArticuloActivity agregarArticuloActivity,
                         Images images, int op){
        this.agregarArticuloActivity = agregarArticuloActivity;
        setJsonObject(images);
        queue = Volley.newRequestQueue(ctx);
        Log.d(Constants.appName, jsonObject.toString());
        this.op = op;
        this.ctx = ctx;
    }

    @Override
    protected Object doInBackground (Object[]params){
        try {


            try {

                if (op == Constants.GUARDAR_IMAGEN) {//guarda la imagen de un articulo en la tabla de imagenes solamente
                    String url = Constants.URL_BASE + Constants.SAVE_IMAGE;
                    executeRequestJson2(url);
                }
            } catch (Exception e) {
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
    protected void onPreExecute () {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute (Object o){
        super.onPostExecute(o);
    }

    public void executeRequestJson2(String url) throws Exception {
        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject.toString(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String input = response.toString();
                        String id = "";
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(input);
                            id = jObject.getString("id");
                            Log.d(Constants.appName, "id: "+id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        agregarArticuloActivity.showNewImageAfterSaveImg(id );
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.appName, "ERROR ON onErrorResponse: " + Utils.getStackTrace(error));
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

    public void setJsonObject(Images images) {
        Log.d("save images: ", images.toString());
        this.jsonObject = createJsonObject(images);
    }

    public JSONObject createJsonObject(Images images) {
        JSONObject json = new JSONObject();
        try {
            json.put("idArticulo", images.getIdArticulo());
            json.put("idImagen", images.getIdImagen());
            json.put("imagenString", images.getImagenString());
        } catch (JSONException e) {
            System.out.println("*********************createJsonObject(Images images " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        Log.d(Constants.appName, "createJsonObject: " + json.toString());
        return json;
    }

    /****************
     * Metodo para regresar los registros de los articulos guardados en la base de datos
     */
    public void getArticulos(String url) throws Exception {
        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject.toString(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String input = response.toString();
                        //progressDialog.dismiss();
                        getListaArticulos(input);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.appName, "ERROR ON onErrorResponse: " + Utils.getStackTrace(error));
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

    public void getListaArticulos(String listaArticulos) {
        JSONArray jsonMainArr = null;
        Log.d(Constants.appName, "listaArticulos : " + listaArticulos);
        try {
            JSONObject jsonObj = new JSONObject(listaArticulos);
            jsonMainArr = jsonObj.getJSONArray("friends");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Log.d(Constants.appName, "jsonMainArr.toString() : " + jsonMainArr.toString());

        List<Articulo> listaTemporal = new ArrayList<Articulo>();
        for (int i = 0; i < jsonMainArr.length(); i++) {
            JSONObject articulo = null;
            try {
                articulo = jsonMainArr.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = null;
            try {
                byte[] imageString = Base64.decode(articulo.getString("imagenString"), Base64.DEFAULT);
                Log.d(Constants.appName, "imagenString : " + articulo.getString("imagenString").getBytes());
                bitmap = BitmapFactory.decodeByteArray(imageString, 0, imageString.length);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher);
            }
            Articulo articuloImage;
            articuloImage = new Articulo();
            try {
                articuloImage.setIdNegocio(articulo.getInt("idNegocio"));
                articuloImage.setImageBitmap(bitmap);
                articuloImage.setDescripcion(articulo.getString("descripcion"));
                articuloImage.setPrecio(articulo.getDouble("precio"));
                articuloImage.setIdArticulo(articulo.getInt("idArticulo"));
                articuloImage.setNombreArticulo(articulo.getString("nombreArticulo"));
                articuloImage.setIdNegocio(articulo.getInt("idNegocio"));
                listaTemporal.add(articuloImage);
                Log.d(Constants.appName, articuloImage.toString());
                if (articulo.getString("nombreArticulo").equals("empty")) {
                    MyProperties.getInstance().listaValor = Constants.LISTA_VACIA;
                } else {
                    MyProperties.getInstance().listaValor = Constants.LISTA_LLENA;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MyProperties.getInstance().listaArticulos = listaTemporal;
        Utils.getBus().post("created lista articulos");
    }
}
