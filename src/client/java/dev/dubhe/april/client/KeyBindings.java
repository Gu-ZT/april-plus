package dev.dubhe.april.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.dubhe.april.AprilPlus;
import dev.dubhe.april.client.feat.camera.FreeCam;
import dev.dubhe.april.client.feat.gamma.GammaManager;
import dev.dubhe.april.network.SetGroupPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
    public static final KeyMapping GROUP_NONE = new KeyMapping(
        KeyBindings.key("group_none"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_0,
        GAMMA_CATEGORY
    );
    public static final KeyMapping GROUP_1 = new KeyMapping(
        KeyBindings.key("group_red"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_1,
        GAMMA_CATEGORY
    );
    public static final KeyMapping GROUP_2 = new KeyMapping(
        KeyBindings.key("group_blue"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_2,
        GAMMA_CATEGORY
    );
    public static final KeyMapping GROUP_3 = new KeyMapping(
        KeyBindings.key("group_lime"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_3,
        GAMMA_CATEGORY
    );
    public static final KeyMapping GROUP_4 = new KeyMapping(
        KeyBindings.key("group_yellow"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_4,
        GAMMA_CATEGORY
    );
    public static final KeyMapping GROUP_5 = new KeyMapping(
        KeyBindings.key("group_purple"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_5,
        GAMMA_CATEGORY
    );
    public static final KeyMapping GROUP_6 = new KeyMapping(
        KeyBindings.key("group_aqua"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_6,
        GAMMA_CATEGORY
    );
    public static final KeyMapping GROUP_ALL = new KeyMapping(
        KeyBindings.key("group_all"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_DECIMAL,
        GAMMA_CATEGORY
    );

    private KeyBindings() {
    }

    public static void registerBindings() {
        registerGammaToggleKey();
        registerFreeCameraKey();
        registerGroupKey();
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

    private static void registerGroupKey() {
        KeyMappingHelper.registerKeyMapping(GROUP_NONE);
        KeyMappingHelper.registerKeyMapping(GROUP_1);
        KeyMappingHelper.registerKeyMapping(GROUP_2);
        KeyMappingHelper.registerKeyMapping(GROUP_3);
        KeyMappingHelper.registerKeyMapping(GROUP_4);
        KeyMappingHelper.registerKeyMapping(GROUP_5);
        KeyMappingHelper.registerKeyMapping(GROUP_6);
        KeyMappingHelper.registerKeyMapping(GROUP_ALL);

        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            while (GROUP_NONE.consumeClick()) {
                KeyBindings.setGroup(0);
            }
            while (GROUP_1.consumeClick()) {
                KeyBindings.setGroup(1);
            }
            while (GROUP_2.consumeClick()) {
                KeyBindings.setGroup(2);
            }
            while (GROUP_3.consumeClick()) {
                KeyBindings.setGroup(3);
            }
            while (GROUP_4.consumeClick()) {
                KeyBindings.setGroup(4);
            }
            while (GROUP_5.consumeClick()) {
                KeyBindings.setGroup(5);
            }
            while (GROUP_6.consumeClick()) {
                KeyBindings.setGroup(6);
            }
            while (GROUP_ALL.consumeClick()) {
                KeyBindings.setGroup(7);
            }
        });
    }

    private static void setGroup(int id) {
        ClientPlayNetworking.send(new SetGroupPayload(id));
    }

    public static String key(String name) {
        return "key." + AprilPlus.MOD_ID + "." + name;
    }
}
