package dev.dubhe.april.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.april.feat.melting.SmeltNearbyImprovements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.behavior.SmeltNearbyLivingBlockBehavior;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;
import javax.annotation.Nullable;

@Mixin(SmeltNearbyLivingBlockBehavior.class)
public class SmeltNearbyLivingBlockBehaviorMixin {
    @WrapOperation(
        method = "canStartUsing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;getNearestEntity(Lnet/minecraft/world/level/entity/EntityTypeTest;DDDLnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Lnet/minecraft/world/entity/Entity;"
        )
    )
    private @Nullable Entity canStartUsing(
        ServerLevel instance,
        EntityTypeTest<Entity, LivingBlock> entityTypeTest,
        final double x, final double y, final double z,
        AABB aabb,
        Predicate<LivingBlock> predicate,
        Operation<Entity> original
    ) {
        return SmeltNearbyImprovements.getNearestSmeltingTarget(instance, entityTypeTest, x, y, z, aabb, predicate);
    }
}
