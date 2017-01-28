package admin.mx.com.perron;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.logic.RealPathUtil;
import admin.mx.com.perron.utils.ASFUriHelper;
import admin.mx.com.perron.utils.AbsRuntimePermission;
import admin.mx.com.perron.utils.CheckPermission;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.CropSquareTransformation;
import admin.mx.com.perron.utils.GPSTracker;
import admin.mx.com.perron.utils.MyProperties;
import admin.mx.com.perron.utils.Utils;

public class MainActivity extends AbsRuntimePermission implements View.OnClickListener {
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
    Button btnLista;
    Button btnChooseImage;
    Bitmap thumbnail = null;
    Snackbar snackbar;
    View.OnClickListener mOnClickListener;
    ImageView imgView;
    String realPath;
    private int option = 999;
    int position;
    private Negocios negociosImage;
    private GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            int op = PackageManager.PERMISSION_GRANTED;
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != op ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != op ||
                    checkSelfPermission(Manifest.permission.CAMERA) != op)
                requestAppPermissions();
            else
                onCreate2();
//            if(CheckPermission.checkPermissionForGPS(this)){
//                onCreate2();
//            } else {
//                CheckPermission.requestPermissionForGPS(this);
//            }
        } else {
            onCreate2();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    public void onCreate2(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        initialize();
        Bundle extras =  getIntent().getExtras();
        if(extras!=null){
            option = extras.getInt("option");
            if(option== Constants.ACTUALIZAR){
                TextView textTitulo = (TextView) findViewById(R.id.titulo);
                textTitulo.setText("Modificar Negocio");
                this.position = extras.getInt("position");
                List<Negocios> listaNegocios = MyProperties.getInstance().listaNegocios;
                negociosImage = listaNegocios.get(position);
                setValues(negociosImage);
            }else if(option== Constants.CREAR){
                gps = new GPSTracker(getApplicationContext(), this);
                if(gps.getLocation()){
                    edtCoordenadas.setText(gps.getLatitude()+","+gps.getLongitude());
                }else{
                    edtCoordenadas.setText("");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialize() {
        edtNombreNegocio = (EditText) findViewById(R.id.edtNombreNegocio);
        edtDireccion = (EditText) findViewById(R.id.edtDireccion);
        edtCoordenadas = (EditText) findViewById(R.id.edtCoordenadas);
        edtLogotipo = (EditText) findViewById(R.id.edtLogotipo);
        edtResponse = (EditText) findViewById(R.id.edtResponse);
        textResult = (TextView) findViewById(R.id.textResult);
        textResult2 = (TextView) findViewById(R.id.ip);
        btnChooseImage = (Button) findViewById(R.id.btn_choose_image);
        btnChooseImage.setOnClickListener(this);
        imgView = (ImageView) findViewById(R.id.image_ftp);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        textIp = (TextView)findViewById(R.id.ip);
        textIp.setText("IP address new job: "+ip);
        btnLista = (Button)findViewById(R.id.btn_lista);
        btnLista.setOnClickListener(this);
    }
    public void setValues(Negocios negociosImage) {
        //imgView.setImageBitmap(negociosImage.getLogotipo());
        edtNombreNegocio.setText(negociosImage.getNombreNegocio());
        edtDireccion.setText(negociosImage.getDireccion());
        edtCoordenadas.setText(negociosImage.getCoordenadas());
        //thumbnail = negociosImage.getLogotipo();
        Picasso.with(getApplicationContext())
                .load(Constants.URL_BASE+negociosImage.getLogotipo())
                .error(R.drawable.not_available)
                .into(imgView);
    }

    public boolean validar(View view) {
        boolean res = false;
        if (edtNombreNegocio.getText().toString().equals("")) {
            messageUser(view, "Complete el campo de nombre del negocio");
        } else if (edtDireccion.getText().toString().equals("")) {
            messageUser(view, "Complete la direccion del negocio");
        } else if (edtCoordenadas.getText().toString().equals("")) {
            messageUser(view, "Complete las coordenadas del negocio");
        } else if (edtLogotipo.getText().toString().equals("")) {
            messageUser(view, "Complete el logotipo del negocio");
        } else {
            res = true;
        }
        return res;
    }

    public void messageUser(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public Negocios createNegocio() {
        Negocios res = new Negocios();
        res.setNombreNegocio(edtNombreNegocio.getText().toString());
        res.setCoordenadas(edtCoordenadas.getText().toString());
        res.setDireccion(edtDireccion.getText().toString());
        res.setLogotipo(edtLogotipo.getText().toString());
        Log.d("Create: ", res.toString());
        return res;
    }

    public Negocios createNegocioUpdate() {
        Negocios res = new Negocios();
        res.setNombreNegocio(edtNombreNegocio.getText().toString());
        res.setCoordenadas(edtCoordenadas.getText().toString());
        res.setDireccion(edtDireccion.getText().toString());
//        if (thumbnail != null)
        res.setLogotipo(negociosImage.getLogotipo());
        res.setIdNegocio(negociosImage.getIdNegocio());
        Log.d("Update: ", res.toString());
        return res;
    }
    public void mostrarMesaje(String message) {
        textResult.setText(message.toString());
        edtResponse.setText(message.toString());
    }



    public void mostrarMensaje(String message) {
        textResult2.setText(message.toString());
        edtResponse.setText(message);
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }


    @Override
    public void onClick(View v) {
        if (v == fab) {
            if(option==Constants.CREAR) {
                if (validar(v)) {
                    Negocios nego = createNegocio();
                    String message = Utils.convertGson(nego);
                    //executeRequestJson2(message);
                    if (thumbnail != null) {
                        FtpCliente ftp = new FtpCliente(getApplicationContext(), this, thumbnail, getJsonObject2(), option, position);
                        ftp.execute();
//                        finish();
                    } else {
                        nuevoSnack("Please select an image first that all");
                    }
                }
            }else if(option==Constants.ACTUALIZAR){
                if (validar(v)) {
                    Negocios nego = createNegocioUpdate();
                    String message = Utils.convertGson(nego);
                    //executeRequestJson2(message);
//                    if (thumbnail == null)
                    FtpCliente ftp = new FtpCliente(getApplicationContext(), this, thumbnail, getJsonObject2(), option, position);
                    ftp.execute();

//                    finish();
//                    } else {
//                        nuevoSnack("Please select an image first that all");
//                    }
                }

            }
        }else if (btnChooseImage == v) {
            chooseImage();
        }
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
                mostrarMensaje(msg2);
            }
        };
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void chooseImage() {
        // Create intent to Open Image applications like Gallery, Google Photos
        //Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        //Toast.makeText(getApplicationContext(), "Choose image", Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                if (Build.VERSION.SDK_INT < 11) {// SDK < API11
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                } else if (Build.VERSION.SDK_INT < 19) {// SDK >= 11 && SDK < 19
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                } else if (Build.VERSION.SDK_INT == 19){// SDK > 19 (Android 4.4)
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                } else {
                    realPath = ASFUriHelper.getPath(this, data.getData());
                }
                File imageFile = new File(realPath);
                Uri uriFromPath = Uri.fromFile(imageFile);
                try {
                    thumbnail = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                CropSquareTransformation ctf = new CropSquareTransformation();
                thumbnail = ctf.transform(thumbnail);
                imgView.setImageBitmap(thumbnail);
                nuevoSnack(realPath);

            } else {
                nuevoSnack("You haven't picked Image");
            }
        } catch (Exception e) {
            nuevoSnack("You haven't picked Image" + getStackTrace(e));
            e.printStackTrace();
        }
    }

    public JSONObject getJsonObject2() {
        JSONObject json = new JSONObject();
        try {
            json.put("nombreNegocio", edtNombreNegocio.getText());
            json.put("direccion", edtDireccion.getText());
            json.put("coordenadas", edtCoordenadas.getText());
            if(option == Constants.ACTUALIZAR){
                json.put("idNegocio", negociosImage.getIdNegocio());
                Toast.makeText(this, String.valueOf(negociosImage.getIdNegocio()), Toast.LENGTH_LONG).show();
            }
            if(thumbnail!=null) {
                String imagen = Utils.getEncodedString(thumbnail);
                json.put("logotipo", imagen);
            }

        } catch (JSONException e) {
            System.out.println("******************************************************************************ERROR ON JSONOBject: " + getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        System.out.println("JSObject: "+json.toString());
        return json;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.READ_GPS_PERMISSIONS_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    onCreate2();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

