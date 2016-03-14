package admin.mx.com.perron.utils;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.entities.Negocios;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
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
class FtpUtil extends AsyncTask {
    private Context ctx;
    ProgressDialog progressDialog;
    MainActivity ftpUpload;
    Bitmap bitmap;
    private String fileName;
    private JSONObject jsonObject;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;
    RequestQueue queue = null;
    int op;
    public FtpUtil(Context ctx, MainActivity ftpUpload, Bitmap bitmap, JSONObject jsonObject, int op){
        //public FtpCliente(Context ctx, MainActivity ftpUpload, Bitmap bitmap, JSONObject jsonObject){
        this.ctx = ctx;
        this.ftpUpload = ftpUpload;
        this.bitmap = bitmap;
        setJsonObject(jsonObject);
        queue = Volley.newRequestQueue(ctx);
        Log.d("Save or update:", jsonObject.toString());
        this.op = op;
    }
    private Exception exception;
    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "ftp.byethost16.com";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "b16_16833864";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="Temporal2005";


    String remoteDirectory = "/htdocs";


    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void uploadFile() throws Exception{
        //Utils.showMessage(ctx, app, "Caliz" );
        FTPClient client = null;
        //File fileName = new File("/storage/emulated/0/DCIM/Camera/IMG_20151031_165659.jpg");
        /*ByteArrayOutputStream out = new ByteArrayOutputStream();

        File fileName = convertImage2File(bitmap);
        client = new FTPClient();
        client.connect(FTP_HOST, 21);
        client.login(FTP_USER, FTP_PASS);
        client.setType(FTPClient.TYPE_BINARY);
        client.changeDirectory(remoteDirectory);
        client.upload(fileName, new MyTransferListener());*/
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            try {


            }catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog= ProgressDialog.show(ftpUpload, "Uploading image to the server","Uploading ....", true);

        //do initialization
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
        //Utils.showMessage(ctx, app, "File uploaded successfully");

    }

    /*******  Used to file upload and show progress  **********/

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            //btn.setVisibility(View.GONE);
            // Transfer started
            //Toast.makeText(ctx, " Upload Started ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" Upload Started ...");*/
        }

        public void transferred(int length) {
/*
            // Yet other length bytes has been transferred since the last time this
            // method was called
            Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
            //System.out.println(" transferred ..." + length);
            */
        }

        public void completed() {
/*
            btn.setVisibility(View.VISIBLE);
            // Transfer completed

            Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );
            */
        }

        public void aborted() {
/*
            btn.setVisibility(View.VISIBLE);
            // Transfer aborted
            Toast.makeText(getBaseContext()," transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );
            */
        }

        public void failed() {
/*
            btn.setVisibility(View.VISIBLE);
            // Transfer failed
            System.out.println(" failed ..." );
            */
        }

    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public File convertImage2File(Bitmap image) throws IOException {
        //create a file to write bitmap data
        String archivo = Utils.replaceBlank(null)+".jpg";

        CropSquareTransformation cop = new CropSquareTransformation();
        Bitmap original = cop.transform(bitmap);


        File filename = new File(Environment.getExternalStorageDirectory()
                + File.separator + archivo);

        filename.createNewFile();
//Convert bitmap to byte array

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        original = Bitmap.createScaledBitmap(original, 2000, 2000, true);
        original.compress(Bitmap.CompressFormat.JPEG, 1, bos);
//write the bytes in file
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(bos.toByteArray());
        fos.flush();
        fos.close();
        setFileName(archivo);
        return filename;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    public void executeRequestJson2(String url) throws Exception{

        JsonObjectRequest jsonObjReq = null;
        /*
        jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, actualizarNegocio(jsonObject), new Response.Listener<JSONObject>() {*/
        jsonObjReq = new JsonObjectRequest(Request.Method.POST,
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }
    public JSONObject actualizarNegocio(JSONObject json){
        Gson gson = new Gson();
        System.out.println("jsonObject: "+json.toString());
        Negocios neg = gson.fromJson(json.toString(), Negocios.class);

        neg.setLogotipo(neg.getLogotipo());
        JSONObject json2 = new JSONObject();
        try {
            json2.put("nombreNegocio", neg.getNombreNegocio());
            json2.put("direccion", neg.getDireccion());
            json2.put("coordenadas", neg.getCoordenadas());
            json2.put("logotipo", neg.getLogotipo());
        }catch (JSONException e) {
            System.out.println("******************************************************************************ERROR ON JSONOBject: ");
        }
        Log.d("Negociazo: ", json2.toString());
        return json2;
    }
}
