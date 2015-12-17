package net.dries007.tfc.seedmaker.datatypes;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author Dries007
 */
public enum Biome
{
    OCEAN(0, new Color(0x3A3CEC), 0.00001F),
    PLAINS(1, new Color(0x4AA02C), 0.16F),
    LAKE(2, new Color(0x66B2EC), 0.001F),
    HIGH_HILLS(3, new Color(0xC5BD06), 1.6F),
    SWAMPLAND(6, new Color(0x158864), 0.1F),
    RIVER(7, new Color(0x00B1AF), -0.3F),
    BEACH(16, new Color(0x8D6AA1), 0.02F),
    GRAVEL_BEACH(17, new Color(0x821CA1), 0.02F),
    HIGH_HILLS_EDGE(20, new Color(0xC47D21), 0.4F),
    ROLLING_HILLS(30, new Color(0x9E5569), 0.4F),
    MOUNTAINS(31, new Color(0x940007), 1.6F),
    MOUNTAINS_EDGE(32, new Color(0x7A0040), 0.8F),
    HIGH_PLAINS(35, new Color(0x348017), 0.43F),
    DEEP_OCEAN(36, new Color(0x000059), 0.00001F);

    public static final List<Biome> SPAWNLIST = Arrays.asList(PLAINS, ROLLING_HILLS, SWAMPLAND, MOUNTAINS, HIGH_PLAINS);
    public static final List<Biome> ALLOWEDBIOMES = Arrays.asList(OCEAN, HIGH_HILLS, PLAINS, HIGH_PLAINS, SWAMPLAND, ROLLING_HILLS, MOUNTAINS);
    public static final Biome LIST[] = new Biome[256];
    public static final int COLORS[] = new int[256];

    static
    {
        HashSet<Integer> pool = new HashSet<>();
        for (Biome biome : values())
        {
            if (!pool.add(biome.id)) throw new RuntimeException("Duplicate Biome");
            LIST[biome.id] = biome;
            COLORS[biome.id] = biome.color.getRGB();
        }
    }

    public final int id;
    public final Color color;
    public final float heightVariation;

    Biome(final int id, final Color color, final float heightVariation)
    {
        this.id = id;
        this.color = color;
        this.heightVariation = heightVariation - 2.7F;
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
