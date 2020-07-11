package pmma.rushingturtles.websocket;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;

public class JsonObjectMapper {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static <T> String getJsonFromObject(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            Log.i("JsonObjectMapper", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    public static <T> T getObjectFromJson(String message, Class<T> objectClass) {
        try {
            return mapper.readValue(message, objectClass);
        } catch (IOException e) {
            Log.i("JsonObjectMapper", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }
}
