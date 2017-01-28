package admin.mx.com.perron.adapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
import com.squareup.picasso.Picasso;

import java.util.List;
import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.R;
import admin.mx.com.perron.activities.ListArticulosActivity;
import admin.mx.com.perron.dao.DaoArticulo;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.logic.DeleteArticulo;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;

/**
 * Created by Jorge on 07/feb/2016.
 */
public class NegociosAdapter extends RecyclerView.Adapter<NegociosAdapter.NegocioViewHolder>{
    private List<Negocios> negociosList;
    private Context mContext;
    private ContextMenu.ContextMenuInfo mContextMenuInfo = null;
    public static final int DELETE_RECORD = 0;
    public static final int UPDATE_RECORD = 1;
    int position;
    public NegociosAdapter(List<Negocios> negociosList, Context mContext) {
        super();
        this.negociosList = negociosList;
        this.mContext = mContext;
    }
    @Override
    public NegocioViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.negocios_layout, viewGroup, false);

        return new NegocioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NegocioViewHolder holder, int position) {
        Negocios negocio = negociosList.get(position);
        Picasso.with(mContext)
                .load(Constants.URL_BASE+negocio.getLogotipo())
                .error(R.drawable.not_available)
                .into(holder.vLogotipo);
        holder.vNombreNegocio.setText(negocio.getNombreNegocio());
        holder.vDireccion.setText(negocio.getDireccion());
        holder.vCoordenadas.setText(negocio.getCoordenadas());
        holder.vIdRecord.setText(negocio.getIdNegocio()+"");
        holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        return negociosList.size();
    }

    public class NegocioViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener{
        public ImageView vLogotipo;
        public TextView vNombreNegocio;
        public TextView vDireccion;
        public TextView vCoordenadas;
        public TextView vIdRecord;


        public NegocioViewHolder(View v) {
            super(v);
            vLogotipo =  (ImageView) v.findViewById(R.id.logotipo);
            vNombreNegocio = (TextView)  v.findViewById(R.id.nombre_negocio);
            vDireccion = (TextView)  v.findViewById(R.id.direccion);
            vCoordenadas = (TextView) v.findViewById(R.id.coordenadas);
            vIdRecord = (TextView)v.findViewById(R.id.id_record);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(Constants.appName, "Opcion 2");
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.putExtra("option", Constants.ACTUALIZAR);
            int position = getLayoutPosition();
            intent.putExtra("position", position);
            //intent.putExtra("option", getNegociosList().get(getAdapterPosition()));
            v.getContext().startActivity(intent);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add("Borrar registro permanentemente?").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    int position = getLayoutPosition();
                                    Negocios negociosImage = negociosList.get(position);
                                    negociosList.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(v.getContext(), " Registro borrado!! " + negociosImage.getIdNegocio(), Toast.LENGTH_SHORT).show();
                                    DeleteArticulo deleteArticulo = new DeleteArticulo(getmContext(), negociosImage);
                                    deleteArticulo.execute();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    return true;
                }
            });
            menu.add("Agregar articulo").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        position = getLayoutPosition();
                        MyProperties.getInstance().negocio = MyProperties.getInstance().listaNegocios.get(position);
                        Log.d(Constants.appName, "position");
                        Toast.makeText(v.getContext(), " agregar articulo!! ", Toast.LENGTH_SHORT).show();
                        Articulo art = new Articulo();
                        Negocios negociosImage = (Negocios)MyProperties.getInstance().listaNegocios.get(position);
                        Log.d(Constants.appName, negociosImage.toString());
                        int idNegocio = negociosImage.getIdNegocio().intValue();
//                        art.setIdArticulo(idNegocio);
//                        art.setPrecio(123);
//                        art.setDescripcion("descripcion");
//                        art.setNombreArticulo("nombre");
                        art.setIdNegocio(idNegocio);
//                        art.setImageCode("imagen");
                        DaoArticulo daoArticulo = new DaoArticulo( getmContext(), Constants.LISTAR_ARTICULOS, position);
                        daoArticulo.getListaArticulo(art);
                    } catch (Exception e) {
                        Log.d("Error:NegociosAdapter: ", Utils.getStackTrace(e));
                    }
                    return true;
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            v.setOnCreateContextMenuListener(this);
            return false;
        }
    }

    public List<Negocios> getNegociosList() {
        return negociosList;
    }

    public void setNegociosList(List<Negocios> negociosList) {
        this.negociosList = negociosList;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
    public void callArticulo(String nada){

        Intent intent = new Intent(getmContext(), ListArticulosActivity.class);
        intent.putExtra("position", position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getmContext().startActivity(intent);

    }

}
