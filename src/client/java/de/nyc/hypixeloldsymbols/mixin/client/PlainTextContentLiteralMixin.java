package de.nyc.hypixeloldsymbols.mixin.client;

import de.nyc.hypixeloldsymbols.text.SymbolReplacementService;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlainTextContent.Literal.class)
abstract class PlainTextContentLiteralMixin {
    @Shadow @Final private String string;

    @Inject(method = "visit(Lnet/minecraft/text/StringVisitable$Visitor;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private <T> void hypixelOldSymbols$replacePlainVisit(StringVisitable.Visitor<T> visitor, CallbackInfoReturnable<Optional<T>> cir) {
        cir.setReturnValue(visitor.accept(SymbolReplacementService.replace(this.string)));
    }

    @Inject(method = "visit(Lnet/minecraft/text/StringVisitable$StyledVisitor;Lnet/minecraft/text/Style;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private <T> void hypixelOldSymbols$replaceStyledVisit(StringVisitable.StyledVisitor<T> visitor, Style style, CallbackInfoReturnable<Optional<T>> cir) {
        cir.setReturnValue(visitor.accept(style, SymbolReplacementService.replace(this.string)));
    }

    @Inject(method = "string()Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    private void hypixelOldSymbols$replaceStringAccessor(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(SymbolReplacementService.replace(cir.getReturnValue()));
    }
}

