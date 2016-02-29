package admin.mx.com.perron.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.MainActivity;
import admin.mx.com.perron.R;
import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.entities.NegociosImage;
import admin.mx.com.perron.logic.ListaResultado;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;

/**
 * Created by Jorge on 07/feb/2016.
 */
public class MenuMain extends AdministracionMain implements View.OnClickListener{

    EditText edtNombreNegocio;
    EditText edtDireccion;
    EditText edtCoordenadas;
    EditText edtLogotipo;
    TextView textResult;
    TextView textResult2;
    TextView textIp;
    EditText edtResponse;
    FloatingActionButton fab;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Button btnLista;
    Button btnChooseImage;
    Bitmap thumbnail = null;
    Snackbar snackbar;
    View.OnClickListener mOnClickListener;
    ImageView imgView;

    CardView cvOpcion1;
    CardView cvOpcion2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        initialize();
    }

    public void initialize() {
        cvOpcion1 = (CardView)findViewById(R.id.cv) ;
        cvOpcion2 = (CardView)findViewById(R.id.cv2) ;
        cvOpcion1.setOnClickListener(this);
        cvOpcion2.setOnClickListener(this);
    }


    public void nuevoSnack(String msg) {
        final String msg2 = msg;
        snackbar = Snackbar
                .make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
                .setAction("Undo", mOnClickListener);
        snackbar.setActionTextColor(Color.BLACK);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.MAGENTA);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
    }


    @Override
    public void onClick(View v) {
        if(cvOpcion1 ==v){
            Log.d(Constants.appName, "Opcion 1");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("option", Constants.CREAR);
            startActivity(intent);
        }else if (cvOpcion2 == v){
            Log.d(Constants.appName, "Opcion 2");
            ListaResultado listaResultado = new ListaResultado(getApplicationContext(), this);
            listaResultado.execute();
        }
    }
    //public void loadListNegocios(){
    public void loadListNegocios(List<NegociosImage> listaNegocios){
        try {
            Log.d(Constants.appName, "return from webservice");
            Intent intent = new Intent(this, ListNegociosActivity.class);
            //intent.putParcelableArrayListExtra(Constants.LISTA_NEGOCIOS, (ArrayList<? extends Parcelable>) listaNegocios);
            MyProperties.getInstance().listaNegocios = listaNegocios;
            Log.d(Constants.appName, "Calling ListNegociosActivity");
            startActivity(intent);
        }catch(Exception e ){
            Log.d("Error-loadListNegocios:", Utils.getStackTrace(e));
        }
    }
}
