package admin.mx.com.perron.entities;

import android.graphics.Bitmap;

/**
 * Created by jorge on 3/19/2016.
 */
public class Images {
    private int idArticulo;
    private int idImagen;
    private Bitmap image;

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
