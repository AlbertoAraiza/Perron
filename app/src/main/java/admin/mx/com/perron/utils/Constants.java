package admin.mx.com.perron.utils;

/**
 * Created by Jorge on 07/feb/2016.
 */
public interface Constants {
    public static final String appName = "Administrador android";
    public static final String LISTA_NEGOCIOS = "getNegocios";
    public static final int ACTUALIZAR = 1;
    public static final int CREAR = 2;
    public static final int GUARDAR_ARTICULO = 3;
    public static final int GUARDAR_IMAGEN = 4;
    public static final int LISTAR_ARTICULOS = 5;
    public static final String URL_BASE ="http://192.168.1.222:8080/publicidad2/rest/v1/status/";
    public static final String SAVE_METHOD = "post";
    public static final String UPDATE_METHOD = "update";
    public static final String DELETE_METHOD = "delete";
    public static final String SAVE_ITEM = "saveItem";
    public static final String SAVE_IMAGE = "saveImages";
    //public static final String URL_BASE ="http://puntodeventa-aguascalientes.rhcloud.com/mobile/rest/v1/status/";
    public static final int NEW_ITEM = 99;
    public static final int UPDATE_ITEM = 100;
    /*Nombres de los metodos que se llaman desde la invocacion de los webservices*/
    public static final String UPDATE_IMAGES = "updateImages";
    public static final String UPDATE_ITEMS = "updateItem";
    public static final String GET_IMAGES = "getImages";
    public static final String GET_ARTICULOS = "getArticulos";
}
