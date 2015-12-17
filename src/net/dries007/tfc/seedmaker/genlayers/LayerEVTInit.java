package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Evt;

public class LayerEVTInit extends Layer
{
    public LayerEVTInit(final long seed)
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
                int out = Evt.LOW + this.nextInt(4);
                /*
                 * We want to make High EVT areas slightly more rare so that there is more vegetation than not
				 * so we hide it behind another rand
				 * */
                if (out == Evt.LOW && this.nextInt(4) == 0) out += 1 + this.nextInt(2);
                if (out == Evt.LOW && this.nextInt(12) == 0) out--;
                if (out == Evt.HIGH && this.nextInt(12) == 0) out++;

                outCache[xx + yy * sizeX] = out;
            }
        }

        return outCache;
    }
}
