package admin.mx.com.perron.entities;
import android.os.Parcel;
import android.os.Parcelable;

public class Negocios implements Parcelable {
    private Long idNegocio;
    private String nombreNegocio;
    private String direccion;
    private String coordenadas;
    private String logotipo;

    public Negocios(Parcel in) {
        nombreNegocio = in.readString();
        direccion = in.readString();
        coordenadas = in.readString();
        logotipo = in.readString();
    }

    public static final Creator<Negocios> CREATOR = new Creator<Negocios>() {
        @Override
        public Negocios createFromParcel(Parcel in) {
            return new Negocios(in);
        }

        @Override
        public Negocios[] newArray(int size) {
            return new Negocios[size];
        }
    };

    public Negocios() {

    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(Long idNegocio) {
        this.idNegocio = idNegocio;
    }

    public String getLogotipo() {
        return logotipo;
    }

    public void setLogotipo(String logotipo) {
        this.logotipo = logotipo;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    @Override
    public String toString() {
        return "idNegocio: "+getIdNegocio()+", nombreNegocio: "+getNombreNegocio()+", direccion: "+getDireccion()+", coordenadas: "+getCoordenadas()+", logotipo"+getLogotipo();
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
        dest.writeString(logotipo);
    }
}