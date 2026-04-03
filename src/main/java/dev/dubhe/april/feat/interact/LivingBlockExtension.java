package dev.dubhe.april.feat.interact;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.livingblock.interact.OnInteract;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface LivingBlockExtension {
    OnInteract aprilPlus$getOnInteract();

    InteractionResult aprilPlus$superInteract(final Player player, final InteractionHand hand, final Vec3 location);
}
