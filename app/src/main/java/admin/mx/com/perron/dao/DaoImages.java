package admin.mx.com.perron.dao;
import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.entities.Images;

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
