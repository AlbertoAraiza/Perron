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
import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.ArticuloTemp;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.otto.ImagesItems;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
public class DaoUpdateImageArticulo extends AsyncTask {
    AgregarArticuloActivity agregarArticuloActivity;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000000;
    RequestQueue queue = null;
    private Context ctx;
    public DaoUpdateImageArticulo(Context ctx, AgregarArticuloActivity agregarArticuloActivity,
                                  ArticuloTemp articulo){
        this.agregarArticuloActivity = agregarArticuloActivity;
        setJsonObject(articulo);
        queue = Volley.newRequestQueue(ctx);
        Log.d(Constants.appName, jsonObject.toString());
        this.ctx = ctx;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        try {
            try {
                String url = Constants.URL_BASE + Constants.UPDATE_IMAGES;
                executeRequestJson2(url);
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
                        callUpdateArticulo();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.appName, "ERROR ON onErrorResponse: "+Utils.getStackTrace(error));
                        //progressDialog.dismiss();
                    }
                });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }
    public void setJsonObject(ArticuloTemp object) {
        Log.d("save articulo: ", object.toString());
        this.jsonObject = createJsonObject(object);
    }

    public JSONObject createJsonObject(ArticuloTemp articulo) {
        JSONObject json = new JSONObject();
        try {
            json.put("nombreArticulo", articulo.getNombreArticulo());
            json.put("precio", articulo.getPrecio());
            json.put("descripcion", articulo.getDescripcion());
            json.put("idNegocio", articulo.getIdNegocio());
            json.put("imagenString", articulo.getImagenString());
            json.put("idArticulo", articulo.getIdArticulo());
        } catch (JSONException e) {
            System.out.println("*******************createJsonObject(Articulo articulo " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        System.out.println("JSObject: " + json.toString());
        return json;
    }



    public void callUpdateArticulo(){
        agregarArticuloActivity.showMessage("The image has been updated");
    }



}
