package admin.mx.com.perron.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import admin.mx.com.perron.R;
import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.activities.ListArticulosActivity;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.utils.Constants;

/**
 * Created by jorge on 3/16/2016.
 */
public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.MyViewHolder> {

    private List<Articulo> articuloList;
    private Context mContext;
    public ArticuloAdapter(List<Articulo> articuloList, Context mContext) {
        this.articuloList = articuloList;
        this.mContext = mContext;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener{
        public TextView idArticulo, nombreArticulo, precio, descripcion, idNegocio;
        public Bitmap image;
        public ImageView imageViewer;
        public MyViewHolder(View view) {
            super(view);
            idArticulo = (TextView) view.findViewById(R.id.id_articulo_row);
            nombreArticulo = (TextView) view.findViewById(R.id.nombre_articulo_row);
            precio = (TextView) view.findViewById(R.id.precio_row);
            descripcion = (TextView) view.findViewById(R.id.descripcion_row);
            idNegocio = (TextView) view.findViewById(R.id.id_negocio_row);
            imageViewer = (ImageView)view.findViewById(R.id.image_row);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select the action");
            menu.add("Editar articulo").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int position = getLayoutPosition();
                    Toast.makeText(v.getContext(), " edit item!! ", Toast.LENGTH_SHORT).show();
                    callArticulo(position);
                    return true;
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            v.setOnCreateContextMenuListener(this);
            return false;
        }
        @Override
        public void onClick(View v) {

        }
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
        holder.imageViewer.setImageBitmap(articulo.getImageBitmap());
    }

    @Override
    public int getItemCount() {
        return articuloList.size();
    }
    public void callArticulo(int position){
        Intent intent = new Intent(getmContext(), AgregarArticuloActivity.class);
        intent.putExtra("action", Constants.UPDATE_ITEM);
        intent.putExtra("articulo", (Parcelable) getArticuloList().get(position));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getmContext().startActivity(intent);
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }
}
