package dev.dubhe.april.mixin.stack;

import dev.dubhe.april.feat.stack.ActionStack;
import dev.dubhe.april.feat.stack.LivingBlockExtension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.livingblock.LivingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingBlock.class)
abstract class LivingBlockMixin implements LivingBlockExtension {
    @Shadow
    public abstract boolean isIdle();

    @Shadow
    private int currentActivities;
    @Unique
    private final ActionStack aprilPlus$actionStack = new ActionStack((LivingBlock) (Object) this);

    @Override
    public ActionStack aprilPlus$getActionStack() {
        return this.aprilPlus$actionStack;
    }

    @Inject(
        method = "tickBehaviors",
        at = @At("RETURN")
    )
    private void tickBehaviors(ServerLevel level, CallbackInfo ci) {
        if (this.currentActivities != 0) {
            return;
        }
        if (!this.isIdle()) {
            return;
        }
        this.aprilPlus$getActionStack().accept();
    }
}
