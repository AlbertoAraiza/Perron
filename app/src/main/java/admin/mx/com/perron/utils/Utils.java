package admin.mx.com.perron.utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import admin.mx.com.perron.entities.Negocios;

public class Utils {
    private static Bus bus;

    public Utils(){

    }
    private static StringBuffer globalMessage = new StringBuffer("");
    public static void showMessage(Context context, final AppCompatActivity nuevaClase, String text ){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        alertDialogBuilder
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        nuevaClase.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    public static void showMessage(Context context, final AppCompatActivity nuevaClase,Exception e  ){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        alertDialogBuilder
                .setMessage(getStackTrace(e))
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        nuevaClase.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    public static StringBuffer getGlobalMessage() {
        return globalMessage;
    }

    public static void setGlobalMessage(StringBuffer globalMessage) {
        Utils.globalMessage = globalMessage;
    }
    public static String replaceBlank(String str){
        if(str==null){
            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);
            str = ts.toString();
        }
        StringBuffer strBuffer = new StringBuffer("");
        for(int i=0;i<str.length();i++){
            if(i==0){
                if(str.substring(i,1).equals(" ")||str.substring(i,1).equals(":")){
                    strBuffer.append("_");
                }else{
                    strBuffer.append(str.substring(i,1));
                }
            }else {
                if(str.substring(i,i+1).equals(" ")||str.substring(i,i+1).equals(":")){
                    strBuffer.append("_");
                }else{
                    strBuffer.append(str.substring(i,i+1));
                }
            }
        }
        return strBuffer.toString();
    }

    public static String convertGson(Negocios neg) {
        Gson gson = new Gson();
        String json = gson.toJson(neg);
        return json;
    }

    public static Negocios createNegocios(String neg) {
        Gson gson = new Gson();
        Negocios negocios = gson.fromJson(neg, Negocios.class);
        return negocios;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
// RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }
    public static String getEncodedString(Bitmap bitmap) {


        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5), (int)(bitmap.getHeight()*0.5), true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = android.util.Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }

    public static Bus getBus() {
        if(bus==null){
            bus = new Bus();
        }
        return bus;
    }
    public static Bitmap getBitmapFromImageView(ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return bitmap;
    }
    public static byte[] stringToBytesUTFNIO(String str) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length << 1];
        CharBuffer cBuffer = ByteBuffer.wrap(b).asCharBuffer();
        for(int i = 0; i < buffer.length; i++)
        cBuffer.put(buffer[i]);
        return b;
    }
}
