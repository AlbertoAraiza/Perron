package admin.mx.com.perron.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import admin.mx.com.perron.R;
import admin.mx.com.perron.adapter.ArticuloAdapter;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/13/2016.
 */
public class ListArticulosActivity extends AdministracionMain implements View.OnClickListener{
    Negocios negociosImage = MyProperties.getInstance().negocio;
    ImageView imageNegocio;
    TextView idNegocio;
    TextView nombreNegocio;
    TextView direccion;
    TextView coordenadas;
    private List<Articulo> listaArticulo;
    private RecyclerView recList;
    private ArticuloAdapter adapter;
    private Button btnAddItem;
    public ListArticulosActivity() {
        super();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.articulos_activity);
//            List<Negocios> listaNegocios = MyProperties.getInstance().listaNegocios;
//            Bundle extras =  getIntent().getExtras();
//            if(extras!=null) {
//                int position = extras.getInt("position");
//                negociosImage = listaNegocios.get(position);
//                initializeView();
//            }
            initializeView();
            recList = (RecyclerView) findViewById(R.id.articulos_recycler_view);
            recList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);
            listaArticulo =  MyProperties.getInstance().listaArticulos;
            adapter = new ArticuloAdapter(listaArticulo, getApplicationContext());
            recList.setAdapter(adapter);
            MyProperties.getInstance().listItems = recList;
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
        btnAddItem = (Button)findViewById(R.id.btn_add_item);

        Picasso.with(imageNegocio.getContext())
                .load(Constants.URL_HOST_IMAGES+negociosImage.getLogotipo())
                .error(R.drawable.not_available)
                .into(imageNegocio);
        idNegocio.setText("ID Negocio: " + negociosImage.getIdNegocio());
        nombreNegocio.setText("Nombre Negocio: " + negociosImage.getNombreNegocio());
        direccion.setText("Direccion: " + negociosImage.getDireccion());
        coordenadas.setText("Coordenadas: " + negociosImage.getCoordenadas());
        btnAddItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnAddItem){
            Intent intent = new Intent(this, AgregarArticuloActivity.class);
            intent.putExtra("action",Constants.NEW_ITEM);
            intent.putExtra("idNegocio", negociosImage.getIdNegocio());
            startActivity(intent);
        }
    }
}
