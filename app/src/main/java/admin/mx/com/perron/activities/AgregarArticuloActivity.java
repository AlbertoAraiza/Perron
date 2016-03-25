package admin.mx.com.perron.activities;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import admin.mx.com.perron.R;
import admin.mx.com.perron.adapter.ArticuloAdapter;
import admin.mx.com.perron.dao.DaoArticulo;
import admin.mx.com.perron.dao.DaoImages;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.entities.NegociosImage;
import admin.mx.com.perron.logic.ImageEncode;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/17/2016.
 */
public class AgregarArticuloActivity extends AdministracionMain implements View.OnClickListener{
    private ImageView imageArticulo;
    private TextView editPrecioArticulos;
    private TextView editNombreArticulos;
    private TextView editDescripcionArticulos;
    private List<Articulo> listaArticulo;
    private Button btnImagenArticulos;
    private Button btnGuardarArticulos;
    private int TAKE_PHOTO_CODE = 1;
    private LinearLayout layoutImage;
    private ArrayList<String> imagesList;
    private int idNegocio;
    public AgregarArticuloActivity() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_articulo_layout);
        initializeView();
        try {
            Bundle extras =  getIntent().getExtras();
            if(extras!=null) {
                initializeView();
                int action = (int) extras.get("action");
                idNegocio = ((Long) extras.get("action")).intValue();
                if (action== Constants.NEW_ITEM) {

                }else{
                    Articulo articulo = (Articulo) extras.get("articulo");
                    loadDataFromArticulo(articulo);
                }
            }
        }catch(Exception e){
            Log.d("Error:AgregarArticulo: ", Utils.getStackTrace(e));
        }
    }
    public void initializeView(){
        btnImagenArticulos = (Button)findViewById(R.id.btn_imagen_articulos);
        btnGuardarArticulos = (Button)findViewById(R.id.btn_guardar_articulos);
        btnImagenArticulos.setOnClickListener(this);
        btnGuardarArticulos.setOnClickListener(this);
        layoutImage = (LinearLayout)findViewById(R.id.add_imagelayout_articulos);
        editPrecioArticulos = (TextView) findViewById(R.id.edit_precio_articulos);
        editNombreArticulos = (TextView) findViewById(R.id.edit_nombre_articulos);
        editDescripcionArticulos = (TextView) findViewById(R.id.edit_descripcion_articulos);
        imagesList = null;

        /*imageNegocio = (ImageView) findViewById(R.id.image_negocio_articulos_activity);
        idNegocio = (TextView) findViewById(R.id.id_articulos_activity);
        nombreNegocio = (TextView) findViewById(R.id.nombre_negocio_articulos_activity);
        direccion = (TextView) findViewById(R.id.direccion_articulos_activity);
        coordenadas = (TextView) findViewById(R.id.coordenadas_articulos_activity);


        imageNegocio.setImageBitmap(negociosImage.getLogotipo());
        idNegocio.setText(negociosImage.getIdNegocio()+"");
        nombreNegocio.setText(negociosImage.getNombreNegocio());
        direccion.setText(negociosImage.getDireccion());
        coordenadas.setText(negociosImage.getCoordenadas());
*/
    }

    @Override
    public void onClick(View v) {
        if(v==btnImagenArticulos){
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        }else if(v==btnGuardarArticulos){
            if(getArticulo()) {
                Utils.showMessage(this, this, "Articulo guardado exitosamente: ");
            }else{
                Utils.showMessage(this, this, "Por favor tome una foto del articulo ");
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imagesList = new ArrayList<String>();
            //View child = getLayoutInflater().inflate(R.layout.child, null);
            ImageView  child = getNewImage(photo);
            imagesList.add(Utils.getEncodedString(photo));
            layoutImage.addView(child);
        }
    }
    public ImageView getNewImage(Bitmap picture){
        ImageView image = new ImageView(this);
        image.setLayoutParams(new android.view.ViewGroup.LayoutParams(808, 608));
        image.setMaxHeight(1200);
        image.setMaxWidth(1200);
        image.setImageBitmap(picture);
        return image;
    }
    public boolean getArticulo(){
        if(imagesList==null){
            return false;
        }else {
            int childCount = ((ViewGroup) layoutImage).getChildCount();
            Articulo articulo = new Articulo();
            articulo.setPrecio(Double.parseDouble(editPrecioArticulos.getText().toString()));
            articulo.setNombreArticulo(editNombreArticulos.getText().toString());
            articulo.setDescripcion(editDescripcionArticulos.getText().toString());
            articulo.setImageCode(imagesList.get(0));
            articulo.setIdNegocio(idNegocio);
            DaoArticulo daoArticulo = new DaoArticulo(getApplicationContext(), Constants.GUARDAR_ARTICULO, this);
            daoArticulo.saveArticulo(articulo);
            return true;
        }
    }
    public boolean loadDataFromArticulo(Articulo articulo){
        editPrecioArticulos.setText(String.valueOf(articulo.getPrecio()));
        editNombreArticulos.setText(articulo.getNombreArticulo());
        editDescripcionArticulos.setText(articulo.getDescripcion());
        agregarImages(layoutImage);
        return true;
    }
    public void agregarImages( LinearLayout layoutImage){
        List<Images> listaImagenes = DaoImages.getListaImages();
        Log.d("listaImagenes.size(): ", listaImagenes.size()+"");
        for(int i=0; i<listaImagenes.size(); i++){
            Images images = listaImagenes.get(i);
            ImageView  child = getNewImage(images.getImage());
            layoutImage.addView(child);
        }
    }
}
