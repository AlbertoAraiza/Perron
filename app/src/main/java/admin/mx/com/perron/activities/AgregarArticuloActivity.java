package admin.mx.com.perron.activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import admin.mx.com.perron.R;
import admin.mx.com.perron.dao.DaoArticulo;
import admin.mx.com.perron.dao.DatabaseObject;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.otto.Article;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;
/**
 * Created by jorge on 3/17/2016.
 */
public class AgregarArticuloActivity extends AdministracionMain implements View.OnClickListener{
    private TextView editPrecioArticulos;
    private TextView editNombreArticulos;
    private TextView editDescripcionArticulos;
    private Button btnImagenArticulos;
    private Button btnGuardarArticulos;
    private int TAKE_PHOTO_CODE = 1;
    private LinearLayout layoutImage;
    private ArrayList<String> imagesList;
    private int idNegocio;
    private Articulo articulo = null;
    private int action;
    private Bitmap photo;
    public AgregarArticuloActivity() {
        Utils.getBus().register(this);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        super.onDestroy();
        Utils.getBus().unregister(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_articulo_layout);
        initializeView();
        try {
            Bundle extras =  getIntent().getExtras();
            if(extras!=null) {
                action = (int) extras.get("action");
                if (action== Constants.UPDATE_ITEM) {
                    articulo = (Articulo) extras.get("articulo");
                    Log.d(Constants.appName, "articulo: "+articulo);
                    btnImagenArticulos.setVisibility(View.VISIBLE);
                    loadDataFromArticulo(articulo);
                }else if (action== Constants.NEW_ITEM){
                    idNegocio = ((Long) extras.get("idNegocio")).intValue();;
                }
            }
        }catch(Exception e){
            Log.d(Constants.appName, Utils.getStackTrace(e));
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
    }

    @Override
    public void onClick(View v) {
        if(v==btnImagenArticulos){
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        }else if(v==btnGuardarArticulos){
            if(saveArticulo()) {
                Utils.showMessage(this, this, "Articulo guardado exitosamente: ");
                btnImagenArticulos.setVisibility(View.GONE);
            }else{
                if(action == Constants.NEW_ITEM){
                    Utils.showMessage(this, this, "Por favor tome una foto del articulo ");
                }else{//Codigo para actualizar un articulo ya existente
                    updateArticulo();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            photo = (Bitmap) data.getExtras().get("data");
            if(imagesList == null ){
                imagesList = new ArrayList<String>();
            }
            //View child = getLayoutInflater().inflate(R.layout.child, null);
            String imgString = Utils.getEncodedString(photo);
            imagesList.add(imgString);
            if(action== Constants.UPDATE_ITEM){
                saveImage(articulo.getIdArticulo(), imgString);
            }
        }
    }
    public View getNewImage(Bitmap picture){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View inflatedLayout= inflater.inflate(R.layout.image_row_layout, null, false);
        ImageView image = (ImageView)inflatedLayout.findViewById(R.id.id_image_update_articulo);
        image.setImageBitmap(picture);
        Button btnDeleteArticulos = (Button)inflatedLayout.findViewById(R.id.btn_delete_articulos);
        Button btnUpdateArticulos = (Button)inflatedLayout.findViewById(R.id.btn_update_articulos);
        btnDeleteArticulos.setVisibility(View.GONE);

        btnUpdateArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "AGREGAR idImagen principal, idArticulo: " + articulo.getIdArticulo(), Toast.LENGTH_SHORT).show();
            }
        });
        return inflatedLayout;
    }
    public boolean saveArticulo(){
        if(imagesList==null || action== Constants.UPDATE_ITEM){
            return false;
        }else {
            Articulo art = new Articulo();
            art.setPrecio(Double.parseDouble(editPrecioArticulos.getText().toString()));
            art.setNombreArticulo(editNombreArticulos.getText().toString());
            art.setDescripcion(editDescripcionArticulos.getText().toString());
            art.setImageCode(imagesList.get(0));
            art.setIdNegocio(idNegocio);
            DaoArticulo daoArticulo = new DaoArticulo(getApplicationContext(), Constants.GUARDAR_ARTICULO);
            daoArticulo.saveArticulo(art);
            return true;
        }
    }

    public boolean loadDataFromArticulo(Articulo articulo){
        Log.d(Constants.appName, articulo.toString());
        editPrecioArticulos.setText(String.valueOf(articulo.getPrecio()));
        editNombreArticulos.setText(articulo.getNombreArticulo());
        editDescripcionArticulos.setText(articulo.getDescripcion());
        View child = getNewImage(articulo.getImageBitmap());
        layoutImage.addView(child);
        getListaImages();
        return true;
    }

    public void saveImages(int idArticulo){
        int childCount = ((ViewGroup)layoutImage).getChildCount();
        Log.d("Imagenes a guardar: ", childCount + "");
        for(int i=1; i<childCount; i++){
            Images images = new Images();
            View  child = ((ViewGroup)layoutImage).getChildAt(i);
            ImageView imageView = (ImageView) child;
            images.setIdArticulo(idArticulo);
            images.setImagenString(Utils.getEncodedString(Utils.getBitmapFromImageView(imageView)));
            DatabaseObject databaseObject = new DatabaseObject(this, images, Constants.GUARDAR_IMAGEN);
            databaseObject.execute();
        }
    }
    public void getListaImages() {
        List<Images> listImages = MyProperties.getInstance().listaImagenes;
        for(int i=0;i<listImages.size();i++){
            Images images = (Images)listImages.get(i);
            images.setImageBitmap(images.getImageBitmap());
            Log.d(Constants.appName, images.toString());
            layoutImage.addView(getButtonRow(images.getImageBitmap(), images.getIdImagen()));
        }
    }
    /**
     * seccion donde se encuentran los metodos que actualizan el articulo
     */
    public boolean updateArticulo(){
        Articulo art = actualizarArticulo();
        if(art==null) {
            return false;
        }else{
            DaoArticulo daoArticulo = new DaoArticulo(getApplicationContext(), Constants.ACTUALIZAR_ARTICULO);
            daoArticulo.actualizarArticulo(art);
            return true;
        }
    }
    public Articulo actualizarArticulo(){
        Articulo art = null;
        try{
            art = new Articulo();
            art.setPrecio(Double.parseDouble(editPrecioArticulos.getText().toString()));
            art.setNombreArticulo(editNombreArticulos.getText().toString());
            art.setDescripcion(editDescripcionArticulos.getText().toString());
            //art.setImageCode(Utils.getEncodedString(Utils.getBitmapFromImageView(imageView)));
            art.setImageCode(" ");
            art.setIdNegocio(articulo.getIdNegocio());
            art.setIdArticulo(articulo.getIdArticulo());
            return art;
        }catch(Exception e){
            Log.d(Constants.appName, Utils.getStackTrace(e));
            art = null;
        }
        return art;
    }

    public View getButtonRow(Bitmap picture, final int idImagen){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View inflatedLayout= inflater.inflate(R.layout.image_row_layout, null, false);
        ImageView image = (ImageView)inflatedLayout.findViewById(R.id.id_image_update_articulo);
        image.setImageBitmap(picture);
        Button btnDeleteArticulos = (Button)inflatedLayout.findViewById(R.id.btn_delete_articulos);
        Button btnUpdateArticulos = (Button)inflatedLayout.findViewById(R.id.btn_update_articulos);

        btnUpdateArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "AGREGAR idImagen principal, idArticulo: " + idImagen, Toast.LENGTH_SHORT).show();
            }
        });
        btnDeleteArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "ELEMINAR idImagen principal, idArticulo: " + idImagen, Toast.LENGTH_SHORT).show();
            }
        } );
        return inflatedLayout;
    }
    public void saveImage(int idArticulo, String imgString){
        Images images = new Images();
        images.setIdArticulo(idArticulo);
        images.setImagenString(imgString);
        DatabaseObject databaseObject = new DatabaseObject(this, images, Constants.GUARDAR_IMAGEN);
        databaseObject.execute();
    }
    @Subscribe
    public void returnFromSaveArticulo(Article art){
        this.finish();
    }
}//llave principal de la clase
