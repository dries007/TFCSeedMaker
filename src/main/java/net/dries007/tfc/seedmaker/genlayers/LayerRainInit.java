package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Rain;

public class LayerRainInit extends Layer
{
    public LayerRainInit(final long seed)
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
                int out = Rain.DRY + this.nextInt(6);

                if (out == Rain.DRY && this.nextInt(12) == 0) out--;
                if (out == Rain.WET && this.nextInt(12) == 0) out++;

                outCache[xx + yy * sizeX] = out;
            }
        }

        return outCache;
    }
}
