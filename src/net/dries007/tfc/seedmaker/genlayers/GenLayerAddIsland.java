package net.dries007.tfc.seedmaker.genlayers;

public class GenLayerAddIsland extends GenLayer
{
    public GenLayerAddIsland(final long seed, final GenLayer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int sizeX_2 = sizeX + 2;
        final int sizeY_2 = sizeY + 2;
        final int[] ints = parent.getInts(x - 1, y - 1, sizeX_2, sizeY_2);
        final int[] out = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                final int dl = ints[xx +     yy       * sizeX_2]; // down left
                final int dr = ints[xx + 2 + yy       * sizeX_2]; // down right
                final int ul = ints[xx +     (yy + 2) * sizeX_2]; // up left
                final int ur = ints[xx + 2 + (yy + 2) * sizeX_2]; // up right
                final int us = ints[xx + 1 + (yy + 1) * sizeX_2]; // us
                initChunkSeed(xx + x, yy + y);

                if (us == 0 && (dl != 0 || dr != 0 || ul != 0 || ur != 0)) // We are OCEAN and any of our neighbours is not
                {
                    int countNonZero = 1;
                    int lastNonZero = 1;

                    if (dl != 0 && nextInt(countNonZero++) == 0) lastNonZero = dl;
                    if (dr != 0 && nextInt(countNonZero++) == 0) lastNonZero = dr;
                    if (ul != 0 && nextInt(countNonZero++) == 0) lastNonZero = ul;
                    if (ur != 0 && nextInt(countNonZero /*++*/) == 0) lastNonZero = ur;

                    if (nextInt(3) == 0) out[xx + yy * sizeX] = lastNonZero;
                    else out[xx + yy * sizeX] = 0;
                }
                else if (us != 0 && (dl == 0 || dr == 0 || ul == 0 || ur == 0)) // We are not OCEAN and any of our neighbours is
                {
                    if (nextInt(5) == 0) out[xx + yy * sizeX] = 0;
                    else out[xx + yy * sizeX] = us;
                }
                else
                {
                    out[xx + yy * sizeX] = us;
                }
            }
        }
        return out;
    }
}
