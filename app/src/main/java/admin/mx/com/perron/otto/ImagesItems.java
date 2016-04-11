package admin.mx.com.perron.otto;

import admin.mx.com.perron.entities.Articulo;

/**
 * Created by jorge on 4/2/2016.
 */
public class ImagesItems {
    private int idArticulo;
    private int idImagen;
    private byte[] imagen;
    private Articulo articulo;
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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}
