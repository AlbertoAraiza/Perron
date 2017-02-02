package admin.mx.com.perron.entities;

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
}
