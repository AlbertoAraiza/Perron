package admin.mx.com.perron.adapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import java.util.List;
import admin.mx.com.perron.R;
import admin.mx.com.perron.activities.AgregarArticuloActivity;
import admin.mx.com.perron.activities.ListArticulosActivity;
import admin.mx.com.perron.dao.DatabaseObject;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.otto.ImagesItems;
import admin.mx.com.perron.otto.Items;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/16/2016.
 */
public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.MyViewHolder> {
    private List<Articulo> articuloList;
    private Context mContext;
    private int position;
    public ArticuloAdapter(List<Articulo> articuloList, Context mContext) {
        super();
        this.articuloList = articuloList;
        this.mContext = mContext;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener{
        public TextView idArticulo, nombreArticulo, precio, descripcion, idNegocio;
        public Bitmap image;
        public ImageView imageViewer;
        public MyViewHolder(View view) {
            super(view);
            if (MyProperties.getInstance().listaValor == Constants.LISTA_LLENA) {
                idArticulo = (TextView) view.findViewById(R.id.id_articulo_row);
                nombreArticulo = (TextView) view.findViewById(R.id.nombre_articulo_row);
                precio = (TextView) view.findViewById(R.id.precio_row);
                descripcion = (TextView) view.findViewById(R.id.descripcion_row);
                idNegocio = (TextView) view.findViewById(R.id.id_negocio_row);
                imageViewer = (ImageView) view.findViewById(R.id.image_row);
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
                view.setOnCreateContextMenuListener(this);
            }
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select the action");
            menu.add("Editar articulo").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    position = getLayoutPosition();
                    Articulo articulo = (Articulo) getArticuloList().get(position);
                    Log.d(Constants.appName, "Articulo a actualizar: "+articulo.toString());
                    DatabaseObject dao = new DatabaseObject(v.getContext(),articulo,  Constants.LISTAR_IMAGENES);
                    dao.execute();
                    return true;
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            if (MyProperties.getInstance().listaValor == Constants.LISTA_LLENA) {
                v.setOnCreateContextMenuListener(this);
            }
            return false;
        }
        @Override
        public void onClick(View v) {

        }
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == EMPTY_VIEW) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_empty, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.articulos_list_row, parent, false);
        }
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (!(MyProperties.getInstance().listaValor == Constants.LISTA_VACIA)) {
            Articulo articulo = articuloList.get(position);
            holder.idArticulo.setText(articulo.getIdArticulo() + "");
            holder.nombreArticulo.setText(articulo.getNombreArticulo());
            holder.precio.setText(articulo.getPrecio() + "");
            holder.descripcion.setText(articulo.getDescripcion());
            holder.idNegocio.setText(articulo.getIdNegocio() + "");
            holder.imageViewer.setImageBitmap(articulo.getImageBitmap());
        }
    }

    @Override
    public int getItemCount() {
        if( MyProperties.getInstance().listaValor == Constants.LISTA_VACIA){
            return Constants.LISTA_VACIA;
        }else {
            return articuloList.size();
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (MyProperties.getInstance().listaValor == Constants.LISTA_VACIA) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
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
    private static final int EMPTY_VIEW = 10;




}
