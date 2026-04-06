package dev.dubhe.april.mixin.enchantment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.interact.EnchantRandomItemInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Predicate;

@Mixin(EnchantRandomItemInteraction.class)
abstract class EnchantRandomItemInteractionMixin {
    @WrapOperation(
            method = "apply",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private @NonNull List<LivingBlock> getEntities(
            ServerLevel instance,
            final EntityTypeTest<Entity, LivingBlock> type,
            final AABB bb,
            final Predicate<? super LivingBlock> selector,
            Operation<List<LivingBlock>> original
    ) {
        // 不要让活化书架被附魔台吸走
        // 否则会遮挡书架上方空间 导致无法正常附魔
        // 如果想要解决这个问题可能要大面积改寻路
        // 所以采用更方便的解决方案
        List<LivingBlock> called = original.call(instance, type, bb, selector);
        return called.stream()
                .filter(block -> !block.getBlockState().is(Blocks.BOOKSHELF))
                .toList();
    }
}
