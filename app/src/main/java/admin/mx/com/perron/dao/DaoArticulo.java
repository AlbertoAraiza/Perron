package admin.mx.com.perron.dao;
import java.util.ArrayList;
import java.util.List;
import admin.mx.com.perron.entities.Articulo;
public class DaoArticulo {
    public static List<Articulo> listaArticulo = null;
    public List<Articulo> getListaArticulo(){
        if(listaArticulo==null) {
            listaArticulo = new ArrayList<Articulo>();
        }
        return listaArticulo;
    }
    public void saveArticulo(Articulo articulo){

        listaArticulo.add(articulo);
    }
}
