package pmma.rushingturtles.websocket.messages;

public class ErrorMsg extends BasicMsgFromServer {

    private String description;

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
