package pmma.rushingturtles.websocket;

import org.junit.Test;

import pmma.rushingturtles.websocket.WSC;

import static org.junit.Assert.*;

public class WSCTest {

    @Test
    public void getMessageFromMsgTest() {
        String msg = "{\"message\": \"Halo elo morelo\", \"description\": \"Halo OPIS\"}";
        String message = WSC.getInstance().getMessageFromMsg(msg);
        assertEquals("Halo elo morelo", message);
    }

}