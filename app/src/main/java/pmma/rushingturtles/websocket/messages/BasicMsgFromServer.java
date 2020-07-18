package pmma.rushingturtles.websocket.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import pmma.rushingturtles.websocket.messages.gameactivity.broadcast.GameStateUpdatedMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.broadcast.GameWonMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.CardsUpdatedMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.FullGameStateMsg;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "message")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value= FullGameStateMsg.class, name="full game state"),
//        @JsonSubTypes.Type(value= CardsUpdatedMsg.class, name="player cards updated"),
//        @JsonSubTypes.Type(value= GameStateUpdatedMsg.class, name="game state updated"),
//        @JsonSubTypes.Type(value= GameWonMsg.class, name="game won"),
//        @JsonSubTypes.Type(value= ErrorMsg.class, name="error")
//})
public abstract class BasicMsgFromServer {
    protected String message;

    public BasicMsgFromServer() {}

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
