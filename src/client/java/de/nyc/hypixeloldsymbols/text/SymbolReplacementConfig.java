package de.nyc.hypixeloldsymbols.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import de.nyc.hypixeloldsymbols.Hypixel_old_symbolsClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SymbolReplacementConfig {
    private static final Gson GSON = new GsonBuilder()
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir()
        .resolve(Hypixel_old_symbolsClient.MOD_ID + ".json");
    private static final List<ReplacementDefinition> DEFAULT_REPLACEMENT_DEFINITIONS = List.of(
        enabled("\uE010", "\u2764", "Health"),
        enabled("\uE008", "\u2748", "Defense"),
        enabled("\uE027", "\u2742", "True Defense"),
        enabled("\uE00D", "\u2741", "Strength"),
        enabled("\uE02C", "\u2623", "Crit Chance"),
        enabled("\uE007", "\u2620", "Crit Damage"),
        enabled("\uE001", "\u2694", "Bonus Attack Speed"),
        enabled("\uE00B", "\u2AFD", "Ferocity"),
        enabled("\uE024", "\u24C8", "Swing Range"),
        enabled("\uE003", "\u270E", "Intelligence"),
        enabled("\uE002", "\u0E51", "Ability Damage"),
        enabled("\uE011", "\u2763", "Health Regeneration"),
        enabled("\uE028", "\u2668", "Vitality"),
        enabled("\uE014", "\u2604", "Mending"),

        enabled("\uE005", "\u24C5", "Breaking Power"),
        enabled("\uE015", "\u2E15", "Mining Speed"),
        enabled("\uE016", "\u259A", "Mining Spread"),
        enabled("\uE00F", "\u259A", "Gemstone Spread"),
        enabled("\uE01C", "\u2727", "Pristine"),
        disabled("\uE053", "\u2618", "Mining Fortune"),

        enabled("\uE019", "\u0D60", "Bonus Pest Chance"),
        enabled("\uE02B", "\uE02B", "Overbloom"),
        disabled("\uE051", "\u2618", "Farming Fortune"),

        enabled("\uE023", "\u222E", "Sweep"),
        disabled("\uE054", "\u2618", "Foraging Fortune"),

        enabled("\uE00C", "\u2602", "Fishing Speed"),
        enabled("\uE021", "\u03B1", "Sea Creature Chance"),
        enabled("\uE009", "\u2693", "Double Hook Chance"),
        enabled("\uE02A", "\u2654", "Trophy Fish Chance"),
        enabled("\uE025", "\u26C3", "Treasure Chance"),

        enabled("\uE022", "\u2726", "Speed"),
        enabled("\uE01A", "\u272F", "Magic Find"),
        enabled("\uE013", "\u2663", "Pet Luck"),
        enabled("\uE012", "\u2668", "Heat Resistance"),
        enabled("\uE006", "\u2744", "Cold Resistance"),
        enabled("\uE01D", "\u26B6", "Respiration"),
        enabled("\uE01B", "\u274D", "Pressure Resistance"),
        enabled("\uE00A", "\u2620", "Fear"),
        enabled("\uE077", "\u2743", "Tracking"),

        enabled("\uE02D", "\u16F7", "Pull"),
        disabled("\uE05B", "\u2618", "Hunter Fortune")
    );

    private boolean enabled = true;
    private LinkedHashMap<String, String> replacements = defaultReplacements();

    private static LinkedHashMap<String, String> defaultReplacements() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        for (ReplacementDefinition definition : DEFAULT_REPLACEMENT_DEFINITIONS) {
            if (definition.enabledByDefault) {
                map.put(definition.key, definition.value);
            }
        }

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

        try {
            String fileContent = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
            String sanitizedContent = stripLineComments(fileContent);
            SymbolReplacementConfig config = GSON.fromJson(sanitizedContent, SymbolReplacementConfig.class);
            if (config == null) {
                throw new JsonParseException("[Hypixel-old-symbols] Config file was empty");
            }

            if (config.replacements == null) {
                config.replacements = defaultReplacements();
            } else {
                LinkedHashMap<String, String> merged = defaultReplacements();
                merged.putAll(config.replacements);
                config.replacements = merged;
            }

            String normalizedContent = serialize(config);
            if (!normalizedContent.equals(fileContent)) {
                writeString(normalizedContent);
            }

            return config;
        } catch (IOException | JsonParseException exception) {
            Hypixel_old_symbolsClient.LOGGER.error("[Hypixel-old-symbols] Failed to read symbol replacement config from {}", CONFIG_PATH, exception);
            SymbolReplacementConfig fallback = new SymbolReplacementConfig();
            write(fallback);
            return fallback;
        }
    }

    private static void write(SymbolReplacementConfig config) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            writeString(serialize(config));
        } catch (IOException exception) {
            Hypixel_old_symbolsClient.LOGGER.error("[Hypixel-old-symbols] Failed to write symbol replacement config to {}", CONFIG_PATH, exception);
        }
    }

    private static void writeString(String content) throws IOException {
        Files.writeString(CONFIG_PATH, content, StandardCharsets.UTF_8);
    }

    private static String serialize(SymbolReplacementConfig config) {
        String newline = System.lineSeparator();
        LinkedHashMap<String, String> replacements = config.replacements == null
            ? new LinkedHashMap<>()
            : config.replacements;
        Set<String> writtenKeys = new HashSet<>();
        List<String> replacementLines = new ArrayList<>();
        int remainingActiveEntries = replacements.size();

        for (ReplacementDefinition definition : DEFAULT_REPLACEMENT_DEFINITIONS) {
            String replacementValue = replacements.get(definition.key);
            if (replacementValue != null) {
                remainingActiveEntries--;
                replacementLines.add(
                    "    " + quoted(definition.key, true) + ": " + quoted(replacementValue, false)
                        + (remainingActiveEntries > 0 ? "," : "")
                        + " // " + definition.description
                );
                writtenKeys.add(definition.key);
            } else if (!definition.enabledByDefault) {
                replacementLines.add(
                    "    // " + quoted(definition.key, true) + ": " + quoted(definition.value, false)
                        + ", // " + definition.description + " (disabled by default)"
                );
            }
        }

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            if (writtenKeys.contains(entry.getKey()) || entry.getValue() == null) {
                continue;
            }

            remainingActiveEntries--;
            replacementLines.add(
                "    " + quoted(entry.getKey(), true) + ": " + quoted(entry.getValue(), false)
                    + (remainingActiveEntries > 0 ? "," : "")
                    + " // Custom entry"
            );
        }

        StringBuilder builder = new StringBuilder();
        builder.append("{").append(newline)
            .append("  \"enabled\": ").append(config.enabled).append(',').append(newline)
            .append("  \"replacements\": {").append(newline);

        for (int i = 0; i < replacementLines.size(); i++) {
            builder.append(replacementLines.get(i)).append(newline);
        }

        builder.append("  }").append(newline)
            .append("}").append(newline);
        return builder.toString();
    }

    private static String stripLineComments(String content) {
        StringBuilder builder = new StringBuilder(content.length());
        boolean inString = false;
        boolean escaped = false;

        for (int index = 0; index < content.length(); index++) {
            char current = content.charAt(index);

            if (inString) {
                builder.append(current);
                if (escaped) {
                    escaped = false;
                } else if (current == '\\') {
                    escaped = true;
                } else if (current == '"') {
                    inString = false;
                }
                continue;
            }

            if (current == '"') {
                inString = true;
                builder.append(current);
                continue;
            }

            if (current == '/' && index + 1 < content.length() && content.charAt(index + 1) == '/') {
                index += 2;
                while (index < content.length() && content.charAt(index) != '\n' && content.charAt(index) != '\r') {
                    index++;
                }

                if (index < content.length()) {
                    if (content.charAt(index) == '\r') {
                        builder.append('\r');
                        if (index + 1 < content.length() && content.charAt(index + 1) == '\n') {
                            builder.append('\n');
                            index++;
                        }
                    } else {
                        builder.append('\n');
                    }
                }
                continue;
            }

            builder.append(current);
        }

        return builder.toString();
    }

    private static String quoted(String value, boolean forceUnicodeEscapes) {
        StringBuilder builder = new StringBuilder();
        builder.append('"');
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            switch (current) {
                case '\\' -> builder.append("\\\\");
                case '"' -> builder.append("\\\"");
                case '\b' -> builder.append("\\b");
                case '\f' -> builder.append("\\f");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                default -> {
                    if (forceUnicodeEscapes || Character.isISOControl(current) || Character.getType(current) == Character.PRIVATE_USE) {
                        builder.append(String.format("\\u%04X", (int) current));
                    } else {
                        builder.append(current);
                    }
                }
            }
        }
        builder.append('"');
        return builder.toString();
    }

    private static ReplacementDefinition enabled(String key, String value, String description) {
        return new ReplacementDefinition(key, value, description, true);
    }

    private static ReplacementDefinition disabled(String key, String value, String description) {
        return new ReplacementDefinition(key, value, description, false);
    }

    private static final class ReplacementDefinition {
        private final String key;
        private final String value;
        private final String description;
        private final boolean enabledByDefault;

        private ReplacementDefinition(String key, String value, String description, boolean enabledByDefault) {
            this.key = key;
            this.value = value;
            this.description = description;
            this.enabledByDefault = enabledByDefault;
        }
    }
}

