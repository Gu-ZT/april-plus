package dev.dubhe.april.mixin.crop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.livingblock.LivingBlockTypeRegistry;
import net.minecraft.world.entity.livingblock.LivingBlockTypes;
import net.minecraft.world.entity.livingblock.behavior.BuildBehavior;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(LivingBlockTypes.class)
interface LivingBlockTypesMixin {
    @WrapOperation(
            method = "buildRegistry",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/livingblock/LivingBlockTypeRegistry$Builder;build()Lnet/minecraft/world/entity/livingblock/LivingBlockTypeRegistry;"
            )
    )
    private static LivingBlockTypeRegistry buildRegistry(
            LivingBlockTypeRegistry.Builder instance,
            Operation<LivingBlockTypeRegistry> original
    ) {
        instance.register(
                List.of(
                        Items.WHEAT_SEEDS,
                        Items.CARROT,
                        Items.POTATO,
                        Items.BEETROOT_SEEDS,
                        Items.MELON_SEEDS,
                        Items.PUMPKIN_SEEDS,
                        Items.NETHER_WART,
                        Items.TORCHFLOWER_SEEDS,
                        Items.PITCHER_POD,
                        Items.SWEET_BERRIES,
                        Items.GLOW_BERRIES
                ),
                LivingBlockTypes.DEFAULT_ITEM.toBuilder().behavior(BuildBehavior.BUILD).build()
        );
        return original.call(instance);
    }
}
