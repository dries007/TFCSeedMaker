package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.WorldGen;

/**
 * @author Dries007
 */
public enum Stability
{
    SEISMIC_STABLE(110, false),
    SEISMIC_UNSTABLE(111, true);

    public final int id;
    public final boolean value;

    Stability(final int id, final boolean value)
    {
        this.id = id;
        this.value = value;
    }

    static
    {
        final int mul = 255 / values().length;
        for (Stability x : values())
        {
            final int id = x.ordinal() * mul;
            WorldGen.COLORS[x.id] = (id << 16) + (id << 8) + id;
        }
    }
}
