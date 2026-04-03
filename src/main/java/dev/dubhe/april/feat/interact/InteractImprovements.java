package dev.dubhe.april.feat.interact;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.LivingBlockGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ActionItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class InteractImprovements {
    @SuppressWarnings("resource")
    public static boolean interactLivingBlock(
        Player player,
        LivingBlock target,
        boolean doubleClick,
        boolean lastShiftDown,
        final InteractionHand hand,
        final Vec3 location
    ) {
        boolean selected = target.isSelected();
        if (doubleClick) {
            if (player.isShiftKeyDown() && lastShiftDown) {
                List<LivingBlock> entities = target.level().getEntities(
                    EntityTypeTest.forClass(LivingBlock.class),
                    target.getBoundingBox().inflate(10, 10, 10),
                    tb -> tb.isOwnedBy(player) && Objects.equals(tb.getGroup(), target.getGroup())
                );
                target.setOwner(null);
                target.setSelected(false);
                for (LivingBlock entity : entities) {
                    entity.setOwner(null);
                    entity.setSelected(false);
                    entity.setGroup(LivingBlockGroup.NONE);
                }
                return true;
            } else if (selected) {
                List<LivingBlock> entities = target.level().getEntities(
                    EntityTypeTest.forClass(LivingBlock.class),
                    target.getBoundingBox().inflate(5, 5, 5),
                    tb -> ItemStack.isSameItem(tb.getItemStack(), target.getItemStack())
                          && tb.canBeControlledBy(player)
                          && !tb.isOwnedBy(player)
                );
                for (LivingBlock entity : entities) {
                    InteractImprovements.interact(entity, player, hand, location);
                }
                return true;
            }
        } else if (player.isShiftKeyDown()) {
            target.setOwner(null);
            target.setSelected(false);
            return true;
        }
        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    private static InteractionResult interact(
        final LivingBlock entity,
        final Player player,
        final InteractionHand hand,
        final Vec3 location
    ) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.getItem() instanceof ActionItem action) {
            InteractionResult interactionResult = action.interactLivingBlock(player, entity);
            if (interactionResult != InteractionResult.PASS) {
                return interactionResult;
            }
        }

        InteractionResult apply = ((LivingBlockExtension) entity).aprilPlus$getOnInteract().apply(player, hand, location, entity);
        if (apply == InteractionResult.PASS) {
            return ((LivingBlockExtension) entity).aprilPlus$superInteract(player, hand, location);
        } else {
            return apply;
        }
    }
}
