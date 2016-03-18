package admin.mx.com.perron.dao;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.utils.Utils;

/**
 * Created by jorge on 3/16/2016.
 */
public class DaoArticulo {
    public List<Articulo> getListaArticulo(){
        List<Articulo> listaArticulo = null;
        try{
            listaArticulo = new ArrayList<Articulo>();
            for(int i=0; i<10; i++){
                Articulo articulo = new Articulo();
                articulo.setDescripcion("Descripcion " + i);
                articulo.setIdArticulo(i);
                articulo.setIdNegocio(i);
                articulo.setNombreArticulo("nombre articulo " + i);
                articulo.setPrecio(i);
                listaArticulo.add(articulo);
            }
        }catch(Exception e){
            Log.d("Error:ListaNEgocios: ", Utils.getStackTrace(e));
        }
        return listaArticulo;
    }
}
