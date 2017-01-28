package admin.mx.com.perron.dao;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/22/2016.
 */
public class DaoSaveArticulo extends AsyncTask {
    AgregarArticuloActivity agregarArticuloActivity;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000000;
    RequestQueue queue = null;
    private Context ctx;
    private Articulo articulo;
    ProgressDialog barProgressDialog;
    private int position;;
    public DaoSaveArticulo(Context ctx, AgregarArticuloActivity agregarArticuloActivity,
                          Articulo articulo, int position){
        this.position = position;
        this.agregarArticuloActivity = agregarArticuloActivity;
        this.articulo = articulo;
        setJsonObject();
        queue = Volley.newRequestQueue(ctx);
        Log.d(Constants.appName, jsonObject.toString());
        this.ctx = ctx;
    }
    @Override
    protected Object doInBackground(Object[] params) {
       try{
            String url = Constants.URL_BASE +Constants.SAVE_ITEM;
            executeRequestJson2(url);

        }catch (Exception e) {
            MainActivity.getStackTrace(e);
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        launchBarDialog();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        barProgressDialog.dismiss();
    }
    public void executeRequestJson2(String url) throws Exception{

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject.toString(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String input = response.toString();
                        int id;
                        String path = "";
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(input);
                            id = jObject.getInt("id");
                            path = jObject.getString("imagen");
                            articulo.setIdArticulo(id);
                            articulo.setImagen(path);
                            //Actualizar Recycler View
                            MyProperties.getInstance().listaArticulos.add(articulo);
                            MyProperties.getInstance().listItems.getAdapter().notifyDataSetChanged();

                            Log.d(Constants.appName, "id: "+id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        agregarArticuloActivity.showNewImageAfterSave(agregarArticuloActivity.getPhoto(), path);
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
    public void setJsonObject() {
        this.jsonObject = createJsonObject();
    }

    public JSONObject createJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("nombreArticulo", articulo.getNombreArticulo());
            json.put("precio", articulo.getPrecio());
            json.put("descripcion", articulo.getDescripcion());
            json.put("idNegocio", articulo.getIdNegocio());
            json.put("imagen", articulo.getImagen());
        } catch (JSONException e) {
            System.out.println("*******************createJsonObject(Articulo articulo " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        System.out.println("JSObject: " + json.toString());
        return json;
    }



    public void launchBarDialog() {
        barProgressDialog = new ProgressDialog(agregarArticuloActivity);
        barProgressDialog.setTitle("Uploading Image ...");
        barProgressDialog.setMessage("Upload in progress ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
    }

}
