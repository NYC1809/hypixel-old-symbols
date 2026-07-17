package de.nyc.hypixeloldsymbols.mixin.client;

import de.nyc.hypixeloldsymbols.text.SymbolReplacementService;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(StringDecomposer.class)
abstract class TextVisitFactoryMixin {
    @ModifyVariable(method = "iterate(Ljava/lang/String;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceVisitForwards(String text) {
        return SymbolReplacementService.replace(text);
    }

    @ModifyVariable(method = "iterateBackwards(Ljava/lang/String;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceVisitBackwards(String text) {
        return SymbolReplacementService.replace(text);
    }

    @ModifyVariable(method = "iterateFormatted(Ljava/lang/String;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceVisitFormatted(String text) {
        return SymbolReplacementService.replace(text);
    }

    @ModifyVariable(method = "iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceIndexedVisitFormatted(String text) {
        return SymbolReplacementService.replace(text);
    }

    @ModifyVariable(method = "iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceLayeredVisitFormatted(String text) {
        return SymbolReplacementService.replace(text);
    }
}
