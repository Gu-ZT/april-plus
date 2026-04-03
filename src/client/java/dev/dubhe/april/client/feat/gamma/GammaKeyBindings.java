package dev.dubhe.april.client.feat.gamma;

import com.mojang.blaze3d.platform.InputConstants;
import dev.dubhe.april.client.AprilPlusClient;
import dev.dubhe.april.client.feat.group.GroupKeyBindings;
import dev.dubhe.april.client.feat.camera.FreeCam;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class GammaKeyBindings {
    public static final KeyMapping GAMMA_TOGGLE = new KeyMapping(
        GroupKeyBindings.key("gamma_toggle"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );

    private GammaKeyBindings() {
    }

    public static void registerBindings() {
        KeyMappingHelper.registerKeyMapping(GAMMA_TOGGLE);

        ClientTickEvents.START_CLIENT_TICK.register(_ -> FreeCam.INSTANCE.onClientTickStart());
        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            while (GAMMA_TOGGLE.consumeClick()) {
                GammaManager.toggleGamma();
            }
        });
    }
}
