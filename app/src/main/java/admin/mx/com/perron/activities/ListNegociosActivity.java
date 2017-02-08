package admin.mx.com.perron.activities;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import java.util.List;
import admin.mx.com.perron.R;
import admin.mx.com.perron.adapter.NegociosAdapter;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by Jorge on 07/feb/2016.
 */
public class ListNegociosActivity extends AdministracionMain{
    public ListNegociosActivity() {}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.list_negocios);
            RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
            recList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);
            List<Negocios> listaNegocios = MyProperties.getInstance().listaNegocios;
            Toast.makeText(this, "registros: "+listaNegocios.size(), Toast.LENGTH_LONG).show();
            NegociosAdapter adapter = new NegociosAdapter(listaNegocios, getBaseContext());
            recList.setAdapter(adapter);
            //agregando Recycler View a MyProperties para actualizarlo
            MyProperties.getInstance().listNego = recList;
        }catch(Exception e){
            Log.d("Error:ListaNEgocios: ", Utils.getStackTrace(e));
        }
    }
}
