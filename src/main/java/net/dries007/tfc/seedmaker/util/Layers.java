package net.dries007.tfc.seedmaker.util;

import net.dries007.tfc.seedmaker.datatypes.Biome;
import net.dries007.tfc.seedmaker.datatypes.Drainage;
import net.dries007.tfc.seedmaker.datatypes.Rock;
import net.dries007.tfc.seedmaker.datatypes.Stability;
import net.dries007.tfc.seedmaker.genlayers.Layer;

/**
 * @author Dries007
 */
public enum Layers
{
    BIOMES(Biome.class),
    ROCK_TOP(Rock.class),
    ROCK_MIDDLE(Rock.class),
    ROCK_BOTTOM(Rock.class),
    STABILITY(Stability.class),
    DRAINAGE(Drainage.class),
    ;

    public final Class<?> enumClass;

    Layers(Class<?> enumClass)
    {
        this.enumClass = enumClass;
    }

    public Layer init(long seed)
    {
        switch (this)
        {
            case BIOMES: return Layer.initBiomes(seed);
            case ROCK_TOP: return Layer.initRock(seed + 1, Rock.TOP);
            case ROCK_MIDDLE: return Layer.initRock(seed + 2, Rock.MIDDLE);
            case ROCK_BOTTOM: return Layer.initRock(seed + 3, Rock.BOTTOM);
            case STABILITY: return Layer.initStability(seed + 9);
            case DRAINAGE: return Layer.initDrain(seed + 11);
        }
        throw new RuntimeException("Missing layer init");
    }

    public int[] colors()
    {
        switch (this)
        {
            case BIOMES: return Biome.COLORS;
            case ROCK_TOP:
            case ROCK_MIDDLE:
            case ROCK_BOTTOM:
                return Rock.COLORS;
            case STABILITY:
                return new int[] {0xFFBABABA, 0xFF5E5E5E};
            case DRAINAGE:
                return new int[] {0xFF333333, 0xFF555555, 0xFF777777, 0xFF999999, 0xFFAAAAAA, 0xFFEEEEEE};
        }
        throw new RuntimeException("Missing layer colors");
    }

    public static Layers layersForClass(Class<?> x)
    {
        for (Layers value : values())
        {
            if (value.enumClass == x) return value;
        }
        throw new RuntimeException("Missing layers");
    }
}
