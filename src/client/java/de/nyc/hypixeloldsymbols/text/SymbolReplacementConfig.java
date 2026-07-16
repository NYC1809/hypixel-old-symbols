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
    private LinkedHashMap<String, String> replacements = new LinkedHashMap<>();

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
                config.replacements = new LinkedHashMap<>();
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

