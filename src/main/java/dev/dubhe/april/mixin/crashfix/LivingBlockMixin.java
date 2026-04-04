package dev.dubhe.april.mixin.crashfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.april.AprilPlus;
import dev.dubhe.april.crashfix.LivingBlockExtension;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.Target;
import net.minecraft.world.entity.livingblock.movement.MovementData;
import net.minecraft.world.entity.livingblock.movement.MovementStrategy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingBlock.class)
abstract class LivingBlockMixin extends Entity implements LivingBlockExtension {
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final EntityDataAccessor<Boolean> APRIL_PLUS_IS_BLOCK = SynchedEntityData.defineId(
        LivingBlock.class,
        EntityDataSerializers.BOOLEAN
    );

    public LivingBlockMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void aprilPlus$setIsBlock(boolean isBlock) {
        this.entityData.set(APRIL_PLUS_IS_BLOCK, isBlock);
    }

    @Shadow
    public abstract ItemStack getItemStack();

    @Shadow
    public abstract BlockState getBlockState();


    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(APRIL_PLUS_IS_BLOCK, false);
    }

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

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (
            this.getItemStack().isEmpty()
            || (
                this.entityData.get(APRIL_PLUS_IS_BLOCK)
                && this.getBlockState().isAir()
            )
        ) {
            this.discard();
            ci.cancel();
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void onSave(ValueOutput output, CallbackInfo ci) {
        output.putBoolean("april_plus_is_block", this.entityData.get(APRIL_PLUS_IS_BLOCK));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void onLoad(ValueInput input, CallbackInfo ci) {
        this.entityData.set(APRIL_PLUS_IS_BLOCK, input.getBooleanOr("april_plus_is_block", false));
    }
}
