package net.dries007.tfc.seedmaker.genlayers;

public class GenLayerVoronoiZoom extends GenLayer
{
    public GenLayerVoronoiZoom(final long seed, final GenLayer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(int x, int y, final int sizeX, final int sizeY)
    {
        x -= 2;
        y -= 2;
        final int xZoom = x >> 2;
        final int yZoom = y >> 2;
        final int sizeXZoom = (sizeX >> 2) + 2;
        final int sizeYZoom = (sizeY >> 2) + 2;
        final int[] ints = parent.getInts(xZoom, yZoom, sizeXZoom, sizeYZoom);
        final int sizeXTemp = sizeXZoom - 1 << 2;
        final int sizeYTemp = sizeYZoom - 1 << 2;
        final int[] temp = new int[sizeXTemp * sizeYTemp];

        for (int yy = 0; yy < sizeYZoom - 1; ++yy)
        {
            int id1 = ints[yy * sizeXZoom];
            int id2 = ints[(yy + 1) * sizeXZoom];

            for (int xx = 0; xx < sizeXZoom - 1; ++xx)
            {
                initChunkSeed(xx + xZoom << 2, yy + yZoom << 2);
                final double d1 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
                final double d2 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
                initChunkSeed(xx + xZoom + 1 << 2, yy + yZoom << 2);
                final double d3 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
                final double d4 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
                initChunkSeed(xx + xZoom << 2, yy + yZoom + 1 << 2);
                final double d5 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
                final double d6 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
                initChunkSeed(xx + xZoom + 1 << 2, yy + yZoom + 1 << 2);
                final double d7 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
                final double d8 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
                final int id3 = ints[xx + 1 + (yy    ) * sizeXZoom] & 255;
                final int id4 = ints[xx + 1 + (yy + 1) * sizeXZoom] & 255;

                for (int i = 0; i < 4; ++i)
                {
                    int tempIndex = ((yy << 2) + i) * sizeXTemp + (xx << 2);

                    for (int j = 0; j < 4; ++j)
                    {
                        final double d9 = (i - d2) * (i - d2) + (j - d1) * (j - d1);
                        final double d10 = (i - d4) * (i - d4) + (j - d3) * (j - d3);
                        final double d11 = (i - d6) * (i - d6) + (j - d5) * (j - d5);
                        final double d12 = (i - d8) * (i - d8) + (j - d7) * (j - d7);

                        if (d9 < d10 && d9 < d11 && d9 < d12)
                        {
                            temp[tempIndex++] = id1;
                        }
                        else if (d10 < d9 && d10 < d11 && d10 < d12)
                        {
                            temp[tempIndex++] = id3;
                        }
                        else if (d11 < d9 && d11 < d10 && d11 < d12)
                        {
                            temp[tempIndex++] = id2;
                        }
                        else
                        {
                            temp[tempIndex++] = id4;
                        }
                    }
                }

                id1 = id3;
                id2 = id4;
            }
        }

        final int[] out = new int[sizeX * sizeY];
        for (int yy = 0; yy < sizeY; ++yy) System.arraycopy(temp, (yy + (y & 3)) * sizeXTemp + (x & 3), out, yy * sizeX, sizeX);
        return out;
    }
}
