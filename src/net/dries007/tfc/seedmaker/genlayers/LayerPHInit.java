package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Ph;

public class LayerPHInit extends Layer
{
    public LayerPHInit(final long seed)
    {
        super(seed);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] outCache = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                this.initChunkSeed(x + xx, y + yy);
                final int out = Ph.MIN + this.nextInt(4);
                outCache[xx + yy * sizeX] = out;
            }
        }

        return outCache;
    }
}
