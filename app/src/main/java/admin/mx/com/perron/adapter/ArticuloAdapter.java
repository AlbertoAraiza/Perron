package admin.mx.com.perron.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import admin.mx.com.perron.R;
import admin.mx.com.perron.entities.Articulo;

/**
 * Created by jorge on 3/16/2016.
 */
public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.MyViewHolder> {

    private List<Articulo> articuloList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView idArticulo, nombreArticulo, precio, descripcion, idNegocio;
        public Bitmap image;

        public MyViewHolder(View view) {
            super(view);
            idArticulo = (TextView) view.findViewById(R.id.id_articulo_row);
            nombreArticulo = (TextView) view.findViewById(R.id.nombre_articulo_row);
            precio = (TextView) view.findViewById(R.id.precio_row);
            descripcion = (TextView) view.findViewById(R.id.descripcion_row);
            idNegocio = (TextView) view.findViewById(R.id.id_negocio_row);
        }
    }


    public ArticuloAdapter(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.articulos_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Articulo articulo = articuloList.get(position);
        holder.idArticulo.setText(articulo.getIdArticulo()+"");
        holder.nombreArticulo.setText(articulo.getNombreArticulo());
        holder.precio.setText(articulo.getPrecio()+"");
        holder.descripcion.setText(articulo.getDescripcion());
        holder.idNegocio.setText(articulo.getIdNegocio()+"");
    }

    @Override
    public int getItemCount() {
        return articuloList.size();
    }
}
