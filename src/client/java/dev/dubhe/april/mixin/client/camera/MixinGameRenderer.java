package dev.dubhe.april.mixin.client.camera;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.april.client.feat.camera.FreeCam;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(at = @At("HEAD"), method = "render")
    private void onRender(DeltaTracker delta, boolean renderLevel, CallbackInfo ci) {
        FreeCam.INSTANCE.onRenderTickStart();
    }

    @WrapOperation(
        method = "renderItemInHand",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z", ordinal = 0)
    )
    private boolean onRenderItemInHandIsFirstPerson(CameraType cameraType, Operation<Boolean> original) {
        if (FreeCam.INSTANCE.isActive()) {
            return true;
        }
        return original.call(cameraType);
    }
}
