package dev.dubhe.april;

import com.mojang.logging.LogUtils;
import dev.dubhe.april.network.ModNetworks;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public class AprilPlus implements ModInitializer {
    public static final String MOD_ID = "april_plus";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        ModNetworks.register();
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(AprilPlus.MOD_ID, path);
    }
}
