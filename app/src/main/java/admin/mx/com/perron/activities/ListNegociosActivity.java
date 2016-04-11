package admin.mx.com.perron.activities;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.List;
import admin.mx.com.perron.R;
import admin.mx.com.perron.adapter.NegociosAdapter;
import admin.mx.com.perron.entities.NegociosImage;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by Jorge on 07/feb/2016.
 */
public class ListNegociosActivity extends AdministracionMain{


    public ListNegociosActivity() {

    }

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
            List<NegociosImage> listaNegocios = MyProperties.getInstance().listaNegocios;
            NegociosAdapter adapter = new NegociosAdapter(listaNegocios, getBaseContext());
            recList.setAdapter(adapter);
        }catch(Exception e){
            Log.d("Error:ListaNEgocios: ", Utils.getStackTrace(e));
        }
    }
}
