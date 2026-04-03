package dev.dubhe.april.feat.stack;

import dev.dubhe.april.mixin.stack.CommandActionItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingBlockCommand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Targetable;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.LivingBlockGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CommandActionItem;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public class ActionStack extends LinkedList<ActionStack.ActionEntry> {
    private final LivingBlock livingBlock;

    public ActionStack(LivingBlock livingBlock) {
        this.livingBlock = livingBlock;
    }

    public void accept() {
        if (this.isEmpty()) return;
        ActionEntry pop = this.pop();
        pop.action.apply(pop.type, livingBlock);
    }

    public void actionOnBlock(CommandActionItem type, final Player player, final BlockPos pos, final Direction direction) {
        this.add(new ActionEntry(type, new ActionOnBlock(player, pos, direction, type::command)));
        if (player instanceof ServerPlayer serverPlayer) {
            ActionStack.spawnCommandEntity(type, serverPlayer.level(), pos.getCenter().add(direction.getUnitVec3().scale(0.5)));
        }
    }

    public void actionOnEntity(CommandActionItem type, final Player player, final Entity entity) {
        this.add(new ActionEntry(type, new ActionOnEntity(player, entity, type::command)));
        if (player instanceof ServerPlayer serverPlayer) {
            ActionStack.spawnCommandEntity(type, serverPlayer.level(), entity.position());
        }
    }

    public record ActionEntry(
        CommandActionItem type,
        Action action
    ) {
    }

    public interface Action extends BiFunction<CommandActionItem, LivingBlock, Boolean> {
    }

    public interface OnBlockCommand {
        void command(
            final LivingBlock block,
            final ServerPlayer player,
            final Vec3 pos,
            final BlockPos blockPos,
            final Direction direction
        );
    }

    public interface OnEntityCommand {
        void command(final LivingBlock block, final ServerPlayer player, final Targetable target);
    }

    public record ActionOnBlock(Player player, BlockPos pos, Direction direction, OnBlockCommand command) implements Action {
        @Override
        public Boolean apply(CommandActionItem type, LivingBlock livingBlock) {
            if (player.isSpectator()) {
                return false;
            } else if (player instanceof ServerPlayer serverPlayer) {
                Vec3 targetPos = pos.getCenter().add(direction.getUnitVec3().scale(0.5));
                ActionStack.applyCommand(
                    type,
                    livingBlock,
                    serverPlayer,
                    targetPos,
                    () -> this.command.command(livingBlock, serverPlayer, targetPos, pos, direction)
                );
                return true;
            } else {
                return true;
            }
        }
    }

    public record ActionOnEntity(Player player, Entity entity, OnEntityCommand command) implements Action {
        @Override
        public Boolean apply(CommandActionItem type, LivingBlock livingBlock) {
            if (player.isSpectator()) {
                return false;
            } else if (player instanceof ServerPlayer serverPlayer) {
                if (entity instanceof LivingEntity target) {
                    ActionStack.applyCommand(
                        type,
                        livingBlock, serverPlayer, target.position(), () -> this.command.command(livingBlock, serverPlayer, target)
                    );
                    return true;
                } else if (entity instanceof EnderDragonPart target) {
                    ActionStack.applyCommand(
                        type,
                        livingBlock,
                        serverPlayer,
                        target.position(),
                        () -> this.command.command(livingBlock, serverPlayer, target.parentMob)
                    );
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public static void applyCommand(
        CommandActionItem type,
        LivingBlock livingBlock,
        final ServerPlayer serverPlayer,
        final Vec3 targetPos,
        final Runnable action
    ) {
        List<LivingBlock> livingBlockEntities = serverPlayer.getCommandedBlocks();

        action.run();

        if (!livingBlockEntities.isEmpty()) {
            ActionStack.spawnCommandEntity(type, serverPlayer.level(), targetPos);
        }
    }

    public static void spawnCommandEntity(CommandActionItem type, final ServerLevel level, final Vec3 position) {
        LivingBlockCommand command = LivingBlockCommand.create(
            level,
            position.add(0.0, 0.1, 0.0),
            ((CommandActionItemAccessor) type).getCommandType()
        );
        level.addFreshEntity(command);
        level.sendParticles(ParticleTypes.END_ROD, position.x(), position.y() + 0.1, position.z(), 1, 0.0, 0.0, 0.0, 0.0);
    }
}
