package net.dries007.tfc.seedmaker.genlayers;

public class GenLayerZoom extends GenLayer
{
    public GenLayerZoom(final long seed, final GenLayer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int xCoord = x >> 1;
        final int zCoord = y >> 1;
        final int sizeX2 = (sizeX >> 1) + 2;
        final int sizeY2 = (sizeY >> 1) + 2;
        final int[] ints = parent.getInts(xCoord, zCoord, sizeX2, sizeY2);
        final int i2 = sizeX2 - 1 << 1;
        final int j2 = sizeY2 - 1 << 1;
        final int[] temp = new int[i2 * j2];

        for (int yy = 0; yy < sizeY2 - 1; ++yy)
        {
            int tempIndex = (yy << 1) * i2;
            int us = ints[yy * sizeX2];
            int idU = ints[(yy + 1) * sizeX2];
            for (int xx = 0; xx < sizeX2 - 1; ++xx)
            {
                initChunkSeed(xx + xCoord << 1, yy + zCoord << 1);
                final int idR = ints[xx + 1 + yy * sizeX2];
                final int idUR = ints[xx + 1 + (yy + 1) * sizeX2];
                temp[tempIndex] = us;
                temp[tempIndex++ + i2] = selectRandom(us, idU);
                temp[tempIndex] = selectRandom(us, idR);
                temp[tempIndex++ + i2] = selectModeOrRandom(us, idR, idU, idUR);
                us = idR;
                idU = idUR;
            }
        }

        final int[] out = new int[sizeX * sizeY];
        for (int yy = 0; yy < sizeY; ++yy) System.arraycopy(temp, (yy + (y & 1)) * i2 + (x & 1), out, yy * sizeX, sizeX);
        return out;
    }

    protected int selectModeOrRandom(int i, int j, int l, int k)
    {
        if (j == l && l == k) return j;
        if (i == j && i == l) return i;
        if (i == j && i == k) return i;
        if (i == l && i == k) return i;
        if (i == j && l != k) return i;
        if (i == l && j != k) return i;
        if (i == k && j != l) return i;
        if (j == l && i != k) return j;
        if (j == k && i != l ) return j;
        if (l == k && i != j ) return l;
        return selectRandom(i, j, l, k);
    }
}
