package pmma.rushingturtles.websocket.messages;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMsg extends BasicMsgFromServer {

    @JsonProperty("details")
    private String description;
    @JsonProperty("offending_message")
    private String offendingMessage;

    public ErrorMsg() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOffendingMessage() {
        return offendingMessage;
    }

    public void setOffendingMessage(String offendingMessage) {
        this.offendingMessage = offendingMessage;
    }
}
