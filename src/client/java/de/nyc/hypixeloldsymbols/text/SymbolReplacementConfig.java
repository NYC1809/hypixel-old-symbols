package de.nyc.hypixeloldsymbols.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import de.nyc.hypixeloldsymbols.Hypixel_old_symbolsClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class SymbolReplacementConfig {
    private static final Gson GSON = new GsonBuilder()
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir()
        .resolve(Hypixel_old_symbolsClient.MOD_ID + ".json");

    private boolean enabled = true;
    private LinkedHashMap<String, String> replacements = defaultReplacements();

    private static LinkedHashMap<String, String> defaultReplacements() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("\uE010", "\u2764");
        map.put("\uE008", "\u2748");
        map.put("\uE027", "\u2742");
        map.put("\uE00D", "\u2741");
        map.put("\uE02C", "\u2623");
        map.put("\uE007", "\u2620");
        map.put("\uE001", "\u2694");
        map.put("\uE00B", "\u2AFD");
        map.put("\uE024", "\u24C8");
        map.put("\uE003", "\u270E");
        map.put("\uE002", "\u0E51");
        map.put("\uE011", "\u2763");
        map.put("\uE028", "\u2668");
        map.put("\uE014", "\u2604");

        map.put("\uE005", "\u24C5");
        map.put("\uE015", "\u2E15");
        map.put("\uE016", "\u259A");
        map.put("\uE00F", "\u259A");
        map.put("\uE01C", "\u2727");
        //map.put("\uE053", "\u2618");

        map.put("\uE019", "\u0D60");
        map.put("\uE02B", "\uE02B");
        //map.put("\uE051", "\u2618");

        map.put("\uE023", "\u222E");
        //map.put("\uE054", "\u2618");

        map.put("\uE00C", "\u2602");
        map.put("\uE021", "\u03B1");
        map.put("\uE009", "\u2693");
        map.put("\uE02A", "\u2654");
        map.put("\uE025", "\u26C3");

        map.put("\uE022", "\u2726");
        map.put("\uE01A", "\u272F");
        map.put("\uE013", "\u2663");
        map.put("\uE012", "\u2668");
        map.put("\uE006", "\u2744");
        map.put("\uE01D", "\u26B6");
        map.put("\uE01B", "\u274D");
        map.put("\uE00A", "\u2620");
        map.put("\uE077", "\u2743");

        map.put("\uE02D", "\u16F7");
        map.put("\uE05B", "\u2618");

        return map;
    }

    public boolean enabled() {
        return this.enabled;
    }

    public Map<String, String> replacements() {
        return this.replacements;
    }

    public static SymbolReplacementConfig load() {
        if (Files.notExists(CONFIG_PATH)) {
            SymbolReplacementConfig config = new SymbolReplacementConfig();
            write(config);
            return config;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            SymbolReplacementConfig config = GSON.fromJson(reader, SymbolReplacementConfig.class);
            if (config == null) {
                throw new JsonParseException("Config file was empty");
            }

            if (config.replacements == null) {
                config.replacements = defaultReplacements();
            } else {
                LinkedHashMap<String, String> merged = defaultReplacements();
                merged.putAll(config.replacements);
                config.replacements = merged;
            }

            return config;
        } catch (IOException | JsonParseException exception) {
            Hypixel_old_symbolsClient.LOGGER.error("Failed to read symbol replacement config from {}", CONFIG_PATH, exception);
            SymbolReplacementConfig fallback = new SymbolReplacementConfig();
            write(fallback);
            return fallback;
        }
    }

    private static void write(SymbolReplacementConfig config) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException exception) {
            Hypixel_old_symbolsClient.LOGGER.error("Failed to write symbol replacement config to {}", CONFIG_PATH, exception);
        }
    }
}

