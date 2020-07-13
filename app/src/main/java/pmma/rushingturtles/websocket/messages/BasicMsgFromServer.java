package pmma.rushingturtles.websocket.messages;

public class BasicMsgFromServer {
    protected String message;

    public BasicMsgFromServer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
