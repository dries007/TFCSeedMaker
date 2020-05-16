package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Biome;

public class LayerAddIsland extends Layer
{
    public LayerAddIsland(final long seed, final Layer parent)
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
                final int dl = ints[xx + yy * sizeX_2]; // down left
                final int dr = ints[xx + 2 + yy * sizeX_2]; // down right
                final int ul = ints[xx + (yy + 2) * sizeX_2]; // up left
                final int ur = ints[xx + 2 + (yy + 2) * sizeX_2]; // up right
                final int us = ints[xx + 1 + (yy + 1) * sizeX_2]; // us
                initChunkSeed(xx + x, yy + y);

                if (us == Biome.OCEAN.id && (dl != Biome.OCEAN.id || dr != Biome.OCEAN.id || ul != Biome.OCEAN.id || ur != Biome.OCEAN.id)) // We are OCEAN and any of our neighbours is not
                {
                    int countNonOcean = 1;
                    int lastNonOcean = Biome.PLAINS.id;

                    if (dl != Biome.OCEAN.id && nextInt(countNonOcean++) == 0) lastNonOcean = dl;
                    if (dr != Biome.OCEAN.id && nextInt(countNonOcean++) == 0) lastNonOcean = dr;
                    if (ul != Biome.OCEAN.id && nextInt(countNonOcean++) == 0) lastNonOcean = ul;
                    if (ur != Biome.OCEAN.id && nextInt(countNonOcean /*++*/) == 0) lastNonOcean = ur;

                    if (nextInt(3) == 0) out[xx + yy * sizeX] = lastNonOcean;
                    else out[xx + yy * sizeX] = Biome.OCEAN.id;
                }
                else if (us != Biome.OCEAN.id && (dl == Biome.OCEAN.id || dr == Biome.OCEAN.id || ul == Biome.OCEAN.id || ur == Biome.OCEAN.id)) // We are not OCEAN and any of our neighbours is
                {
                    if (nextInt(5) == 0) out[xx + yy * sizeX] = Biome.OCEAN.id;
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
