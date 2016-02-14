package admin.mx.com.perron.logic;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import admin.mx.com.perron.activities.MenuMain;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by Jorge on 06/feb/2016.
 */
public class ListaResultado extends AsyncTask {
    private Context ctx;
    ProgressDialog progressDialog;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;
    RequestQueue queue = null;
    TextView textIp;
    public MenuMain ma;
    public ListaResultado(Context ctx, MenuMain ma){
        this.ctx = ctx;
        queue = Volley.newRequestQueue(ctx);
        this.ma = ma;
    }

    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Log.d(Constants.appName, "doInBackground");
            executeRequestJson2("");
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
        progressDialog= ProgressDialog.show(ma, "Getting data","Receiving data ....", true);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    public void executeRequestJson2(String data) throws Exception{
        String url = "http://192.168.1.222:8080/publicidad/rest/v1/status/getNegocios";
        StringRequest stringReq = null;
        stringReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String input = response.toString();
                Log.d(Constants.appName, "response : "+input);
                getListaNegocios(input);
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
    public JSONObject actualizarNegocio2(){
        JSONObject json2 = new JSONObject();
        try {
            json2.put("nombreNegocio", "nombre");
            json2.put("direccion", "direccion");
            json2.put("coordenadas", "coordenadas");
            json2.put("logotipo", "logotipo");
        }catch (JSONException e) {
            System.out.println("******************************************************************************ERROR ON JSONOBject: ");
        }
        Log.d(Constants.appName, json2.toString());
        return json2;
    }
    public void getListaNegocios(String listaNegocio) {
        Log.d(Constants.appName, "getListaNegocios(String listaNegocio) : "+listaNegocio);
        Gson gson = new Gson();
        List<Negocios> listaNegocios = gson.fromJson(listaNegocio, new TypeToken<List<Negocios>>(){}.getType());
        for(int i=0;i<listaNegocios.size();i++){
            Negocios neg = (Negocios)listaNegocios.get(i);
            Log.d(Constants.appName, neg.toString());
        }
        Log.d(Constants.appName, "Converting to JSonArray : "+listaNegocios);
        ma.loadListNegocios(listaNegocios);
    }
}
