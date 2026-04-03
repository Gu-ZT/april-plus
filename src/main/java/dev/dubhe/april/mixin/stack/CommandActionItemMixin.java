package dev.dubhe.april.mixin.stack;

import dev.dubhe.april.feat.stack.ActionStack;
import dev.dubhe.april.feat.stack.LivingBlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CommandActionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(CommandActionItem.class)
abstract class CommandActionItemMixin {
    @Unique
    private final CommandActionItem aprilPlus$self = (CommandActionItem) (Object) this;

    @Unique
    private void aprilPlus$action(Player player, Consumer<ActionStack> consumer, CallbackInfoReturnable<Boolean> cir) {
        List<LivingBlock> blockList = player.getCommandedBlocks();
        if (!player.isShiftKeyDown()) {
            for (LivingBlock commandedBlock : blockList) {
                ((LivingBlockExtension) commandedBlock).aprilPlus$getActionStack().clear();
            }
            return;
        }
        for (LivingBlock commandedBlock : blockList) {
            ActionStack actionStack = ((LivingBlockExtension) commandedBlock).aprilPlus$getActionStack();
            consumer.accept(actionStack);
        }
        cir.setReturnValue(true);
        cir.cancel();
    }

    @Inject(
        method = "actionOnBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    private void actionOnBlock(Player player, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        this.aprilPlus$action(
            player,
            actionStack -> actionStack.actionOnBlock(this.aprilPlus$self, player, pos, direction),
            cir
        );
    }

    @Inject(
        method = "actionOnEntity",
        at = @At("HEAD"),
        cancellable = true
    )
    private void actionOnEntity(Player player, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        this.aprilPlus$action(
            player,
            actionStack -> actionStack.actionOnEntity(this.aprilPlus$self, player, entity),
            cir
        );
    }
}
