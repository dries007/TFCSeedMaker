package net.dries007.tfc.seedmaker.genlayers;

public class LayerIsland extends Layer
{
    public LayerIsland(final long seed)
    {
        super(seed);
    }

    @Override
    public int[] getInts(final int x, final int z, final int sizeX, final int sizeZ)
    {
        final int[] out = new int[sizeX * sizeZ];

        for (int zz = 0; zz < sizeZ; ++zz)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                initChunkSeed(x + xx, z + zz);
                out[xx + zz * sizeX] = nextInt(4) == 0 ? 1 : 0;
            }
        }
        if (x > -sizeX && x <= 0 && z > -sizeZ && z <= 0) out[-x + -z * sizeX] = 1;
        return out;
    }
}
