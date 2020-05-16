package net.dries007.tfc.seedmaker.datatypes;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dries007
 */
public enum Biome
{
    OCEAN(0, new Color(0x3232C8)),
    RIVER(1, new Color(0x2B8CBA)),
    BEACH(2, new Color(0xC7A03B)),
    GRAVEL_BEACH(3, new Color(0x7E7450)),
    HIGH_HILLS(4, new Color(0x920072)),
    PLAINS(5, new Color(0x346B25)),
    SWAMPLAND(6, new Color(0x099200)),
    HIGH_HILLS_EDGE(7, new Color(0x92567C)),
    ROLLING_HILLS(8, new Color(0x734B92)),
    MOUNTAINS(9, new Color(0x920000)),
    MOUNTAINS_EDGE(10, new Color(0x924A4C)),
    HIGH_PLAINS(11, new Color(0x225031)),
    DEEP_OCEAN(12, new Color(0x000080)),
    LAKE(13, new Color(0x5D8C8D));

    public static final List<Biome> ALLOWEDBIOMES = Arrays.asList(OCEAN, HIGH_HILLS, PLAINS, SWAMPLAND, ROLLING_HILLS, MOUNTAINS, HIGH_PLAINS);
    public static final int[] COLORS = new int[values().length];

    static
    {
        for (Biome biome : values()) COLORS[biome.id] = biome.color.getRGB();
    }

    public final int id;
    public final Color color;

    Biome(final int id, final Color color)
    {
        this.id = id;
        this.color = color;
    }

    public static boolean isOceanicBiome(int id)
    {
        return id == OCEAN.id || id == DEEP_OCEAN.id;
    }

    public static boolean isWaterBiome(int id)
    {
        return id == OCEAN.id || id == DEEP_OCEAN.id || id == LAKE.id || id == RIVER.id;
    }

    public static boolean isMountainBiome(int id)
    {
        return id == MOUNTAINS.id || id == MOUNTAINS_EDGE.id;
    }

    public static boolean isBeachBiome(int id)
    {
        return id == BEACH.id || id == GRAVEL_BEACH.id;
    }
}
