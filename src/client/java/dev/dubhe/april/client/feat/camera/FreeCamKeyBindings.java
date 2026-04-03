package dev.dubhe.april.client.feat.camera;

import com.mojang.blaze3d.platform.InputConstants;
import dev.dubhe.april.client.AprilPlusClient;
import dev.dubhe.april.client.feat.group.GroupKeyBindings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class FreeCamKeyBindings {
    public static final KeyMapping FREE_CAMERA = new KeyMapping(
        GroupKeyBindings.key("free_camera"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_GRAVE_ACCENT,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );

    private FreeCamKeyBindings() {
    }

    public static void registerBindings() {
        KeyMappingHelper.registerKeyMapping(FREE_CAMERA);

        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            while (FREE_CAMERA.consumeClick()) {
                FreeCam.INSTANCE.toggle();
            }
        });
    }
}
