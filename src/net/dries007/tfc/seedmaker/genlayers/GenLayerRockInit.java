package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Rock;

public class GenLayerRockInit extends GenLayer
{
    final private Rock[] layerRocks;

    public GenLayerRockInit(final long par1, final Rock[] rocks)
    {
        super(par1);
        layerRocks = rocks;
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] out = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                initChunkSeed(x + xx, y + yy);
                out[xx + yy * sizeX] = layerRocks[nextInt(layerRocks.length)].id;
            }
        }

        return out;
    }
}
