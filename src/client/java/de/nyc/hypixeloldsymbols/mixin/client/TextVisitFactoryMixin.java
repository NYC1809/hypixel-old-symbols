package de.nyc.hypixeloldsymbols.mixin.client;

import de.nyc.hypixeloldsymbols.text.SymbolReplacementService;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextVisitFactory.class)
abstract class TextVisitFactoryMixin {
    @ModifyVariable(method = "visitForwards(Ljava/lang/String;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceVisitForwards(String text) {
        return SymbolReplacementService.replace(text);
    }

    @ModifyVariable(method = "visitBackwards(Ljava/lang/String;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceVisitBackwards(String text) {
        return SymbolReplacementService.replace(text);
    }

    @ModifyVariable(method = "visitFormatted(Ljava/lang/String;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceVisitFormatted(String text) {
        return SymbolReplacementService.replace(text);
    }

    @ModifyVariable(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceIndexedVisitFormatted(String text) {
        return SymbolReplacementService.replace(text);
    }

    @ModifyVariable(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String hypixelOldSymbols$replaceLayeredVisitFormatted(String text) {
        return SymbolReplacementService.replace(text);
    }
}

