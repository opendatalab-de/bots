package de.grundid.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class JsonUtils {

    public static void writeData(String fileName, Object data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            objectWriter.writeValue(new FileOutputStream(fileName), data);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
