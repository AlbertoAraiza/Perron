package admin.mx.com.perron.dao;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.R;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/19/2016.
 */
public class DaoImages {
    public static List<Images> listaImages = null;
    public DaoImages() {
    }
    public static List<Images> getListaImages(){
        return listaImages;
    }
    public void saveImage(Images image) {
        if(listaImages==null) {
            listaImages = new ArrayList<Images>();
        }
        listaImages.add(image);
    }
}
