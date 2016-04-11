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
    public DaoArticulo(Context ctx, int op) {
        this.ctx = ctx;
        this.op = op;
    }
    public void getListaArticulo(Articulo art){
        DatabaseObject dao = new DatabaseObject(ctx,art, op);
        dao.execute();
    }
    public void saveArticulo(Articulo articulo){
        DatabaseObject dao = new DatabaseObject(ctx, agregarArticuloActivity, articulo, op);
        dao.execute();
    }
    public void actualizarArticulo(Articulo articulo){
        DatabaseObject dao = new DatabaseObject(ctx, articulo, op);
        dao.execute();
    }

}
