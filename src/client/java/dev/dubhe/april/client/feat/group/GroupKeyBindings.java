package dev.dubhe.april.client.feat.group;

import com.mojang.blaze3d.platform.InputConstants;
import dev.dubhe.april.AprilPlus;
import dev.dubhe.april.client.AprilPlusClient;
import dev.dubhe.april.network.SetGroupPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class GroupKeyBindings {
    public static final KeyMapping GROUP_NONE = new KeyMapping(
        GroupKeyBindings.key("group_none"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_0,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );
    public static final KeyMapping GROUP_1 = new KeyMapping(
        GroupKeyBindings.key("group_red"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_1,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );
    public static final KeyMapping GROUP_2 = new KeyMapping(
        GroupKeyBindings.key("group_blue"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_2,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );
    public static final KeyMapping GROUP_3 = new KeyMapping(
        GroupKeyBindings.key("group_lime"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_3,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );
    public static final KeyMapping GROUP_4 = new KeyMapping(
        GroupKeyBindings.key("group_yellow"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_4,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );
    public static final KeyMapping GROUP_5 = new KeyMapping(
        GroupKeyBindings.key("group_purple"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_5,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );
    public static final KeyMapping GROUP_6 = new KeyMapping(
        GroupKeyBindings.key("group_aqua"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_6,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );
    public static final KeyMapping GROUP_ALL = new KeyMapping(
        GroupKeyBindings.key("group_all"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_KP_DECIMAL,
        AprilPlusClient.APRIL_PLUS_CATEGORY
    );

    private GroupKeyBindings() {
    }

    public static void registerBindings() {
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
                GroupKeyBindings.setGroup(0);
            }
            while (GROUP_1.consumeClick()) {
                GroupKeyBindings.setGroup(1);
            }
            while (GROUP_2.consumeClick()) {
                GroupKeyBindings.setGroup(2);
            }
            while (GROUP_3.consumeClick()) {
                GroupKeyBindings.setGroup(3);
            }
            while (GROUP_4.consumeClick()) {
                GroupKeyBindings.setGroup(4);
            }
            while (GROUP_5.consumeClick()) {
                GroupKeyBindings.setGroup(5);
            }
            while (GROUP_6.consumeClick()) {
                GroupKeyBindings.setGroup(6);
            }
            while (GROUP_ALL.consumeClick()) {
                GroupKeyBindings.setGroup(7);
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
