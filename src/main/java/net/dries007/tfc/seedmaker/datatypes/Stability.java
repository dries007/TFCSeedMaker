package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.IDataType;
import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;

/**
 * @author Dries007
 */
public enum Stability implements IDataType
{
    SEISMIC_STABLE(110, false),
    SEISMIC_UNSTABLE(111, true);

    static
    {
        final int mul = 255 / values().length;
        for (Stability x : values())
        {
            final int id = x.ordinal() * mul;
            WorldGen.COLORS[x.id] = (id << 16) + (id << 8) + id;
        }
    }

    public final int id;
    public final boolean value;

    Stability(final int id, final boolean value)
    {
        this.id = id;
        this.value = value;
    }

    @Override
    public int getId()
    {
        return this.id;
    }

    @Override
    public Color getColor()
    {
        return new Color(WorldGen.COLORS[this.id]);
    }
}
