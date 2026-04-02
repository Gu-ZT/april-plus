package dev.dubhe.april.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.livingblock.LivingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingBlock.class)
public interface LivingBlockAccessor {
    @Accessor("DATA_PLAYER_INTERACTED")
    static EntityDataAccessor<Boolean> getDataPlayerInteracted() {
        throw new AssertionError();
    }
}