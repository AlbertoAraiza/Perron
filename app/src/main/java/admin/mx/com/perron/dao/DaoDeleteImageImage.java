package admin.mx.com.perron.dao;
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
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 4/30/2016.
 */
public class DaoDeleteImageImage extends AsyncTask {
    AgregarArticuloActivity agregarArticuloActivity;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000000;
    RequestQueue queue = null;
    private Context ctx;
    public DaoDeleteImageImage(Context ctx, AgregarArticuloActivity agregarArticuloActivity,
                               Images images){
        this.agregarArticuloActivity = agregarArticuloActivity;
        setJsonObject(images);
        queue = Volley.newRequestQueue(ctx);
        Log.d(Constants.appName, jsonObject.toString());
        this.ctx = ctx;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        try {
            try {
                String url = Constants.URL_BASE + Constants.DELETE_IMAGES_ITEM;
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
    public void setJsonObject(Images object) {
        this.jsonObject = createJsonObject(object);
    }
    public JSONObject createJsonObject(Images images) {
        JSONObject json = new JSONObject();
        try {
            json.put("idArticulo",123);
            json.put("idImagen", images.getIdImagen());
            json.put("imagenString", images.getImagenString());
        } catch (JSONException e) {
            System.out.println("*********************createJsonObject(Images images " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        Log.d(Constants.appName, "createJsonObject: " + json.toString());
        return json;
    }



    public void callUpdateArticulo(){
        agregarArticuloActivity.showMessage("The imageimageimage has been DELETED");
    }



}
