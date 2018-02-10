package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.IDataType;
import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;

/**
 * @author Dries007
 */
public enum Drainage implements IDataType
{
    DRAINAGE_NONE(120, 0),
    DRAINAGE_VERY_POOR(121, 1),
    DRAINAGE_POOR(122, 2),
    DRAINAGE_NORMAL(123, 3),
    DRAINAGE_GOOD(124, 4),
    DRAINAGE_VERY_GOOD(125, 5);

    public static final int MIN = DRAINAGE_NONE.id;
    public static final int MAX = DRAINAGE_VERY_GOOD.id;

    static
    {
        final int mul = 255 / values().length;
        for (Drainage x : values())
        {
            final int id = x.ordinal() * mul;
            WorldGen.COLORS[x.id] = (id << 16) + (id << 8) + id;
        }
    }

    public final int id;
    public final int value;

    Drainage(final int id, final int value)
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
