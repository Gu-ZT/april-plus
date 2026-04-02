package dev.dubhe.april.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.dubhe.april.AprilPlus;
import dev.dubhe.april.client.feat.GammaManager;
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

    private KeyBindings() {
    }

    public static void registerBindings() {
        registerGammaToggleKey();
    }

    private static void registerGammaToggleKey() {
        KeyMappingHelper.registerKeyMapping(GAMMA_TOGGLE);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (GAMMA_TOGGLE.consumeClick()) {
                GammaManager.toggleGamma();
            }
        });
    }

    public static String key(String name) {
        return "key." + AprilPlus.MOD_ID + "." + name;
    }
}
