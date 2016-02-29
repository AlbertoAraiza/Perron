package admin.mx.com.perron.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.R;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.entities.NegociosImage;
import admin.mx.com.perron.utils.Constants;

/**
 * Created by Jorge on 07/feb/2016.
 */
public class NegociosAdapter extends RecyclerView.Adapter<NegociosAdapter.NegocioViewHolder>{
    private List<NegociosImage> negociosList;
    private Context mContext;

    public NegociosAdapter(List<NegociosImage> negociosList, Context mContext) {
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
        NegociosImage negocio = negociosList.get(position);
        holder.vLogotipo.setImageBitmap(negocio.getLogotipo());
        //Picasso.with(mContext).load("https://puntodeventa-aguascalientes.rhcloud.com/static/2121.jpg").into(holder.vLogotipo);
        holder.vNombreNegocio.setText(negocio.getNombreNegocio());
        holder.vDireccion.setText(negocio.getDireccion());
        holder.vCoordenadas.setText(negocio.getCoordenadas());
    }

    @Override
    public int getItemCount() {
        return negociosList.size();
    }

    public static class NegocioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView vLogotipo;
        protected TextView vNombreNegocio;
        protected TextView vDireccion;
        protected TextView vCoordenadas;


        public NegocioViewHolder(View v) {
            super(v);
            vLogotipo =  (ImageView) v.findViewById(R.id.logotipo);
            vNombreNegocio = (TextView)  v.findViewById(R.id.nombre_negocio);
            vDireccion = (TextView)  v.findViewById(R.id.direccion);
            vCoordenadas = (TextView) v.findViewById(R.id.coordenadas);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(Constants.appName, "Opcion 2");
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.putExtra("option", Constants.ACTUALIZAR);
            int position = getLayoutPosition();

            //intent.putExtra("option", getNegociosList().get(getAdapterPosition()));
            v.getContext().startActivity(intent);
        }
    }

    public List<NegociosImage> getNegociosList() {
        return negociosList;
    }

    public void setNegociosList(List<NegociosImage> negociosList) {
        this.negociosList = negociosList;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
