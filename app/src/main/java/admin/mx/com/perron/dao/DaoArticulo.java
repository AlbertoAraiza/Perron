package admin.mx.com.perron.dao;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.utils.MyProperties;

public class DaoArticulo {
    public DaoArticulo() {
    }

    private Context ctx;
    private int op;
    private AgregarArticuloActivity agregarArticuloActivity;
    public DaoArticulo(Context ctx, int op, AgregarArticuloActivity agregarArticuloActivity) {
        this.ctx = ctx;
        this.op = op;
        this.agregarArticuloActivity = agregarArticuloActivity;
    }
    public DaoArticulo(Context ctx, int op) {
        this.ctx = ctx;
        this.op = op;
    }
    public List<Articulo> getListaArticulo(){
        List<Articulo> listaArticulo = null;
        DatabaseObject dao = new DatabaseObject(ctx, op);
        dao.execute();
        return listaArticulo;
    }
    public void saveArticulo(Articulo articulo){
        DatabaseObject dao = new DatabaseObject(ctx, agregarArticuloActivity, articulo, op);
        dao.execute();
    }

}
