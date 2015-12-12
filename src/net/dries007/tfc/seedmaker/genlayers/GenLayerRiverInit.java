package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.util.Biome;

public class GenLayerRiverInit extends GenLayer
{
    public GenLayerRiverInit(final long seed, final GenLayer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] ints = parent.getInts(x, y, sizeX, sizeY);
        final int[] out = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                initChunkSeed(xx + x, yy + y);
                final int index = xx + yy * sizeX;
                final int id = ints[index];
                out[index] = !Biome.isOceanicBiome(id) && !Biome.isMountainBiome(id) ? 1 : 0;
            }
        }
        return out;
    }
}
