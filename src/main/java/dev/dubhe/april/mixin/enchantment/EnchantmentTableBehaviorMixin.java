package dev.dubhe.april.mixin.enchantment;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.behavior.EnchantmentTableBehavior;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(EnchantmentTableBehavior.class)
abstract class EnchantmentTableBehaviorMixin {
    @Shadow
    private int waitingTicks;

    @Shadow
    protected abstract void cleanupEnchantingAttempt(final LivingBlock entity);

    @Shadow
    protected abstract List<EnchantmentInstance> getEnchantmentList(final RandomSource random, final RegistryAccess access, final ItemStack itemStack, final int enchantmentCost);

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(LivingBlock entity, ServerLevel level, int tickCount, CallbackInfoReturnable<Boolean> cir) {
        // 整个附魔逻辑的错误超过3处 用WrapOperation包装不如直接Inject重写
        // 1.活化书架实体挡住附魔物品
        // 2.混淆getEnchantmentCost获取的值和附魔所需的经验 导致经验要求过高
        // 3.附魔的书而非附魔书
        this.waitingTicks++;
        if (this.waitingTicks > 300) {
            this.cleanupEnchantingAttempt(entity);
            cir.setReturnValue(false);
            return;
        }

        if (entity.livingBlockBeingEnchanted == null) {
            this.cleanupEnchantingAttempt(entity);
            cir.setReturnValue(false);
            return;
        }

        Player commander = entity.getCommander();
        if (commander == null) {
            this.cleanupEnchantingAttempt(entity);
            cir.setReturnValue(false);
            return;
        }

        Vec3 enchantablePos = entity.livingBlockBeingEnchanted.position();
        Vec3 above = entity.position().add(Vec3.Y_AXIS);
        boolean isItemToEnchantNearby = enchantablePos.closerThan(above, 0.7F);
        if (isItemToEnchantNearby) {
            int bookcases = (int) level.getEntities(
                    EntityType.LIVING_BLOCK,
                    entity.getBoundingBox().inflate(5.0F),
                    Entity::isAlive
            ).stream().filter(block -> block.getBlockState().is(Blocks.BOOKSHELF)).count();

            ItemStack itemStack = entity.livingBlockBeingEnchanted.getItemStack();
            int cost = EnchantmentHelper.getEnchantmentCost(level.getRandom(), 2, bookcases, itemStack);
            if (3 > commander.experienceLevel) {
                this.cleanupEnchantingAttempt(entity);
                cir.setReturnValue(false);
                return;
            }

            commander.onEnchantmentPerformed(itemStack, 3);

            for (EnchantmentInstance enchantment : this.getEnchantmentList(level.getRandom(), level.registryAccess(), itemStack, cost)) {
                itemStack.enchant(enchantment.enchantment(), enchantment.level());
            }

            if (itemStack.is(Items.BOOK)) {
                itemStack = itemStack.transmuteCopy(Items.ENCHANTED_BOOK);
            }

            LivingBlock newItem = LivingBlock.createAt(level, entity.livingBlockBeingEnchanted.blockPosition().above(1), itemStack);
            Objects.requireNonNull(newItem);
            entity.livingBlockBeingEnchanted.discard();
            RandomSource random = level.getRandom();
            level.sendParticles(
                    ParticleTypes.EXPLOSION,
                    true,
                    true,
                    newItem.getX() + random.nextDouble(),
                    newItem.getY(),
                    newItem.getZ() + random.nextDouble(),
                    1,
                    0.0,
                    0.0,
                    0.0,
                    1.0
            );
            this.cleanupEnchantingAttempt(entity);
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(true);
        }
    }
}
