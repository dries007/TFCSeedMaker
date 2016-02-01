package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Tree;

public class LayerTreeInit extends Layer
{
    private final Tree[] layerTrees;

    public LayerTreeInit(final long seed, final Tree[] trees)
    {
        super(seed);
        layerTrees = trees;
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] cache = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                initChunkSeed(x + xx, y + yy);
                cache[xx + yy * sizeX] = layerTrees[nextInt(layerTrees.length)].id;
            }
        }

        return cache;
    }
}
