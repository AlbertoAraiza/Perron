package admin.mx.com.perron;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.CropSquareTransformation;
import admin.mx.com.perron.utils.Utils;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import static android.graphics.Bitmap.CompressFormat.WEBP;

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
    int op;
    public FtpCliente(Context ctx, MainActivity ftpUpload, Bitmap bitmap, JSONObject jsonObject, int op){
        this.ctx = ctx;
        this.ftpUpload = ftpUpload;
        this.bitmap = bitmap;
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
        progressDialog= ProgressDialog.show(ftpUpload, "Uploading image to the server","Uploading ....", true);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
    }
    public void executeRequestJson2(String url) throws Exception{

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST,
                url, jsonObject.toString(), new Response.Listener<JSONObject>() {

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
    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
