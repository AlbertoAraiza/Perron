package admin.mx.com.perron.logic;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
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

import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.activities.MenuMain;
import admin.mx.com.perron.entities.MessageError;
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
        Log.d(Constants.appName, "Creating windows to show progress of activity");
        progressDialog= ProgressDialog.show(ma, "Getting data","Receiving data ....", true);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d(Constants.appName, "Destroying window to show progress of activity");
        progressDialog.dismiss();
    }

    public void executeRequestJson2(String data) throws Exception{
        String url = Constants.URL_BASE+Constants.LISTA_NEGOCIOS;
        StringRequest stringReq;
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
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    Toast.makeText(ma, "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                }else{
                    String errorMessage=error.getClass().getSimpleName();
                    if(!TextUtils.isEmpty(errorMessage)){
                        Toast.makeText(ma,"errorMessage:"+errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        stringReq.setRetryPolicy(new DefaultRetryPolicy(
        MY_SOCKET_TIMEOUT_MS,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringReq);
    }
    public void getListaNegocios(String listaNegocio) {
        Gson gson = new Gson();
        try{
            List<Negocios> listaNegocios = gson.fromJson(listaNegocio, new TypeToken<List<Negocios>>(){}.getType());
            Log.d(Constants.appName, "listaNegocio: "+listaNegocio);
            List<Negocios> listaTemporal = new ArrayList<Negocios>();
            for(int i=0;i<listaNegocios.size();i++){
                Negocios neg = (Negocios)listaNegocios.get(i);
                listaTemporal.add(neg);
                Log.d(Constants.appName, neg.toString());
            }
            ma.loadListNegocios(listaTemporal);
        }catch(Exception e){
            try {
                MessageError messageError = gson.fromJson(listaNegocio, MessageError.class);
                ma.showError(messageError);
            }catch(Exception ex){
                MessageError messageError = new MessageError("Error inesperado: "+ex.getMessage(), false);
                ma.showError(messageError);
            }
        }
    }
}
