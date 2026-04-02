package dev.dubhe.april.client.feat.gamma;

public class GammaManager {
    private static double value = 1.0;

    public static void toggleGamma() {
        GammaManager.value = GammaManager.value != 15.0 ? 15.0 : 1.0;
    }

    public static double getGamma() {
        return GammaManager.value;
    }

    public static void setGamma(double value) {
        GammaManager.value = value;
    }
}
