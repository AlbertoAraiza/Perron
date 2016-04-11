package admin.mx.com.perron.entities;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class NegociosImage implements Parcelable {
    private Long idNegocio;
    private String nombreNegocio;
    private String direccion;
    private String coordenadas;
    private Bitmap logotipo;


    public NegociosImage(Parcel in) {
        nombreNegocio = in.readString();
        direccion = in.readString();
        coordenadas = in.readString();
        logotipo = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<NegociosImage> CREATOR = new Creator<NegociosImage>() {
        @Override
        public NegociosImage createFromParcel(Parcel in) {
            return new NegociosImage(in);
        }

        @Override
        public NegociosImage[] newArray(int size) {
            return new NegociosImage[size];
        }
    };

    public NegociosImage() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreNegocio);
        dest.writeString(direccion);
        dest.writeString(coordenadas);
        dest.writeParcelable(logotipo, flags);
    }

    public Long getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(Long idNegocio) {
        this.idNegocio = idNegocio;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Bitmap getLogotipo() {
        return logotipo;
    }

    public void setLogotipo(Bitmap logotipo) {
        this.logotipo = logotipo;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("idNegocio: ");
        str.append(idNegocio);
        str.append(", nombreNegocio: ");
        str.append(nombreNegocio);
        str.append(", direccion: ");
        str.append(direccion);
        str.append(", coordenadas: ");
        str.append(coordenadas);
        return str.toString();
    }
}