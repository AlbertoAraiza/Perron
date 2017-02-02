package admin.mx.com.perron;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import admin.mx.com.perron.entities.MessageError;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;

/**
 * Created by Jorge on 04/nov/2015.
 */
class FtpCliente extends AsyncTask {
    private Context ctx;
    ProgressDialog progressDialog;
    MainActivity ftpUpload;
    Bitmap bitmap;
    private String fileName;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;
    RequestQueue queue = null;
    int op, position;
    public FtpCliente(Context ctx, MainActivity ftpUpload, Bitmap bitmap, JSONObject jsonObject, int op, int position){
        this.ctx = ctx;
        this.ftpUpload = ftpUpload;
        this.bitmap = bitmap;
        this.position = position;
        setJsonObject(jsonObject);
        queue = Volley.newRequestQueue(ctx);
        Log.d("Save or update:",jsonObject.toString());
        this.op = op;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            try {

                if (op == Constants.CREAR) {
                    String url = Constants.URL_BASE +Constants.SAVE_METHOD;
                    executeRequestJson2(url);
                }else if (op == Constants.ACTUALIZAR) {
                    String url =  Constants.URL_BASE +Constants.UPDATE_METHOD;
                    executeRequestJson2(url);
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
                        Gson gson = new Gson();
                        try{
                            MyProperties myProperties = MyProperties.getInstance();
                            String input = null;
                            MessageError messageError = gson.fromJson(response.toString(), MessageError.class);
                            if(messageError.isResult())
                                Toast.makeText(ctx, messageError.getMessage(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ctx, "Error: "+messageError.getMessage(), Toast.LENGTH_SHORT).show();
//                            messageError.isResult()? Toast.makeText(ctx, "Error: " + messageError.getMessage(),Toast.LENGTH_LONG).show() : Toast.makeText(ctx, "Error: "+messageError.getMessage(), Toast.LENGTH_SHORT).show();
                            if (op == Constants.ACTUALIZAR) {
                                try {
                                    if (bitmap != null) {
                                        input = response.getString("id");
                                    } else {
                                        input = myProperties.listaNegocios.get(position).getLogotipo();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Negocios nego = ftpUpload.createNegocioUpdate();
                                nego.setLogotipo(input);
                                myProperties.listaNegocios.set(position, nego);
                                myProperties.listNego.getAdapter().notifyDataSetChanged();
                            }
                        }catch(Exception e){
                            try {
                                MessageError messageError = gson.fromJson(response.toString(), MessageError.class);
                                ftpUpload.mostrarMesaje(messageError);
                            }catch(Exception ex){
                                MessageError messageError = new MessageError("Error inesperado: "+ex.getMessage(), false);
                                ftpUpload.mostrarMesaje(messageError);
                            }
                        }
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
    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
