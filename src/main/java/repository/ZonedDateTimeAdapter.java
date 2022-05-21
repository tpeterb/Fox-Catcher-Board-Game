package repository;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Class for converting {@code ZonedDateTime} objects from and to JSON format.
 */
public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {

    /**
     * Converts the given {@code ZonedDateTime} object to JSON format.
     */
    @Override
    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString());
        }
    }

    /**
     * Converts the given JSON to a {@code ZonedDateTime}
     * object and returns it.
     */
    @Override
    public ZonedDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return ZonedDateTime.parse(in.nextString());
    }

}
