package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Rain;

public class LayerRainMix extends Layer
{
    public LayerRainMix(final long seed, final Layer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] ints = this.parent.getInts(x - 1, y - 1, sizeX + 2, sizeY + 2);
        final int[] outs = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                this.initChunkSeed(xx + x, yy + y);
                final int idD = ints[xx + 1 + (yy) * (sizeX + 2)];
                final int idL = ints[xx + 2 + (yy + 1) * (sizeX + 2)];
                final int idR = ints[xx + (yy + 1) * (sizeX + 2)];
                final int idU = ints[xx + 1 + (yy + 2) * (sizeX + 2)];
                int us = ints[xx + 1 + (yy + 1) * (sizeX + 2)];

                if (idD >= us + 2 || idL >= us + 2 || idR >= us + 2 || idU >= us + 2) if (us + 1 < Rain.RAIN_8000.id) us++;
                if (idD <= us - 2 || idL <= us - 2 || idR <= us - 2 || idU <= us - 2) if (us - 1 > Rain.RAIN_62_5.id) us--;

                outs[xx + yy * sizeX] = us;
            }
        }
        return outs;
    }
}
