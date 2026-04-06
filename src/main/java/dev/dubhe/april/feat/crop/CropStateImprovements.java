package dev.dubhe.april.feat.crop;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.state.BlockState;

public class CropStateImprovements {
    public static BlockState toBlockState(LivingBlock entity) {
        ItemStack itemStack = entity.getItemStack();
        Item item = itemStack.getItem();
        if (item instanceof BlockItem bi && itemStack.is(ItemTags.BLOCK_PLACERS)) {
            BlockState blockState = bi.getBlock().defaultBlockState();
            BlockItemStateProperties stateProperties = itemStack.get(DataComponents.BLOCK_STATE);
            if (stateProperties != null) {
                blockState = stateProperties.apply(blockState);
            }

            return blockState;
        }

        return entity.getBlockState();
    }
}
