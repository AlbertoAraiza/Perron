package admin.mx.com.perron;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import admin.mx.com.perron.entities.Negocios;
import admin.mx.com.perron.utils.Constants;
import admin.mx.com.perron.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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

    private int option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        initialize();
        Bundle extras =  getIntent().getExtras();
        if(extras!=null){
            /*String negocio = intent.getStringExtra("negocio");
            Negocios negocios = Utils.createNegocios(negocio);*/
            option = extras.getInt("option");
            if(option== Constants.ACTUALIZAR){
                TextView textTitulo = (TextView) findViewById(R.id.titulo);
                textTitulo.setText("Modificar Negocio");
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
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        textIp = (TextView)findViewById(R.id.ip);
        textIp.setText("IP address: "+ip);
        btnLista = (Button)findViewById(R.id.btn_lista);
        btnLista.setOnClickListener(this);
    }
    public void setValues() {
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
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        textIp = (TextView)findViewById(R.id.ip);
        textIp.setText("IP address: "+ip);
        btnLista = (Button)findViewById(R.id.btn_lista);
        btnLista.setOnClickListener(this);
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
                        FtpCliente ftp = new FtpCliente(getApplicationContext(), this, thumbnail, getJsonObject2());
                        ftp.execute();
                    } else {
                        nuevoSnack("Please select an image first that all");
                    }
                }
            }else{
                nuevoSnack("Here it will be update all");
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

    public void chooseImage() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        //Toast.makeText(getApplicationContext(), "Choose image", Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                for (int i = 0; i < filePathColumn.length; i++) {

                    System.out.println("filePath: " + getRealPathFromURI3(selectedImage));
                }
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
                imgView.setImageBitmap(bitmap);


                BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
                thumbnail = drawable.getBitmap();


                nuevoSnack(imgDecodableString);

            } else {
                nuevoSnack("You haven't picked Image");
            }
        } catch (Exception e) {
            nuevoSnack("You haven't picked Image" + getStackTrace(e));
        }

    }




    public String getRealPathFromURI3(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public JSONObject getJsonObject2() {
        JSONObject json = new JSONObject();
        try {
            json.put("nombreNegocio", edtNombreNegocio.getText());
            json.put("direccion", edtDireccion.getText());
            json.put("coordenadas", edtCoordenadas.getText());
            String host = (String) getText(R.string.host);
            json.put("logotipo", host);
        } catch (JSONException e) {
            System.out.println("******************************************************************************ERROR ON JSONOBject: " + getStackTrace(e) + "******************************************************************************ERROR ON JSONOBject: ");
        }
        return json;
    }
}

