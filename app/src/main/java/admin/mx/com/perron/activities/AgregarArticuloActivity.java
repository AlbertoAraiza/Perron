package admin.mx.com.perron.activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import admin.mx.com.perron.R;
import admin.mx.com.perron.dao.DaoArticulo;
import admin.mx.com.perron.dao.DaoDeleteImageImage;
import admin.mx.com.perron.dao.DaoImages;
import admin.mx.com.perron.dao.DaoSaveArticulo;
import admin.mx.com.perron.dao.DaoUpdateImageArticulo;
import admin.mx.com.perron.dao.DaoUpdateImageImage;
import admin.mx.com.perron.entities.Articulo;
import admin.mx.com.perron.entities.ArticuloTemp;
import admin.mx.com.perron.entities.Images;
import admin.mx.com.perron.logic.RealPathUtil;
import admin.mx.com.perron.utils.ASFUriHelper;
import admin.mx.com.perron.utils.BitmapScaler;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.CropSquareTransformation;
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
    private int RESULT_LOAD_IMG = 11;
    private int UPDATE_IMAGE = 123654;
    private int ACTUALIZAR_IMAGEN = 654;
    private int ELIMINAR_IMAGEN = 345;
    private LinearLayout layoutImage;
    private ArrayList<String> imagesList;
    private int idNegocio;
    private Articulo articulo = null;
    private int action;
    private Bitmap photo;
    AlertDialog actions;
    private int idImagen;
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
                action = (int) extras.get("action");
                if (action== Constants.UPDATE_ITEM) {
                    articulo = MyProperties.getInstance().articulo;
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
        initializeOptionPicture();
    }

    @Override
    public void onClick(View v) {
        if(v==btnImagenArticulos){
            actions.show();
        }else if(v==btnGuardarArticulos){
            if(saveArticulo()) {
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
        if(null != data) {
            if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK && null != data) {
                Log.d("CameraDemo", "Pic saved");
                photo = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                try {
                    Uri uriFromPath = uriFromPath(data);
                    photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                CropSquareTransformation ctf = new CropSquareTransformation();
                photo = ctf.transform(photo);
            }
            if (imagesList == null) {
                imagesList = new ArrayList<String>();
            }
            //View child = getLayoutInflater().inflate(R.layout.child, null);
            String imgString = Utils.getEncodedString(photo);
            imagesList.add(imgString);
            if (action == Constants.UPDATE_ITEM) {
                saveImage(articulo.getIdArticulo(), imgString);
            }else if(action==UPDATE_IMAGE){
                selectImageUpdateItem(imgString);
            }else if(action==ACTUALIZAR_IMAGEN ){//actualizar imagen de la tabla imagenes
                actualizarImagenImagen(imgString, idImagen );
            }
        }
    }
    public View getNewImage(Bitmap picture, final String id){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View inflatedLayout= inflater.inflate(R.layout.image_row_layout, null, false);
        ImageView image = (ImageView)inflatedLayout.findViewById(R.id.id_image_update_articulo);
        image.setImageBitmap(BitmapScaler.scaleToFitWidth(picture, 120));
        Button btnDeleteArticulos = (Button)inflatedLayout.findViewById(R.id.btn_delete_articulos);
        Button btnUpdateArticulos = (Button)inflatedLayout.findViewById(R.id.btn_update_articulos);
        btnDeleteArticulos.setVisibility(View.GONE);

        btnUpdateArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "idArticulo: " + id, Toast.LENGTH_SHORT).show();
                action = UPDATE_IMAGE;
                actions.show();
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
//            art.setImageCode(imagesList.get(0));
            art.setIdNegocio(idNegocio);
            DaoSaveArticulo daoArticulo = new DaoSaveArticulo(getApplicationContext(), this, art);
            daoArticulo.execute();
            return true;
        }
    }

    public boolean loadDataFromArticulo(Articulo articulo){
        Log.d(Constants.appName, articulo.toString());
        editPrecioArticulos.setText(String.valueOf(articulo.getPrecio()));
        editNombreArticulos.setText(articulo.getNombreArticulo());
        editDescripcionArticulos.setText(articulo.getDescripcion());
//        View child = getNewImage(articulo.getImageBitmap(), articulo.getIdArticulo()+"");
//        layoutImage.addView(child);
        getListaImages();
        return true;
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
//            art.setImageCode(" ");
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
                setIdImagen(idImagen);
                action = ACTUALIZAR_IMAGEN ;
                actions.show();
            }
        });
        btnDeleteArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "ELEMINAR idImagen principal, idArticulo: " + idImagen, Toast.LENGTH_SHORT).show();
                eliminarImagenImagen(idImagen);
            }
        });
        return inflatedLayout;
    }


    public void saveImage(int idArticulo, String imgString){
        Images images = new Images();
        images.setIdArticulo(idArticulo);
        images.setImagenString(imgString);
        DaoImages databaseObject = new DaoImages(this,this,  images, Constants.GUARDAR_IMAGEN);
        databaseObject.execute();
    }

    public Bitmap getPhoto() {
        return photo;
    }
    public void showNewImageAfterSave(Bitmap picture, final String id){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View inflatedLayout= inflater.inflate(R.layout.image_row_layout, null, false);
        ImageView image = (ImageView)inflatedLayout.findViewById(R.id.id_image_update_articulo);
        image.setImageBitmap(picture);
        Button btnDeleteArticulos = (Button)inflatedLayout.findViewById(R.id.btn_delete_articulos);
        Button btnUpdateArticulos = (Button)inflatedLayout.findViewById(R.id.btn_update_articulos);
        btnUpdateArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "AGREGAR idImagen principal, idArticulo: " + id, Toast.LENGTH_SHORT).show();
            }
        });
        layoutImage.addView(inflatedLayout);
        action= Constants.UPDATE_ITEM;
    }
    public void showNewImageAfterSaveImg( final String id){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View inflatedLayout= inflater.inflate(R.layout.image_row_layout, null, false);
        ImageView image = (ImageView)inflatedLayout.findViewById(R.id.id_image_update_articulo);
        image.setImageBitmap(getPhoto());
        Button btnDeleteArticulos = (Button)inflatedLayout.findViewById(R.id.btn_delete_articulos);
        Button btnUpdateArticulos = (Button)inflatedLayout.findViewById(R.id.btn_update_articulos);
        btnUpdateArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "AGREGAR idImagen principal, idArticulo: " + id, Toast.LENGTH_SHORT).show();
            }
        });
        btnDeleteArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "AGREGAR idImagen principal, idArticulo: " + id, Toast.LENGTH_SHORT).show();
            }
        });
        layoutImage.addView(inflatedLayout);
    }
    public void initializeOptionPicture(){
        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Delete
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                        break;
                    case 1: // Copy
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                        break;
                    default:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Option");
        String[] options = { "Tomar foto", "Seleccionar foto" };
        builder.setItems(options, actionListener);
        builder.setNegativeButton("Cancel", null);
        actions = builder.create();
    }
    public Uri uriFromPath (Intent data) {
        String realPath = "";
        if (Build.VERSION.SDK_INT < 11) {// SDK < API11
            realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
        } else if (Build.VERSION.SDK_INT < 19) {// SDK >= 11 && SDK < 19
            realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
        } else if (Build.VERSION.SDK_INT == 19){// SDK > 19 (Android 4.4)
            realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
        }else{// SDK > 19 (Android 4.4)
            realPath = ASFUriHelper.getPath(this, data.getData());
        }
        File imageFile = new File(realPath);
        Uri uriFromPath = Uri.fromFile(imageFile);
        return uriFromPath;
    }
    /**
     * seccion donde se encuentran los metodos que actualizan el articulo
     */

    public void showMessage(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void selectImageUpdateItem(String imgString){
        ArticuloTemp arti = new ArticuloTemp();
        arti.setImagenString(imgString);
        arti.setIdArticulo(articulo.getIdArticulo());
        byte[] CDRIVES = new byte[] { (byte)0xe0, 0x4f, (byte)0xd0, 0x20, (byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, 0x2b, 0x30, 0x30, (byte)0x9d };
        arti.setImagen(CDRIVES);
        arti.setIdNegocio(123);
        arti.setPrecio(123);
        arti.setNombreArticulo("nombre articulo");
        DaoUpdateImageArticulo daoArticulo = new DaoUpdateImageArticulo(getApplicationContext(),this, arti);
        daoArticulo.execute();
    }//end selectImageUpdateItem

    public void actualizarImagenImagen(String imgString, int idImagen){
        Images images = new Images();
        images.setIdImagen(idImagen);
        images.setImagenString(imgString);
        DaoUpdateImageImage daoUpdateImageImage = new DaoUpdateImageImage(this, this, images);
        daoUpdateImageImage.execute();
    }//END actualizarImagenImagen

    /**
     * Metodo creado para asignar valor a la imagen que pertenecen al articulo y que seran asignadas, actualizadas o eliminadas del articulo
     * @param idImagen
     */
    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    /**
     * Metodo para eleminar la imagen de la imagen de los articulos
     * @param idImagen
     */
    public void eliminarImagenImagen( int idImagen){
        Images images = new Images();
        images.setIdImagen(idImagen);
        images.setImagenString("imags");
        DaoDeleteImageImage dao = new DaoDeleteImageImage(this, this, images);
        dao.execute();
    }//END actualizarImagenImagen
}//llave principal de la clase
