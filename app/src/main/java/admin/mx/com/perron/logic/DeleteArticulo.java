package admin.mx.com.perron.logic;
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

import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/12/2016.
 */
public class DeleteArticulo extends AsyncTask{
    private Context ctx;
    RequestQueue queue = null;
    Negocios negociosImage;




        public DeleteArticulo(Context ctx, Negocios negociosImage){
            this.ctx = ctx;
            queue = Volley.newRequestQueue(ctx);
            this.negociosImage = negociosImage;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Log.d(Constants.appName, "doInBackground");
                String url = Constants.URL_BASE + Constants.DELETE_METHOD;
                executeRequestJson2(url);
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
            Log.d(Constants.appName, "Creating windows to show progress of activity");
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d(Constants.appName, "DEstroying window to show progress of activity");
        }


    public void executeRequestJson2(String url) throws Exception{

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST,
                        url, getJsonObject2().toString(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String input = response.toString();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("ERROR ON onErrorResponse: "+Utils.getStackTrace(error));
                    }
                });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                //MY_SOCKET_TIMEOUT_MS,
                5000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }

    public JSONObject getJsonObject2() {
        JSONObject json = new JSONObject();
        try {
            json.put("nombreNegocio", negociosImage.getNombreNegocio());
            json.put("direccion", negociosImage.getDireccion());
            json.put("coordenadas", negociosImage.getCoordenadas());
            json.put("idNegocio", negociosImage.getIdNegocio());
        } catch (JSONException e) {
            System.out.println("******************************************************************************ERROR ON JSONOBject: " + Utils.getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        System.out.println("JSObject: " + json.toString());
        return json;
    }

}
