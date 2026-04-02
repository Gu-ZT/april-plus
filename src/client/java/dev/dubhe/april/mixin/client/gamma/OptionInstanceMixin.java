package dev.dubhe.april.mixin.client.gamma;

import dev.dubhe.april.client.feat.gamma.GammaManager;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionInstance.class)
abstract class OptionInstanceMixin<T> {
    @Shadow
    @Final
    private Component caption;

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    public void getModValue(CallbackInfoReturnable<Double> info) {
        if (isGammaOption()) {
            info.setReturnValue(GammaManager.getGamma());
        }
    }

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    public void setModValue(T value, CallbackInfo info) {
        if (isGammaOption()) {
            GammaManager.setGamma((double) value);
            info.cancel();
        }
    }

    @Unique
    private boolean isGammaOption() {
        if (caption.getContents() instanceof TranslatableContents translatableTextContent) {
            return translatableTextContent.getKey().equals("options.gamma");
        }

        return false;
    }
}
