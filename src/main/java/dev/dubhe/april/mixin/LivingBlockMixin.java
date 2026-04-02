package dev.dubhe.april.mixin;

import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingBlock.class)
public abstract class LivingBlockMixin {

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void onSave(ValueOutput output, CallbackInfo ci) {
        LivingBlock self = (LivingBlock) (Object) this;
        boolean interacted = self.getEntityData().get(LivingBlockAccessor.getDataPlayerInteracted());
        output.putBoolean("player_interacted", interacted);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void onLoad(ValueInput input, CallbackInfo ci) {
        LivingBlock self = (LivingBlock) (Object) this;
        boolean interacted = input.getBooleanOr("player_interacted", false);
        self.getEntityData().set(LivingBlockAccessor.getDataPlayerInteracted(), interacted);
    }
}