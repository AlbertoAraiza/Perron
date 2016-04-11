package admin.mx.com.perron.utils;

import java.util.List;

import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.entities.NegociosImage;

/**
 * Created by jorge on 2/27/2016.
 */
public class MyProperties {
    private static MyProperties mInstance= null;
    public List<Articulo> listaArticulos;
    public List<Images> listaImagenes;
    public List<NegociosImage> listaNegocios;
    public String listaImages;
    public int listaValor;
    protected MyProperties(){}
    public static synchronized MyProperties getInstance(){
        if(null == mInstance){
            mInstance = new MyProperties();
        }
        return mInstance;
    }
}
