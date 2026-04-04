package dev.dubhe.april.mixin.target;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Ghast.class)
abstract class GhastMixin {
    @WrapOperation(
        method = "registerGoals",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",
            ordinal = 4
        )
    )
    private void disableLivingBlockAttacking(GoalSelector instance, int prio, Goal goal, Operation<Void> original) {
    }
}
