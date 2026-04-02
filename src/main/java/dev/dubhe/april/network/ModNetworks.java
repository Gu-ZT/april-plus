package dev.dubhe.april.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModNetworks {
    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(SetGroupPayload.TYPE, SetGroupPayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetGroupPayload.TYPE, SetGroupPayload::receive);
    }
}
