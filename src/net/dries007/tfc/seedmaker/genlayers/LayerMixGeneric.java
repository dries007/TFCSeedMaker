package net.dries007.tfc.seedmaker.genlayers;

public class LayerMixGeneric extends Layer
{
    private final int min;
    private final int max;

    public LayerMixGeneric(final long seed, final Layer parent, final int min, final int max)
    {
        super(seed, parent);
        this.min = min;
        this.max = max;
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int[] ins = this.parent.getInts(x - 1, y - 1, sizeX + 2, sizeY + 2);
        final int[] outs = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                this.initChunkSeed(xx + x, yy + y);
                final int idD = ins[xx + 1 + yy * (sizeX + 2)];
                final int idL = ins[xx + 2 + (yy + 1) * (sizeX + 2)];
                final int idR = ins[xx + (yy + 1) * (sizeX + 2)];
                final int idU = ins[xx + 1 + (yy + 2) * (sizeX + 2)];
                int us = ins[xx + 1 + (yy + 1) * (sizeX + 2)];

                if (idD >= us + 2 || idL >= us + 2 || idR >= us + 2 || idU >= us + 2) if (us + 1 < max) us++;
                if (idD <= us - 2 || idL <= us - 2 || idR <= us - 2 || idU <= us - 2) if (us - 1 > min) us--;

                outs[xx + yy * sizeX] = us;
            }
        }
        return outs;
    }
}