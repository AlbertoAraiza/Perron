package admin.mx.com.perron.entities;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by jorge on 3/15/2016.
 */
public class ArticuloTemp{
    public ArticuloTemp() {
    }
    private int idArticulo;
    private String nombreArticulo;
    private double precio;
    private String descripcion;
    private int idNegocio;
    private byte[] imagen;
    private String imagenString;


    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(int idNegocio) {
        this.idNegocio = idNegocio;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenString() {
        return imagenString;
    }

    public void setImagenString(String imagenString) {
        this.imagenString = imagenString;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("idNegocio: ");
        stringBuffer.append(getIdNegocio());
        stringBuffer.append(", nombreArticulo: ");
        stringBuffer.append(getNombreArticulo());
        stringBuffer.append(", precio: ");
        stringBuffer.append(getPrecio());
        stringBuffer.append(", descripcion: ");
        stringBuffer.append(getDescripcion());
        stringBuffer.append(", idArticulo: ");
        stringBuffer.append(getIdArticulo());
        return stringBuffer.toString();
    }
}
