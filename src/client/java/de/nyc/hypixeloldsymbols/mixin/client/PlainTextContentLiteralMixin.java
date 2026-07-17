package de.nyc.hypixeloldsymbols.mixin.client;

import de.nyc.hypixeloldsymbols.text.SymbolReplacementService;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlainTextContents.LiteralContents.class)
abstract class PlainTextContentLiteralMixin {
    @Shadow @Final private String text;

    @Inject(method = "visit(Lnet/minecraft/network/chat/FormattedText$ContentConsumer;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private <T> void hypixelOldSymbols$replacePlainVisit(FormattedText.ContentConsumer<T> visitor, CallbackInfoReturnable<Optional<T>> cir) {
        cir.setReturnValue(visitor.accept(SymbolReplacementService.replace(this.text)));
    }

    @Inject(method = "visit(Lnet/minecraft/network/chat/FormattedText$StyledContentConsumer;Lnet/minecraft/network/chat/Style;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private <T> void hypixelOldSymbols$replaceStyledVisit(FormattedText.StyledContentConsumer<T> visitor, Style style, CallbackInfoReturnable<Optional<T>> cir) {
        cir.setReturnValue(visitor.accept(style, SymbolReplacementService.replace(this.text)));
    }

    @Inject(method = "text()Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    private void hypixelOldSymbols$replaceStringAccessor(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(SymbolReplacementService.replace(cir.getReturnValue()));
    }
}
