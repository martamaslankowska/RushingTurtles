package pmma.rushingturtles.websocket.messages;

import android.util.Log;

public class ErrorMsg extends BasicMsgFromServer {

    private String description;

    public ErrorMsg() {
    }

    public ErrorMsg(String message, String description) {
        super(message);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
