package com.betelgeuseinteractive.btgtech.data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Every key of the {@code en_us} language file must exist in each other language file (ADR 0051).
 *
 * <p>Language files are read from the classpath, where the generated {@code en_us} file and the hand-made translations
 * meet. A missing language file counts as missing all keys; without an {@code en_us} file there are no keys to check.
 */
class TranslationCompletenessTest {
    private static final String LANGUAGE_DIRECTORY = "/assets/btgtech/lang/";

    @ParameterizedTest
    @ValueSource(strings = {"tr_tr", "zh_cn", "ru_ru", "pt_br", "es_es", "de_de", "fr_fr"})
    void languageContainsEveryEnglishKey(String language) throws IOException {
        Set<String> missingKeys = readKeys("en_us");
        missingKeys.removeAll(readKeys(language));

        assertTrue(missingKeys.isEmpty(), () -> language + ".json is missing keys: " + missingKeys);
    }

    private static Set<String> readKeys(String language) throws IOException {
        Set<String> keys = new TreeSet<>();
        try (InputStream stream =
                TranslationCompletenessTest.class.getResourceAsStream(LANGUAGE_DIRECTORY + language + ".json")) {
            if (stream != null) {
                Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                keys.addAll(JsonParser.parseReader(reader).getAsJsonObject().keySet());
            }
        }
        return keys;
    }
}
