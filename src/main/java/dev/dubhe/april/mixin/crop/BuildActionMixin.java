package dev.dubhe.april.mixin.crop;

import dev.dubhe.april.feat.crop.CropStateImprovements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.cognition.BuildTarget;
import net.minecraft.world.entity.livingblock.cognition.Desires;
import net.minecraft.world.item.BuildAction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuildAction.class)
abstract class BuildActionMixin {
    @Inject(method = "command", at = @At("HEAD"), cancellable = true)
    private void command(
            LivingBlock block,
            ServerPlayer player,
            Vec3 pos,
            BlockPos blockPos,
            Direction direction,
            CallbackInfo ci
    ) {
        if (!block.getItemStack().is(ItemTags.BLOCK_PLACERS)) return;

        Level world = block.level();
        BlockState blockState = CropStateImprovements.toBlockState(block);
        if (!blockState.canSurvive(world, blockPos.relative(direction))) {
            ci.cancel();
            return;
        }

        if (!blockState.isAir()) {
            block.hopesAndDreams.desire(Desires.BUILD, new BuildTarget(blockPos, direction));
        }
    }
}
