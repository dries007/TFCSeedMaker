package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.IDataType;
import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author Dries007
 */
public enum Biome implements IDataType
{
    /*
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
     */
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

    public static final List<Biome> SPAWNLIST = Arrays.asList(PLAINS, SWAMPLAND, ROLLING_HILLS, MOUNTAINS, MOUNTAINS_EDGE, HIGH_PLAINS);
    public static final List<Biome> ALLOWEDBIOMES = Arrays.asList(OCEAN, HIGH_HILLS, PLAINS, SWAMPLAND, ROLLING_HILLS, MOUNTAINS, HIGH_PLAINS);
    public static final Biome LIST[] = new Biome[256];

    static
    {
        HashSet<Integer> pool = new HashSet<>();
        for (Biome biome : values())
        {
            if (!pool.add(biome.id)) throw new RuntimeException("Duplicate Biome");
            LIST[biome.id] = biome;
            WorldGen.COLORS_BIOME[biome.id] = biome.color.getRGB();
        }
    }

    public final int id;
    public final Color color;

    Biome(final int id, final Color color)
    {
        this.id = id;
        this.color = color;
    }

    @Override
    public int getId()
    {
        return this.id;
    }

    @Override
    public Color getColor()
    {
        return this.color;
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
