package admin.mx.com.perron.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jorge on 3/15/2016.
 */
public class Articulo implements Parcelable{
    private int idArticulo;
    private String nombreArticulo;
    private double precio;
    private String descripcion;
    private int idNegocio;
    private byte[] image;
    private Bitmap imageBitmap;

    protected Articulo(Parcel in) {
        idArticulo = in.readInt();
        nombreArticulo = in.readString();
        precio = in.readDouble();
        descripcion = in.readString();
        idNegocio = in.readInt();
        image = in.createByteArray();
        imageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public Articulo() {

    }
    public static final Creator<Articulo> CREATOR = new Creator<Articulo>() {
        @Override
        public Articulo createFromParcel(Parcel in) {
            return new Articulo(in);
        }

        @Override
        public Articulo[] newArray(int size) {
            return new Articulo[size];
        }
    };

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    @Override
    public String toString(){
        return this.nombreArticulo+", $"+this.precio+", "+this.descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idArticulo);
        dest.writeString(nombreArticulo);
        dest.writeDouble(precio);
        dest.writeString(descripcion);
        dest.writeInt(idNegocio);
        dest.writeByteArray(image);
        dest.writeParcelable(imageBitmap, flags);
    }
}
