package de.nyc.hypixeloldsymbols;

import de.nyc.hypixeloldsymbols.text.SymbolReplacementService;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hypixel_old_symbolsClient implements ClientModInitializer {
    public static final String MOD_ID = "hypixel-old-symbols";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        SymbolReplacementService.initialize();
        LOGGER.info("Hypixel_old_symbols Mod Initialized!");
    }
}
