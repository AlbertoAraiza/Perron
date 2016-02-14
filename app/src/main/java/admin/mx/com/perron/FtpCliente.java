package admin.mx.com.perron;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.Utils;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
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
    public FtpCliente(Context ctx, MainActivity ftpUpload, Bitmap bitmap, JSONObject jsonObject){
        this.ctx = ctx;

        this.ftpUpload = ftpUpload;
        this.bitmap = bitmap;
        setJsonObject(jsonObject);
        queue = Volley.newRequestQueue(ctx);
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
    public void uploadFile() throws Exception{
        //Utils.showMessage(ctx, app, "Caliz" );
        FTPClient client = null;
        //File fileName = new File("/storage/emulated/0/DCIM/Camera/IMG_20151031_165659.jpg");
        File fileName = convertImage2File(bitmap);
        client = new FTPClient();
        client.connect(FTP_HOST, 21);
        client.login(FTP_USER, FTP_PASS);
        client.setType(FTPClient.TYPE_BINARY);
        client.changeDirectory(remoteDirectory);

        client.upload(fileName, new MyTransferListener());
    }

    @Override
    protected Object doInBackground(Object[] params) {
        //Utils.showMessage(ctx, app, "uploading file" );
        try {
            uploadFile();
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

        //do initialization
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
        //Utils.showMessage(ctx, app, "File uploaded successfully");
        try {
            executeRequestJson2("");
        }catch (Exception e) {
            MainActivity.getStackTrace(e);
            e.printStackTrace();
        }
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
    public File convertImage2File(Bitmap image) throws IOException {
        //create a file to write bitmap data
        String archivo = Utils.replaceBlank(null)+".png";


        File f = new File(ctx.getCacheDir(), archivo);

        f.createNewFile();
//Convert bitmap to byte array
        Bitmap bitmap = image;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        setFileName(archivo);
        return f;
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
    public void executeRequestJson2(String data) throws Exception{
        //String url = "http://192.168.0.244:8080/publicidad/rest/v1/status/post";
        String url = "http://192.168.1.222:8080/publicidad/rest/v1/status/post";
        JsonObjectRequest jsonObjReq = null;
        jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, actualizarNegocio(jsonObject), new Response.Listener<JSONObject>() {

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
        Negocios neg = gson.fromJson(json.toString(), Negocios.class);

        neg.setLogotipo(neg.getLogotipo()+getFileName());
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
