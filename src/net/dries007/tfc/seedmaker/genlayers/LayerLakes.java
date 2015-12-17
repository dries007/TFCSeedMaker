package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Biome;

import static net.dries007.tfc.seedmaker.datatypes.Biome.LAKE;

public class LayerLakes extends Layer
{
    public LayerLakes(final long seed, final Layer parent)
    {
        super(seed, parent);
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] ints = parent.getInts(x - 1, y - 1, sizeX + 2, sizeY + 2);
        final int[] out = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                initChunkSeed(xx + x, yy + y);
                final int us = ints[xx + 1 + (yy + 1) * (sizeX + 2)];

                final int idD = ints[xx + 1 + (yy) * (sizeX + 2)]; // down
                final int idR = ints[xx + 2 + (yy + 1) * (sizeX + 2)]; // right
                final int idL = ints[xx + (yy + 1) * (sizeX + 2)]; // left
                final int idU = ints[xx + 1 + (yy + 2) * (sizeX + 2)]; // up

                if (Biome.isOceanicBiome(us))
                {
                    if (!Biome.isOceanicBiome(idD) && !Biome.isOceanicBiome(idR) && !Biome.isOceanicBiome(idL) && !Biome.isOceanicBiome(idU)) out[xx + yy * sizeX] = LAKE.id;
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
