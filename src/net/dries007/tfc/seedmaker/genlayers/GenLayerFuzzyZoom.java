package net.dries007.tfc.seedmaker.genlayers;

public class GenLayerFuzzyZoom extends GenLayer
{
    public GenLayerFuzzyZoom(final long seed, final GenLayer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int xZoom = x >> 1;
        final int yZoom = y >> 1;
        final int sizeXZoom = (sizeX >> 1) + 3;
        final int sizeYZoom = (sizeY >> 1) + 3;
        final int[] ints = parent.getInts(xZoom, yZoom, sizeXZoom, sizeYZoom);
        final int[] temp = new int[sizeXZoom * 2 * sizeYZoom * 2];
        final int xOffset = sizeXZoom << 1;

        for (int yy = 0; yy < sizeYZoom - 1; ++yy)
        {
            int yOffset = (yy << 1) * xOffset;
            int id1 = ints[yy * sizeXZoom];
            int id2 = ints[(yy + 1) * sizeXZoom];

            for (int xx = 0; xx < sizeXZoom - 1; ++xx)
            {
                initChunkSeed(xx + xZoom << 1, yy + yZoom << 1);
                final int id3 = ints[xx + 1 + yy * sizeXZoom];
                final int id4 = ints[xx + 1 + (yy + 1) * sizeXZoom];
                temp[yOffset] = id1;
                temp[yOffset++ + xOffset] = choose(id1, id2);
                temp[yOffset] = choose(id1, id3);
                temp[yOffset++ + xOffset] = choose(id1, id3, id2, id4);
                id1 = id3;
                id2 = id4;
            }
        }

        final int[] out = new int[sizeX * sizeY];
        for (int yy = 0; yy < sizeY; ++yy) System.arraycopy(temp, (yy + (y & 1)) * (sizeXZoom << 1) + (x & 1), out, yy * sizeX, sizeX);
        return out;
    }

    protected int choose(final int i, final int j)
    {
        return nextInt(2) == 0 ? i : j;
    }

    protected int choose(final int i, final int j, final int l, final int k)
    {
        int rnd = nextInt(4);
        return rnd == 0 ? i : rnd == 1 ? j : rnd == 2 ? l : k;
    }
}
