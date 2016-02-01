package net.dries007.tfc.seedmaker.genlayers;

public class LayerIsland extends Layer
{
    public LayerIsland(final long seed)
    {
        super(seed);
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
                out[xx + yy * sizeX] = nextInt(4) == 0 ? 1 : 0;
            }
        }
        if (x > -sizeX && x <= 0 && y > -sizeY && y <= 0) out[-x + -y * sizeX] = 1;
        return out;
    }
}
