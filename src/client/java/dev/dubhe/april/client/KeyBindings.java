package dev.dubhe.april.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.dubhe.april.AprilPlus;
import dev.dubhe.april.client.feat.camera.FreeCam;
import dev.dubhe.april.client.feat.gamma.GammaManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static final KeyMapping.Category GAMMA_CATEGORY = KeyMapping.Category.register(AprilPlus.identifier("mappings"));
    public static final KeyMapping GAMMA_TOGGLE = new KeyMapping(
        KeyBindings.key("gamma_toggle"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        GAMMA_CATEGORY
    );
    public static final KeyMapping FREE_CAMERA = new KeyMapping(
        KeyBindings.key("free_camera"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_GRAVE_ACCENT,
        GAMMA_CATEGORY
    );

    private KeyBindings() {
    }

    public static void registerBindings() {
        registerGammaToggleKey();
        registerFreeCameraKey();
    }

    private static void registerGammaToggleKey() {
        KeyMappingHelper.registerKeyMapping(GAMMA_TOGGLE);

        ClientTickEvents.START_CLIENT_TICK.register(_ -> FreeCam.INSTANCE.onClientTickStart());
        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            while (GAMMA_TOGGLE.consumeClick()) {
                GammaManager.toggleGamma();
            }
        });
    }

    private static void registerFreeCameraKey() {
        KeyMappingHelper.registerKeyMapping(FREE_CAMERA);

        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            while (FREE_CAMERA.consumeClick()) {
                FreeCam.INSTANCE.toggle();
            }
        });
    }

    public static String key(String name) {
        return "key." + AprilPlus.MOD_ID + "." + name;
    }
}
