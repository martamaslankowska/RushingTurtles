package pmma.rushingturtles.websocket.messages.mainactivity.broadcast;

import java.util.List;

import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class RoomUpdateMsg extends BasicMsgFromServer {

    private List<String> listOfPlayersInTheRoom;

    public RoomUpdateMsg(String message, List<String> listOfPlayersInTheRoom) {
        super(message);
        this.listOfPlayersInTheRoom = listOfPlayersInTheRoom;
    }

    public List<String> getListOfPlayersInTheRoom() {
        return listOfPlayersInTheRoom;
    }

    public void setListOfPlayersInTheRoom(List<String> listOfPlayersInTheRoom) {
        this.listOfPlayersInTheRoom = listOfPlayersInTheRoom;
    }
}
