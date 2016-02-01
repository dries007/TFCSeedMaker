package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Stability;

public class LayerStabilityInit extends Layer
{
    public LayerStabilityInit(final long seed)
    {
        super(seed);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] cache = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                this.initChunkSeed(x + xx, y + yy);
                cache[xx + yy * sizeX] = this.nextInt(3) == 0 ? Stability.SEISMIC_UNSTABLE.id : Stability.SEISMIC_STABLE.id;
            }
        }

        return cache;
    }
}
