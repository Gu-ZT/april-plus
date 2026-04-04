package dev.dubhe.april.mixin.crashfix;

import dev.dubhe.april.crashfix.LivingBlockExtension;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.LivingBlockType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingBlockType.class)
public class LivingBlockTypeMixin {
    @Inject(
        method = "create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/entity/livingblock/LivingBlock;",
        at = @At("RETURN")
    )
    private void create(Level level, BlockState blockState, CallbackInfoReturnable<LivingBlock> cir) {
        LivingBlock livingBlock = cir.getReturnValue();
        if (livingBlock == null) return;
        ((LivingBlockExtension) livingBlock).aprilPlus$setIsBlock(true);
    }
}
