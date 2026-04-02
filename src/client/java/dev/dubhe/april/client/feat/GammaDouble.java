package dev.dubhe.april.client.feat;


import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;

import java.util.Optional;

public enum GammaDouble implements OptionInstance.SliderableValueSet<Double> {
    INSTANCE;

    public Optional<Double> validateValue(final Double value) {
        return value >= 0.0 && value <= 15.0 ? Optional.of(value) : Optional.empty();
    }

    public double toSliderValue(final Double value) {
        return value;
    }

    public Double fromSliderValue(final double slider) {
        return slider;
    }

    @Override
    public Codec<Double> codec() {
        return Codec.withAlternative(Codec.doubleRange(0.0, 15.0), Codec.BOOL, b -> b ? 15.0 : 0.0);
    }
}
