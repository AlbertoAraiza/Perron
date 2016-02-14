package admin.mx.com.perron.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import admin.mx.com.perron.R;
import admin.mx.com.perron.adapter.NegociosAdapter;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.Constants;

/**
 * Created by Jorge on 07/feb/2016.
 */
public class ListNegociosActivity extends AdministracionMain{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_negocios);
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        List<Negocios> listaNegocios = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listaNegocios = extras.getParcelableArrayList (Constants.LISTA_NEGOCIOS);
        }

        NegociosAdapter adapter = new NegociosAdapter(listaNegocios);
        recList.setAdapter(adapter);
    }
}
