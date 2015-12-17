package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Drainage;

public class LayerDrainageInit extends Layer
{
    public LayerDrainageInit(final long seed)
    {
        super(seed);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] outs = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                this.initChunkSeed(x + xx, y + yy);
                outs[xx + yy * sizeX] = Drainage.MIN + this.nextInt(5);
            }
        }

        return outs;
    }
}
