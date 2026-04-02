package dev.dubhe.april.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.dubhe.april.extension.LivingBlockExtension;
import dev.dubhe.april.feat.interact.InteractImprovements;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.interact.OnInteract;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ActionItem;
import net.minecraft.world.item.GroupAction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingBlock.class)
abstract class LivingBlockMixin extends Entity implements LivingBlockExtension {
    @Shadow
    private OnInteract onInteract;
    @Unique
    @Nullable
    private Player aprilPlus$lastInteractPlayer;
    @Unique
    private long aprilPlus$lastInteractTime = 0;
    @Unique
    private boolean aprilPlus$lastShiftDown = false;

    public LivingBlockMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

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
        Operation<InteractionResult> original,
        @Local(argsOnly = true, name = "hand") final InteractionHand hand,
        @Local(argsOnly = true, name = "location") final Vec3 location
    ) {
        long currentTimeMillis = System.currentTimeMillis();
        boolean proxyInteract = false;
        if (target.canBeControlledBy(player) && !(instance instanceof GroupAction)) {
            boolean doubleClick = this.aprilPlus$lastInteractPlayer != null
                                  && this.aprilPlus$lastInteractPlayer.is(player)
                                  && currentTimeMillis - this.aprilPlus$lastInteractTime < 200;
            proxyInteract = InteractImprovements.interactLivingBlock(
                player,
                target,
                doubleClick,
                this.aprilPlus$lastShiftDown,
                hand,
                location
            );
        }
        this.aprilPlus$lastInteractTime = currentTimeMillis;
        this.aprilPlus$lastInteractPlayer = player;
        this.aprilPlus$lastShiftDown = player.isShiftKeyDown();
        if (proxyInteract) {
            return InteractionResult.SUCCESS;
        }
        return original.call(instance, player, target);
    }

    @Override
    public OnInteract aprilPlus$getOnInteract() {
        return this.onInteract;
    }

    @Override
    public InteractionResult aprilPlus$superInteract(final Player player, final InteractionHand hand, final Vec3 location) {
        return super.interact(player, hand, location);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void onSave(ValueOutput output, CallbackInfo ci) {
        LivingBlock self = (LivingBlock) (Object) this;
        boolean interacted = self.getEntityData().get(LivingBlockAccessor.getDataPlayerInteracted());
        output.putBoolean("player_interacted", interacted);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void onLoad(ValueInput input, CallbackInfo ci) {
        LivingBlock self = (LivingBlock) (Object) this;
        boolean interacted = input.getBooleanOr("player_interacted", false);
        self.getEntityData().set(LivingBlockAccessor.getDataPlayerInteracted(), interacted);
    }
}
