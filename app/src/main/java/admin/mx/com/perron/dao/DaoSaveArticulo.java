package admin.mx.com.perron.dao;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.R;
import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.otto.Article;
import admin.mx.com.perron.otto.ImagesItems;
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
    public DaoSaveArticulo(Context ctx, AgregarArticuloActivity agregarArticuloActivity,
                          Articulo articulo){
        this.agregarArticuloActivity = agregarArticuloActivity;
        setJsonObject(articulo);
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
                        String id = "";
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(input);
                            id = jObject.getString("id");
                            Log.d(Constants.appName, "id: "+id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        agregarArticuloActivity.showNewImageAfterSave(agregarArticuloActivity.getPhoto(), id);
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



    public void launchBarDialog() {
        barProgressDialog = new ProgressDialog(agregarArticuloActivity);
        barProgressDialog.setTitle("Downloading Image ...");
        barProgressDialog.setMessage("Download in progress ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
    }

}
