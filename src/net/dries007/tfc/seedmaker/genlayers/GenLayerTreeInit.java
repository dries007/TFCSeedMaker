package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Tree;

public class GenLayerTreeInit extends GenLayer
{
	private Tree[] layerTrees;
	public GenLayerTreeInit(final long seed, final Tree[] trees)
	{
		super(seed);
		layerTrees = trees;
	}

	@Override
	public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
	{
		int[] cache = new int[sizeX * sizeY];

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
