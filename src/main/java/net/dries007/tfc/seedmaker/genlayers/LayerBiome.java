package net.dries007.tfc.seedmaker.genlayers;

import static net.dries007.tfc.seedmaker.datatypes.Biome.*;

public class LayerBiome extends Layer
{
    public LayerBiome(final long seed, final Layer parent)
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
                final int id = ints[xx + yy * sizeX];
                if (isOceanicBiome(id)) out[xx + yy * sizeX] = id;
                else out[xx + yy * sizeX] = ALLOWEDBIOMES.get(nextInt(ALLOWEDBIOMES.size())).id;
            }
        }
        return out;
    }
}
