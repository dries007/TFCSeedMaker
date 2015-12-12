package net.dries007.tfc.seedmaker.genlayers;

import static net.dries007.tfc.seedmaker.util.Biome.OCEAN;
import static net.dries007.tfc.seedmaker.util.Biome.RIVER;

public class GenLayerRiver extends GenLayer
{
    public GenLayerRiver(final long seed, final GenLayer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int sizeX2 = sizeX + 2;
        final int sizeY2 = sizeY + 2;
        final int[] ints = parent.getInts(x - 1, y - 1, sizeX2, sizeY2);
        final int[] out = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                final int us = calcWidth(ints[xx + 1 + (yy + 1) * sizeX2]);
                final int idL = calcWidth(ints[xx     + (yy + 1) * sizeX2]);
                final int idD = calcWidth(ints[xx + 1 + (yy    ) * sizeX2]);
                final int idR = calcWidth(ints[xx + 2 + (yy + 1) * sizeX2]);
                final int idU = calcWidth(ints[xx + 1 + (yy + 2) * sizeX2]);

                if (us == idL && us == idD && us == idR && us == idU) out[xx + yy * sizeX] = OCEAN.id;
                else out[xx + yy * sizeX] = RIVER.id;
            }
        }

        return out;
    }

    private int calcWidth(final int i)
    {
        return i >= 2 ? 2 + (i & 1) : i;
    }
}
