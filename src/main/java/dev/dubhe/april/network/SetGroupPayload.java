package dev.dubhe.april.network;

import dev.dubhe.april.AprilPlus;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.livingblock.LivingBlockGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public record SetGroupPayload(int groupId) implements CustomPacketPayload {
    public static final Type<SetGroupPayload> TYPE = new Type<>(AprilPlus.identifier("set_group"));

    public static final StreamCodec<ByteBuf, SetGroupPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        SetGroupPayload::groupId,
        SetGroupPayload::new
    );

    @Override
    public Type<SetGroupPayload> type() {
        return SetGroupPayload.TYPE;
    }

    public static void receive(SetGroupPayload payload, ServerPlayNetworking.Context context){
        ServerPlayer player = context.player();
        LivingBlockGroup nextGroup = LivingBlockGroup.BY_ID.apply(payload.groupId());
        player.setSelectedGroup(nextGroup);
        ItemStack itemStack = player.getInventory().getItem(6);
        if (!itemStack.isEmpty()) {
            itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(nextGroup.color()));
            Component name = Component.translatable(
                "item.minecraft.select_group_action.details",
                Component.translatable("living_blocks.group." + nextGroup.getSerializedName())
            );
            player.sendOverlayMessage(name);
        }
    }
}
