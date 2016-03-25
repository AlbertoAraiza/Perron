package admin.mx.com.perron.logic;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by jorge on 2/21/2016.
 */
public class ImageEncode {
    public String getEncodedString() {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile("imgPath", options);
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5), (int)(bitmap.getHeight()*0.5), true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }
    public byte[] getEncodedByte() {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile("imgPath", options);
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5), (int)(bitmap.getHeight()*0.5), true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byte_arr = stream.toByteArray();

        return byte_arr;
    }
    /*public static String getEncodedString(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5), (int)(bitmap.getHeight()*0.5), true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }*/

}
