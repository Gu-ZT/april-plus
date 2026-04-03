package dev.dubhe.april.client;

import dev.dubhe.april.AprilPlus;
import dev.dubhe.april.client.feat.camera.FreeCamKeyBindings;
import dev.dubhe.april.client.feat.gamma.GammaKeyBindings;
import dev.dubhe.april.client.feat.group.GroupKeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;

public class AprilPlusClient implements ClientModInitializer {
    public static final KeyMapping.Category APRIL_PLUS_CATEGORY = KeyMapping.Category.register(AprilPlus.identifier("mappings"));

    @Override
    public void onInitializeClient() {
        GammaKeyBindings.registerBindings();
        FreeCamKeyBindings.registerBindings();
        GroupKeyBindings.registerBindings();
    }
}
