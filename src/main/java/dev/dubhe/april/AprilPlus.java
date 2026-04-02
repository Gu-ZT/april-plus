package dev.dubhe.april;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class AprilPlus implements ModInitializer {
    public static final String MOD_ID = "april_plus";

    @Override
    public void onInitialize() {
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(AprilPlus.MOD_ID, path);
    }
}
