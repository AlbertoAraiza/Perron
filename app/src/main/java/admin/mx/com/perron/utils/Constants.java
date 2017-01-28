package admin.mx.com.perron.utils;

/**
 * Created by Jorge on 07/feb/2016.
 */
public interface Constants {
    public static final String appName = "Administrador android";

    public static final int ACTUALIZAR = 1;
    public static final int CREAR = 2;
    public static final int GUARDAR_ARTICULO = 3;
    public static final int GUARDAR_IMAGEN = 4;
    public static final int LISTAR_ARTICULOS = 5;
    public static final int LISTAR_IMAGENES = 6;
    public static final int ACTUALIZAR_ARTICULO = 7;
//    String URL_BASE ="http://192.168.0.11/picouWeb/public_html/php/";
    String URL_BASE ="http://picou.jvmhost.net/mavenproject1-1.0-SNAPSHOT/rest/v1/status/";
//    String URL_BASE ="http://picou.000webhostapp.com/php/";
    //String SAVE_METHOD = "agregarNegocio.php";
    String SAVE_METHOD = "post";
    String LISTA_NEGOCIOS = "getNegocios.php";
    String UPDATE_METHOD = "update.php";
    String DELETE_METHOD = "delete.php";
    String SAVE_IMAGE = "saveImages.php";
    String SAVE_ITEM = "saveItem.php";
    String GET_IMAGES = "getImages.php";
    String UPDATE_ITEMS = "updateItem.php";
    String UPDATE_IMAGES_ITEM = "updateImageItem.php";
    String DELETE_IMAGES_ITEM = "deleteImageItem.php";
    String UPDATE_MAIN_IMAGEN = "updateMainImage.php";

//    public static final String URL_BASE ="http://puntodeventa-aguascalientes.rhcloud.com/mobile/rest/v1/status/";
//    public static final String SAVE_METHOD = "post";
//    public static final String URL_BASE ="http://puntodeventa-aguascalientes.rhcloud.com/mobile/rest/v1/status/";
    public static final int NEW_ITEM = 99;
    public static final int UPDATE_ITEM = 100;
    /*Nombres de los metodos que se llaman desde la invocacion de los webservices*/
    public static final String UPDATE_IMAGES = "updateImages";
    public static final String GET_ARTICULOS = "getArticulos.php";
    public static final int LISTA_VACIA = 1;
    public static final int LISTA_LLENA = 2;

}

