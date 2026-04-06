package dev.dubhe.april.mixin.crop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.april.feat.crop.CropStateImprovements;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.behavior.BuildBehavior;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BuildBehavior.class)
abstract class BuildBehaviorMixin {
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/livingblock/LivingBlock;getBlockState()Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private BlockState getBlockState(
            LivingBlock instance,
            Operation<BlockState> original
    ) {
        return CropStateImprovements.toBlockState(instance);
    }

    @Inject(method = "canStartUsing", at = @At("HEAD"), cancellable = true)
    private void canStartUsing(
            LivingBlock entity,
            CallbackInfoReturnable<Boolean> cir
    ) {
        cir.setReturnValue(!CropStateImprovements.toBlockState(entity).isAir());
    }
}
