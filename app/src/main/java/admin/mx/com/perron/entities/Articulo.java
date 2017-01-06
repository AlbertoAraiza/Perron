package admin.mx.com.perron.entities;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by jorge on 3/15/2016.
 */
public class Articulo implements Parcelable{
    public Articulo() {
    }
    private int idArticulo;
    private String nombreArticulo;
    private double precio;
    private String descripcion;
    private int idNegocio;
    private String imagen;

    protected Articulo(Parcel in) {
        idArticulo = in.readInt();
        nombreArticulo = in.readString();
        precio = in.readDouble();
        descripcion = in.readString();
        idNegocio = in.readInt();
        imagen = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idArticulo);
        dest.writeString(nombreArticulo);
        dest.writeDouble(precio);
        dest.writeString(descripcion);
        dest.writeInt(idNegocio);
        dest.writeString(imagen);
    }
    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("idNegocio: ");
        stringBuffer.append(getIdNegocio());
        stringBuffer.append("\n, nombreArticulo: ");
        stringBuffer.append(getNombreArticulo());
        stringBuffer.append("\n, precio: ");
        stringBuffer.append(getPrecio());
        stringBuffer.append("\n, descripcion: ");
        stringBuffer.append(getDescripcion());
        stringBuffer.append("\n, idArticulo: ");
        stringBuffer.append(getIdArticulo());
        return stringBuffer.toString();
    }
}
