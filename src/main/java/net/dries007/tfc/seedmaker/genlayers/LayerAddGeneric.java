package net.dries007.tfc.seedmaker.genlayers;

public class LayerAddGeneric extends Layer
{
    private final int min;
    private final int max;

    public LayerAddGeneric(final long seed, final Layer parent, final int min, final int max)
    {
        super(seed, parent);
        this.min = min;
        this.max = max;
    }

    @Override
    public int[] getInts(final int x, final int y, final int sizeX, final int sizeY)
    {
        final int sizeX_2 = sizeX + 2;
        final int sizeY_2 = sizeY + 2;
        final int[] ins = this.parent.getInts(x - 1, y - 1, sizeX_2, sizeY_2);
        final int[] outs = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                final int dl = ins[xx + yy * sizeX_2]; // down left
                final int dr = ins[xx + 2 + yy * sizeX_2]; // down right
                final int ul = ins[xx + (yy + 2) * sizeX_2]; // up left
                final int ur = ins[xx + 2 + (yy + 2) * sizeX_2]; // up right
                final int us = ins[xx + 1 + (yy + 1) * sizeX_2]; // us
                this.initChunkSeed(xx + x, yy + y);

                if (dl > us || dr > us || ul > us || ur > us)
                {
                    int count = 1;
                    int out = us;

                    if (dl < max && this.nextInt(count++) == 0) out = dl + 1;
                    if (dr < max && this.nextInt(count++) == 0) out = dr + 1;
                    if (ul < max && this.nextInt(count++) == 0) out = ul + 1;
                    if (ur < max && this.nextInt(count/*++*/) == 0) out = ur + 1;

                    if (this.nextInt(3) == 0 && out <= max) outs[xx + yy * sizeX] = out;
                    else outs[xx + yy * sizeX] = us;
                }
                else if (dl < us || dr < us || ul < us || ur < us)
                {
                    int count = 1;
                    int out = us;

                    if (dl > min && this.nextInt(count++) == 0) out = dl - 1;
                    if (dr > min && this.nextInt(count++) == 0) out = dr - 1;
                    if (ul > min && this.nextInt(count++) == 0) out = ul - 1;
                    if (ur > min && this.nextInt(count/*++*/) == 0) out = ur - 1;

                    if (this.nextInt(3) == 0 && out >= min) outs[xx + yy * sizeX] = out;
                    else outs[xx + yy * sizeX] = us;
                }
                else
                {
                    outs[xx + yy * sizeX] = us;
                }
            }
        }
        return outs;
    }
}
