package dev.dubhe.april.mixin.client.gamma;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.april.client.feat.gamma.GammaDouble;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(Options.class)
abstract class OptionsMixin {
    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "NEW",
            target = "("
                     + "Ljava/lang/String;Lnet/minecraft/client/OptionInstance$TooltipSupplier;"
                     + "Lnet/minecraft/client/OptionInstance$CaptionBasedToString;"
                     + "Lnet/minecraft/client/OptionInstance$ValueSet;"
                     + "Ljava/lang/Object;"
                     + "Ljava/util/function/Consumer;"
                     + ")Lnet/minecraft/client/OptionInstance;"
        )
    )
    private <O> OptionInstance<O> createOptionInstance(
        final String captionId,
        final OptionInstance.TooltipSupplier<O> tooltip,
        final OptionInstance.CaptionBasedToString<O> toString,
        final OptionInstance.ValueSet<O> values,
        final O initialValue,
        final Consumer<O> onValueUpdate,
        Operation<OptionInstance<O>> original
    ) {
        if (!"options.gamma".equals(captionId)) {
            return original.call(captionId, tooltip, toString, values, initialValue, onValueUpdate);
        }
        return original.call(captionId, tooltip, toString, GammaDouble.INSTANCE, initialValue, onValueUpdate);
    }
}