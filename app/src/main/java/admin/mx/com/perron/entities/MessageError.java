package admin.mx.com.perron.entities;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jorge on 2/2/2017.
 */
public class MessageError {

    private String message;
    private boolean result;

    public MessageError(String message, boolean result) {
        this.message = message;
        this.result = result;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void show (Context ctx){
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }
}
