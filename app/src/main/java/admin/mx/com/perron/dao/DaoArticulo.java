package admin.mx.com.perron.dao;
import android.content.Context;

import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.entities.Articulo;

public class DaoArticulo  {

    public DaoArticulo() {
    }
    private Context ctx;
    private int op;
    private int position;
    private AgregarArticuloActivity agregarArticuloActivity;
    public DaoArticulo(Context ctx, int op, int position) {
        this.position = position;
        this.ctx = ctx;
        this.op = op;
    }

    public void getListaArticulo(Articulo art){
        DatabaseObject dao = new DatabaseObject(ctx,art, op,position);
        dao.execute();
    }
    public void saveArticulo(Articulo articulo){
        DatabaseObject dao = new DatabaseObject(ctx, agregarArticuloActivity, articulo, op, position);
        dao.execute();
    }
    public void actualizarArticulo(Articulo articulo){
        DatabaseObject dao = new DatabaseObject(ctx, articulo, op,position);
        dao.execute();
    }

}
