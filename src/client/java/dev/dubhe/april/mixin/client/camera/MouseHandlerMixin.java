package dev.dubhe.april.mixin.client.camera;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.april.client.feat.camera.FreeCam;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MouseHandler.class, priority = 100)
public abstract class MouseHandlerMixin {
    @WrapOperation(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private static void onBeforeCallPlayerTurn(LocalPlayer instance, double xRot, double yRot, Operation<Void> original) {
        if (FreeCam.INSTANCE.onPlayerTurn(yRot, xRot)) {
            original.call(instance, xRot, yRot);
        }
    }
}
