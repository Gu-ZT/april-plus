package dev.dubhe.april.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.april.AprilPlus;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.Target;
import net.minecraft.world.entity.livingblock.movement.MovementData;
import net.minecraft.world.entity.livingblock.movement.MovementStrategy;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingBlock.class)
abstract class LivingBlockCCEMixin {
    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/livingblock/movement/MovementStrategy;moveTowardsTarget(Lnet/minecraft/world/entity/livingblock/LivingBlock;Lnet/minecraft/world/entity/livingblock/Target;Lnet/minecraft/world/phys/Vec3;)Z"
        )
    )
    private <T extends MovementData> boolean tickWrapMoveTowardsTarget(
        MovementStrategy<T> instance,
        LivingBlock livingBlock,
        Target target,
        Vec3 vec3,
        Operation<Boolean> original
    ) {
        try {
            return original.call(instance, livingBlock, target, vec3);
        } catch (Exception exception) {
            AprilPlus.LOGGER.error(exception.getMessage(), exception);
            return false;
        }
    }

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/livingblock/movement/MovementStrategy;resetMovement(Lnet/minecraft/world/entity/livingblock/LivingBlock;)V"
        )
    )
    private <T extends MovementData> void tickWrapResetMovement(
        MovementStrategy<T> instance, LivingBlock livingBlock, Operation<Void> original
    ) {
        try {
            original.call(instance, livingBlock);
        } catch (Exception exception) {
            AprilPlus.LOGGER.error(exception.getMessage(), exception);
        }
    }

    @WrapOperation(
        method = "interrupt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/livingblock/movement/MovementStrategy;resetMovement(Lnet/minecraft/world/entity/livingblock/LivingBlock;)V"
        )
    )
    private <T extends MovementData> void interruptWrapResetMovement(
        MovementStrategy<T> instance, LivingBlock livingBlock, Operation<Void> original
    ) {
        try {
            original.call(instance, livingBlock);
        } catch (Exception exception) {
            AprilPlus.LOGGER.error(exception.getMessage(), exception);
        }
    }

    @WrapOperation(
        method = "teleportSetPosition",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/livingblock/movement/MovementStrategy;resetMovement(Lnet/minecraft/world/entity/livingblock/LivingBlock;)V"
        )
    )
    private <T extends MovementData> void teleportSetPositionWrapResetMovement(
        MovementStrategy<T> instance, LivingBlock livingBlock, Operation<Void> original
    ) {
        try {
            original.call(instance, livingBlock);
        } catch (Exception exception) {
            AprilPlus.LOGGER.error(exception.getMessage(), exception);
        }
    }

    @WrapOperation(
        method = "adjustStepUpMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/livingblock/movement/MovementStrategy;adjustStepUpMovement(Lnet/minecraft/world/entity/livingblock/LivingBlock;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"
        )
    )
    private <T extends MovementData> Vec3 adjustStepUpMovementWrap(
        MovementStrategy<T> instance, LivingBlock entity, Vec3 movement, Operation<Vec3> original
    ) {
        try {
            return original.call(instance, entity, movement);
        } catch (Exception exception) {
            AprilPlus.LOGGER.error(exception.getMessage(), exception);
            return movement;
        }
    }
}
