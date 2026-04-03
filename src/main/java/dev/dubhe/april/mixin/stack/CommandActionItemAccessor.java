package dev.dubhe.april.mixin.stack;

import net.minecraft.world.entity.LivingBlockCommand;
import net.minecraft.world.item.CommandActionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CommandActionItem.class)
public interface CommandActionItemAccessor {
    @Accessor
    LivingBlockCommand.Type getCommandType();
}
