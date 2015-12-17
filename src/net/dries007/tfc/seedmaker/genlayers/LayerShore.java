package net.dries007.tfc.seedmaker.genlayers;

import static net.dries007.tfc.seedmaker.datatypes.Biome.*;

public class LayerShore extends Layer
{
    public LayerShore(final long seed, final Layer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] ints = parent.getInts(x - 1, y - 1, sizeX + 2, sizeY + 2);
        final int[] out = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                initChunkSeed(yy + x, xx + y);
                final int us = ints[xx + 1 + (yy + 1) * (sizeX + 2)];

                if (!isOceanicBiome(us) && us != RIVER.id && us != SWAMPLAND.id && us != HIGH_HILLS.id)
                {
                    final int idD = ints[xx + 1 + (yy) * (sizeX + 2)];
                    final int idR = ints[xx + 2 + (yy + 1) * (sizeX + 2)];
                    final int idL = ints[xx + (yy + 1) * (sizeX + 2)];
                    final int idU = ints[xx + 1 + (yy + 2) * (sizeX + 2)];

                    if (!isOceanicBiome(idD) && !isOceanicBiome(idR) && !isOceanicBiome(idL) && !isOceanicBiome(idU)) out[xx + yy * sizeX] = us;
                    else
                    {
                        if (isMountainBiome(us)) out[xx + yy * sizeX] = GRAVEL_BEACH.id;
                        else out[xx + yy * sizeX] = BEACH.id;
                    }
                }
                else out[xx + yy * sizeX] = us;
            }
        }
        return out;
    }
}
