package de.nyc.hypixeloldsymbols.text;

import de.nyc.hypixeloldsymbols.Hypixel_old_symbolsClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class SymbolReplacementService {
    private static volatile boolean enabled = true;
    private static volatile List<Map.Entry<String, String>> replacements = List.of();

    private SymbolReplacementService() {
    }

    public static void initialize() {
        SymbolReplacementConfig config = SymbolReplacementConfig.load();
        enabled = config.enabled();
        replacements = config.replacements().entrySet().stream()
            .filter(entry -> entry.getKey() != null && !entry.getKey().isEmpty())
            .filter(entry -> entry.getValue() != null)
            .sorted(Comparator.<Map.Entry<String, String>>comparingInt(entry -> entry.getKey().length()).reversed())
            .toList();

        Hypixel_old_symbolsClient.LOGGER.info(
            "Loaded {} symbol replacement(s); enabled={}",
            replacements.size(),
            enabled
        );
    }

    public static String replace(String value) {
        if (!enabled || value == null || value.isEmpty() || replacements.isEmpty()) {
            return value;
        }

        String replaced = value;
        for (Map.Entry<String, String> entry : replacements) {
            replaced = replaced.replace(entry.getKey(), entry.getValue());
        }

        return replaced;
    }
}

