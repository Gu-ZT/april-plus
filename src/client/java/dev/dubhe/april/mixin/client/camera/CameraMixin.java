package dev.dubhe.april.mixin.client.camera;

import dev.dubhe.april.client.feat.camera.FreeCam;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
abstract class CameraMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private boolean detached;

    @Shadow
    private boolean isPanoramicMode;

    @Shadow
    protected abstract void setRotation(final float yRot, final float xRot);

    @Shadow
    protected abstract void setPosition(final double x, final double y, final double z);

    @Inject(
        method = "alignWithEntity",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z", ordinal = 0),
        cancellable = true)
    private void onAlignWithEntity(float partialTicks, CallbackInfo info) {
        FreeCam freeCam = FreeCam.INSTANCE;
        if (freeCam.isActive()) {
            this.detached = true;
            this.setRotation(freeCam.getYRot(), freeCam.getXRot());
            this.setPosition(freeCam.getX(), freeCam.getY(), freeCam.getZ());
            info.cancel();
        }
    }

    @Inject(method = "calculateFov", at = @At("HEAD"), cancellable = true)
    private void onBeforeCalculateFov(float partialTicks, CallbackInfoReturnable<Float> info) {
        if (this.isPanoramicMode) {
            return;
        }

        if (FreeCam.INSTANCE.isActive()) {
            info.setReturnValue((float) this.minecraft.options.fov().get());
        }
    }

    @Inject(at = @At("HEAD"), method = "modifyFovBasedOnDeathOrFluid", cancellable = true)
    private void onModifyFovBasedOnDeathOrFluid(float partialTicks, float fov, CallbackInfoReturnable<Float> info) {
        if (FreeCam.INSTANCE.isActive()) {
            info.setReturnValue(fov);
        }
    }
}
