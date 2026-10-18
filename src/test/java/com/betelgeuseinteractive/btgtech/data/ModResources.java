package com.betelgeuseinteractive.btgtech.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Reads the mod's resources from the test classpath, where the generated resources and the hand-made ones meet, as
 * they do in the mod jar.
 */
final class ModResources {
    private ModResources() {}

    /** Returns whether the resource exists, for example {@code assets/btgtech/models/block/tin_ore.json}. */
    static boolean exists(String path) {
        return ModResources.class.getResource("/" + path) != null;
    }

    /**
     * Parses a JSON resource.
     *
     * @throws AssertionError if the resource does not exist
     */
    static JsonElement json(String path) {
        try (InputStream stream = ModResources.class.getResourceAsStream("/" + path)) {
            if (stream == null) {
                throw new AssertionError("Missing resource " + path);
            }
            return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Returns the names of the JSON files in a resource directory without their extension, or none if it is missing. */
    static List<String> jsonNames(String directory) {
        URL url = ModResources.class.getResource("/" + directory);
        if (url == null) {
            return List.of();
        }
        try (Stream<Path> files = Files.list(Path.of(url.toURI()))) {
            return files.map(file -> file.getFileName().toString())
                    .filter(name -> name.endsWith(".json"))
                    .map(name -> name.substring(0, name.length() - ".json".length()))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}
