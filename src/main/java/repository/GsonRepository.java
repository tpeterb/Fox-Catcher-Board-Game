package repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Represents a repository of objects of the same type.
 * The elements can also be loaded from a file and stored in the repository.
 * Furthermore, the elements in the repository can be saved to a file
 * in JSON format.
 * @param <T> The type of objects the repository holds.
 */
public class GsonRepository<T> extends Repository<T> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();

    /**
     * Creates a {@code GsonGepository} object whose elements
     * are of type {@code elementType}.
     * @param elementType The type of elements the repository contains.
     */
    public GsonRepository(Class<T> elementType) {
        super(elementType);
    }

    /**
     * Loads the elements from a JSON file and stores them
     * in the repository.
     * @param file The file from which the elements should be loaded.
     * @throws IOException if the {@code file} does not exist, the
     * {@code file} cannot be loaded, or the {@code file} is a directory
     * rather than a regular file.
     */
    public void loadFromFile(File file) throws IOException {
        try (var reader = new FileReader(file)) {
            var listType = TypeToken.getParameterized(List.class, elementType).getType();
            elements = GSON.fromJson(reader, listType);
        }
    }

    /**
     * Saves the elements in the repository to the {@code} specified
     * in JSON format.
     * @param file The file to which the elements in the repository
     * should be saved in JSON format.
     * @throws IOException If the {@code file} exists but is a directory
     * instead of a regular file, or does not exist, or the {@code file}
     * cannot be opened for some reason.
     */
    public void saveToFile(File file) throws IOException {
        try (var writer = new FileWriter(file)) {
            GSON.toJson(elements, writer);
        }
    }

}
