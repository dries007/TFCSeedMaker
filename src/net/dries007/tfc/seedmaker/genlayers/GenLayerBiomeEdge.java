package net.dries007.tfc.seedmaker.genlayers;

import static net.dries007.tfc.seedmaker.util.Biome.*;

public class GenLayerBiomeEdge extends GenLayer
{
    public GenLayerBiomeEdge(final long seed, final GenLayer parent)
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
                final int us  = ints[xx + 1 +     (yy + 1)     * (sizeX + 2)]; // us
                final int idD = ints[xx + 1 +     (yy + 1 - 1) * (sizeX + 2)]; // down
                final int idR = ints[xx + 1 + 1 + (yy + 1)     * (sizeX + 2)]; // right
                final int idL = ints[xx + 1 - 1 + (yy + 1)     * (sizeX + 2)]; // left
                final int idU = ints[xx + 1 +     (yy + 1 + 1) * (sizeX + 2)]; // up

                boolean allEqual = idD == idR && idR == idL && idL == idU && idU == us;

                if (us == HIGH_HILLS.id)
                {
                    if (allEqual) out[xx + yy * sizeX] = us;
                    else out[xx + yy * sizeX] = HIGH_HILLS_EDGE.id;
                }
                else if (us == MOUNTAINS.id)
                {
                    if (allEqual) out[xx + yy * sizeX] = us;
                    else out[xx + yy * sizeX] = MOUNTAINS_EDGE.id;
                }
                else if (us == SWAMPLAND.id)
                {
                    if (allEqual) out[xx + yy * sizeX] = us;
                    else out[xx + yy * sizeX] = PLAINS.id;
                }
                else if (us == HIGH_PLAINS.id)
                {
                    if (allEqual) out[xx + yy * sizeX] = us;
                    else out[xx + yy * sizeX] = PLAINS.id;
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
