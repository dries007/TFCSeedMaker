package net.dries007.tfc.seedmaker.genlayers;

public class GenLayerSmooth extends GenLayer
{
    public GenLayerSmooth(final long seed, final GenLayer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
//        final int var5 = x - 1;
//        final int var6 = y - 1;
        final int sizeX2 = sizeX + 2;
        final int sizeY2 = sizeY + 2;
        final int[] ints = parent.getInts(x - 1, y - 1, sizeX2, sizeY2);
        final int[] out = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                int us = ints[xx + 1 + (yy + 1) * sizeX2];
                final int idD = ints[xx + 1 + (yy    ) * sizeX2];
                final int idL = ints[xx     + (yy + 1) * sizeX2];
                final int idR = ints[xx + 2 + (yy + 1) * sizeX2];
                final int idU = ints[xx + 1 + (yy + 2) * sizeX2];

                if (idL == idR && idD == idU)
                {
                    initChunkSeed(xx + x, yy + y);
                    if (nextInt(2) == 0) us = idL;
                    else us = idD;
                }
                else
                {
                    if (idL == idR) us = idL;
                    if (idD == idU) us = idD;
                }
                if (us < 0)
                {
                    try
                    {
                        throw new Exception();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                out[xx + yy * sizeX] = us;
            }
        }
        return out;
    }
}
