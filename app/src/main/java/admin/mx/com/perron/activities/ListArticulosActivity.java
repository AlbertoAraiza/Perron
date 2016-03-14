package admin.mx.com.perron.activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.R;
import admin.mx.com.perron.adapter.NegociosAdapter;
import admin.mx.com.perron.entities.NegociosImage;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/13/2016.
 */
public class ListArticulosActivity extends AdministracionMain{
    NegociosImage negociosImage;
    ImageView imageNegocio;
    TextView idNegocio;
    TextView nombreNegocio;
    TextView direccion;
    TextView coordenadas;
    public ListArticulosActivity() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.articulos_activity);
            List<NegociosImage> listaNegocios = MyProperties.getInstance().listaNegocios;

            Bundle extras =  getIntent().getExtras();
            if(extras!=null) {
                int position = extras.getInt("position");
                negociosImage = listaNegocios.get(position);
                initializeView();
            }
            /*
            RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);

            recList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);
            List<NegociosImage> listaNegocios = MyProperties.getInstance().listaNegocios;
            NegociosAdapter adapter = new NegociosAdapter(listaNegocios, getBaseContext());
            recList.setAdapter(adapter);
            */
        }catch(Exception e){
            Log.d("Error:ListaNEgocios: ", Utils.getStackTrace(e));
        }
    }
    public void initializeView(){
        imageNegocio = (ImageView) findViewById(R.id.image_negocio_articulos_activity);
        idNegocio = (TextView) findViewById(R.id.id_articulos_activity);
        nombreNegocio = (TextView) findViewById(R.id.nombre_negocio_articulos_activity);
        direccion = (TextView) findViewById(R.id.direccion_articulos_activity);
        coordenadas = (TextView) findViewById(R.id.coordenadas_articulos_activity);

        imageNegocio.setImageBitmap(negociosImage.getLogotipo());
        idNegocio.setText(negociosImage.getIdNegocio()+"");
        nombreNegocio.setText(negociosImage.getNombreNegocio());
        direccion.setText(negociosImage.getDireccion());
        coordenadas.setText(negociosImage.getCoordenadas());

    }
}
