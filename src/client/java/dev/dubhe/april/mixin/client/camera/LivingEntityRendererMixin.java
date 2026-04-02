package dev.dubhe.april.mixin.client.camera;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.april.client.feat.camera.FreeCam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(value = LivingEntityRenderer.class, priority = 2000)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {
    @WrapOperation(
        method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;")
    )
    private static @Nullable Entity onShouldShowNameChangeCameraEntity(Minecraft instance, Operation<Entity> original) {
        return FreeCam.INSTANCE.isActive() ? null : original.call(instance);
    }
}
