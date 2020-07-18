package pmma.rushingturtles.websocket.messages.mainactivity.fromserver;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class HelloClientMsg extends BasicMsgFromServer {
    private String status;
    @JsonProperty("list_of_players_in_room")
    private List<String> listOfPlayersInTheRoom;

    public HelloClientMsg() {}

    public HelloClientMsg(String message, String status, List<String> listOfPlayersInTheRoom) {
        super(message);
        this.status = status;
        this.listOfPlayersInTheRoom = listOfPlayersInTheRoom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getListOfPlayersInTheRoom() {
        return listOfPlayersInTheRoom;
    }

    public void setListOfPlayersInTheRoom(List<String> listOfPlayersInTheRoom) {
        this.listOfPlayersInTheRoom = listOfPlayersInTheRoom;
    }
}
