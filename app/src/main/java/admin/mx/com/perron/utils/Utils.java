package admin.mx.com.perron.utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.Date;

import admin.mx.com.perron.entities.Negocios;

public class Utils {
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
}
