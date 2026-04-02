package dev.dubhe.april.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.CraftingGrid;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.behavior.BeCraftingIngredient;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeCraftingIngredient.class)
abstract class BeCraftingIngredientMixin {
    @Shadow
    private @Nullable CraftingGrid grid;

    @WrapOperation(
        method = "onStop",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/CraftingGrid;stopTrying(Lnet/minecraft/world/entity/livingblock/LivingBlock;II)V"
        )
    )
    public void onStop(@Nullable CraftingGrid instance, LivingBlock block, int x, int y, Operation<Void> original) {
        if (this.grid != null) {
            this.grid.stopTrying(block, x, y);
        }
    }

    @WrapOperation(
        method = "onStart",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/CraftingGrid;startTrying(Lnet/minecraft/world/entity/livingblock/LivingBlock;II)V"
        )
    )
    public void onStart(@Nullable CraftingGrid instance, LivingBlock block, int x, int y, Operation<Void> original) {
        if (this.grid != null) {
            this.grid.startTrying(block, x, y);
        }
    }
}
