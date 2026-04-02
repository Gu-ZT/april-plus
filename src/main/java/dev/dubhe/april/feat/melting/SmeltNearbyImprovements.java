package dev.dubhe.april.feat.melting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;
import javax.annotation.Nullable;

public class SmeltNearbyImprovements {
    public static @Nullable LivingBlock getNearestSmeltingTarget(
        ServerLevel instance,
        EntityTypeTest<Entity, LivingBlock> entityTypeTest,
        final double x, final double y, final double z,
        AABB aabb,
        Predicate<LivingBlock> predicate
    ) {
        return instance.getNearestEntity(entityTypeTest, x, y, z, aabb, lb -> predicate.test(lb) && !lb.isSelected());
    }
}
