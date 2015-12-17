package net.dries007.tfc.seedmaker.genlayers;

import static net.dries007.tfc.seedmaker.datatypes.Biome.DEEP_OCEAN;
import static net.dries007.tfc.seedmaker.datatypes.Biome.OCEAN;

public class LayerDeepOcean extends Layer
{
    public LayerDeepOcean(final long seed, final Layer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int sizeX2 = sizeX + 2;
        final int sizeY2 = sizeY + 2;
        final int[] ints = parent.getInts(x - 1, y - 1, sizeX2, sizeY2);
        final int[] outCache = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                final int us = ints[xx + 1 + (yy + 1) * sizeX2];
                final int idU = ints[xx + 1 + yy * sizeX2];
                final int idR = ints[xx + 2 + (yy + 1) * sizeX2];
                final int idL = ints[xx + (yy + 1) * sizeX2];
                final int idD = ints[xx + 1 + (yy + 2) * sizeX2];

                int oceanCount = 0;
                if (idU == OCEAN.id) ++oceanCount;
                if (idR == OCEAN.id) ++oceanCount;
                if (idL == OCEAN.id) ++oceanCount;
                if (idD == OCEAN.id) ++oceanCount;

                if (us == OCEAN.id && oceanCount > 3) outCache[xx + yy * sizeX] = DEEP_OCEAN.id;
                else outCache[xx + yy * sizeX] = us;
            }
        }

        return outCache;
    }
}
