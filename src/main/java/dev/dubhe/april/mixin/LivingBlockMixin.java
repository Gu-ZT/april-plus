package dev.dubhe.april.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ActionItem;
import net.minecraft.world.item.GroupAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import javax.annotation.Nullable;

@Mixin(LivingBlock.class)
public class LivingBlockMixin {
    @Unique
    @Nullable
    private Player lastInteractPlayer;
    @Unique
    private long lastInteractTime = 0;

    @WrapOperation(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ActionItem;interactLivingBlock(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/livingblock/LivingBlock;)Lnet/minecraft/world/InteractionResult;"
        )
    )
    public InteractionResult interactLivingBlock(
        ActionItem instance,
        Player player,
        LivingBlock target,
        Operation<InteractionResult> original
    ) {
        long currentTimeMillis = System.currentTimeMillis();
        boolean doubleClick = lastInteractPlayer != null && lastInteractPlayer.is(player) && currentTimeMillis - lastInteractTime < 1000;
        boolean selected = target.isSelected();
        if (target.canBeControlledBy(player) && !(instance instanceof GroupAction) && selected && doubleClick) {
            List<LivingBlock> entities = target.level().getEntities(
                EntityTypeTest.forClass(LivingBlock.class),
                target.getBoundingBox().inflate(5, 5, 5),
                tb -> {
                    ItemStack.isSameItem(tb.getItemStack(), target.getItemStack());
                    return tb.canBeControlledBy(player);
                }
            );
            for (LivingBlock entity : entities) {
                entity.setOwner(player);
                entity.setSelected(true);
            }
            this.lastInteractTime = currentTimeMillis;
            this.lastInteractPlayer = player;
            return InteractionResult.SUCCESS;
        }
        this.lastInteractTime = currentTimeMillis;
        this.lastInteractPlayer = player;
        return original.call(instance, player, target);
    }
}
