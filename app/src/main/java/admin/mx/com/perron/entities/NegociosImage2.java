package admin.mx.com.perron.entities;

public class NegociosImage2 {
    public NegociosImage2() {} // JAXB needs this

    private Long idNegocio;
    private String nombreNegocio;
    private String direccion;
    private String coordenadas;
    private String logotipo;
    private byte[] image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}