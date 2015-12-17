package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.Rain;

public class LayerAddRain extends Layer
{
    public LayerAddRain(final long seed, final Layer parent)
    {
        super(seed, parent);
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
                final int dl = ins[xx + yy * sizeX_2];
                final int dr = ins[xx + 2 + yy * sizeX_2];
                final int ul = ins[xx + (yy + 2) * sizeX_2];
                final int ur = ins[xx + 2 + (yy + 2) * sizeX_2];
                final int us = ins[xx + 1 + (yy + 1) * sizeX_2];
                this.initChunkSeed(xx + x, yy + y);

                if (dl > us || dr > us || ul > us || ur > us)
                {
                    int count = 1;
                    int out = us;

                    if (dl != 0 && this.nextInt(count++) == 0) out = dl + 1;
                    if (dr != 0 && this.nextInt(count++) == 0) out = dr + 1;
                    if (ul != 0 && this.nextInt(count++) == 0) out = ul + 1;
                    if (ur != 0 && this.nextInt(count/*++*/) == 0) out = ur + 1;

                    if (this.nextInt(3) == 0 && out <= Rain.WET) outs[xx + yy * sizeX] = out;
                    else outs[xx + yy * sizeX] = us;
                }
                else if (dl < us || dr < us || ul < us || ur < us)
                {
                    int count = 1;
                    int outID = us;

                    if (dl != 0 && this.nextInt(count++) == 0) outID = dl - 1;
                    if (dr != 0 && this.nextInt(count++) == 0) outID = dr - 1;
                    if (ul != 0 && this.nextInt(count++) == 0) outID = ul - 1;
                    if (ur != 0 && this.nextInt(count/*++*/) == 0) outID = ur - 1;

                    if (this.nextInt(3) == 0 && outID >= Rain.DRY) outs[xx + yy * sizeX] = outID;
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
