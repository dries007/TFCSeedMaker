package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.IDataType;
import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;

/**
 * @author Dries007
 */
public enum Ph implements IDataType
{
    PH_ACID_HIGH(130, 0),
    PH_ACID_LOW(131, 1),
    PH_NEUTRAL(132, 2),
    PH_ALKALINE_LOW(133, 3),
    PH_ALKALINE_HIGH(134, 4);

    public static final int MIN = PH_ACID_HIGH.id;
    public static final int MAX = PH_ALKALINE_HIGH.id;

    static
    {
        final int mul = 255 / values().length;
        for (Ph x : values())
        {
            final int id = x.ordinal() * mul;
            WorldGen.COLORS[x.id] = (id << 16) + (id << 8) + id;
        }
    }

    public final int id;
    public final int value;

    Ph(final int id, final int value)
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
